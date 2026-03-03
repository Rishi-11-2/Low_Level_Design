package service;

import dto.AccountStatement;
import dto.AddMoneyRequest;
import dto.NotificationMessage;
import dto.TransactionRequest;
import model.Transaction;
import model.Wallet;
import model.enums.TransactionStatus;
import model.enums.TransactionType;
import model.enums.WalletStatus;
import repository.TransactionRepository;
import repository.WalletRepository;
import strategy.notification.NotificationRouter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TransactionService — core business logic for all wallet operations.
 *
 * Handles: Transfer, Deposit (two-phase), Withdraw, and Account Statements.
 * Uses LockService for concurrency control (distributed locking approach).
 * Uses PaymentGatewayService for deposit payment integration.
 * Uses NotificationRouter for event-driven notifications.
 *
 * Key design decisions:
 * - Locks acquired in sorted wallet ID order to prevent deadlocks
 * - All amounts in minor units (long) — no floating-point arithmetic
 * - Minimum transfer/deposit amount = 1 minor unit
 * - Idempotent callback handling (only PENDING → COMPLETED/FAILED transitions)
 */
public class TransactionService {

    private static final long LOCK_TIMEOUT_MS = 5000; // 5 seconds
    private static final long MIN_AMOUNT = 1; // 1 minor unit = 0.01 TUF

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final PaymentGatewayService paymentGatewayService;
    private final LockService lockService;
    private final NotificationRouter notificationRouter;

    public TransactionService(WalletRepository walletRepository,
                              TransactionRepository transactionRepository,
                              WalletService walletService,
                              PaymentGatewayService paymentGatewayService,
                              LockService lockService,
                              NotificationRouter notificationRouter) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
        this.paymentGatewayService = paymentGatewayService;
        this.lockService = lockService;
        this.notificationRouter = notificationRouter;
    }

    // =========================================================================
    // USE CASE 1: TRANSFER
    // =========================================================================

    /**
     * Transfer funds between two wallets.
     *
     * Flow:
     * 1. Validate: non-negative amount, amount >= min, not self-transfer
     * 2. Fetch wallets; ensure both ACTIVE
     * 3. Acquire distributed locks on both wallet IDs in sorted order
     * 4. Within atomic operation: check balance, debit source, credit destination
     * 5. Create Transaction(COMPLETED, type=TRANSFER)
     * 6. Release locks; send notification
     */
    public Transaction transfer(TransactionRequest request) {
        System.out.println("\n[TransactionService] Transfer: " + request);

        // --- Validation ---
        validateAmount(request.getAmount());

        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new IllegalArgumentException("Self-transfer not allowed");
        }

        // --- Fetch wallets ---
        Wallet fromWallet = walletService.getByAccountNumber(request.getFromAccountNumber());
        Wallet toWallet = walletService.getByAccountNumber(request.getToAccountNumber());

        validateWalletActive(fromWallet, "Source");
        validateWalletActive(toWallet, "Destination");

        // --- Acquire locks in sorted order (prevent deadlocks) ---
        String lockKey1, lockKey2;
        if (fromWallet.getId() < toWallet.getId()) {
            lockKey1 = "wallet_lock_" + fromWallet.getId();
            lockKey2 = "wallet_lock_" + toWallet.getId();
        } else {
            lockKey1 = "wallet_lock_" + toWallet.getId();
            lockKey2 = "wallet_lock_" + fromWallet.getId();
        }

        if (!lockService.acquire(lockKey1, LOCK_TIMEOUT_MS)) {
            throw new RuntimeException("Failed to acquire lock on wallet (timeout)");
        }
        if (!lockService.acquire(lockKey2, LOCK_TIMEOUT_MS)) {
            lockService.release(lockKey1);
            throw new RuntimeException("Failed to acquire lock on wallet (timeout)");
        }

        try {
            // --- Atomic balance update ---
            if (fromWallet.getBalance() < request.getAmount()) {
                throw new IllegalArgumentException("Insufficient funds: balance="
                        + fromWallet.getFormattedBalance() + ", requested="
                        + String.format("%.2f TUF", request.getAmount() / 100.0));
            }

            fromWallet.debit(request.getAmount());
            toWallet.credit(request.getAmount());

            // --- Create transaction record ---
            Transaction transaction = new Transaction(
                    fromWallet.getId(), toWallet.getId(), request.getAmount(),
                    TransactionType.TRANSFER, request.getDescription());
            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);

            System.out.println("[TransactionService] Transfer COMPLETED: " + transaction);
            System.out.println("[TransactionService] From balance: " + fromWallet.getFormattedBalance());
            System.out.println("[TransactionService] To balance: " + toWallet.getFormattedBalance());

            // --- Notification (non-blocking) ---
            sendTransferNotification(fromWallet, toWallet, request.getAmount(), transaction);

            return transaction;

        } finally {
            // Always release locks
            lockService.release(lockKey2);
            lockService.release(lockKey1);
        }
    }

    // =========================================================================
    // USE CASE 2: DEPOSIT (Two-Phase)
    // =========================================================================

    /**
     * Phase 1 — Initiate Deposit.
     * Creates a PENDING transaction and initiates payment through the gateway.
     * @return providerRef for callback tracking
     */
    public String initiateDeposit(AddMoneyRequest request) {
        System.out.println("\n[TransactionService] Initiate Deposit: " + request);

        // --- Validation ---
        validateAmount(request.getAmount());
        Wallet wallet = walletService.getByAccountNumber(request.getAccountNumber());
        validateWalletActive(wallet, "Deposit target");

        // --- Create PENDING transaction ---
        Transaction transaction = new Transaction(
                -1, wallet.getId(), request.getAmount(),
                TransactionType.DEPOSIT, "Deposit via " + request.getPaymentGateway());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus(TransactionStatus.PENDING);

        // --- Initiate payment via gateway ---
        String providerRef = paymentGatewayService.initiatePayment(
                request.getAccountNumber(), request.getAmount(),
                request.getPaymentMethod(), request.getPaymentGateway(),
                request.getPaymentDetails());

        transaction.setPaymentGatewayId(providerRef);
        transactionRepository.save(transaction);

        System.out.println("[TransactionService] Deposit initiated: txnId=" + transaction.getTransactionId()
                + ", providerRef=" + providerRef);

        return providerRef;
    }

    /**
     * Phase 2 — Handle Payment Callback.
     * Idempotent: only processes PENDING → COMPLETED/FAILED transitions.
     */
    public void handleDepositCallback(String providerRef, TransactionStatus callbackStatus) {
        System.out.println("\n[TransactionService] Deposit Callback: ref=" + providerRef
                + ", status=" + callbackStatus);

        // --- Verify callback authenticity ---
        boolean isValid = paymentGatewayService.verifyCallback(providerRef, callbackStatus);
        if (!isValid) {
            System.out.println("[TransactionService] WARNING: Invalid callback rejected: " + providerRef);
            return;
        }

        // --- Find transaction by provider ref ---
        Transaction transaction = transactionRepository.findByPaymentGatewayId(providerRef)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found for ref: " + providerRef));

        // --- Idempotent check: only process if still PENDING ---
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            System.out.println("[TransactionService] Callback ignored (already processed): status="
                    + transaction.getStatus());
            return;
        }

        if (callbackStatus == TransactionStatus.COMPLETED) {
            // --- Credit wallet (with lock) ---
            String lockKey = "wallet_lock_" + transaction.getToWalletId();
            if (!lockService.acquire(lockKey, LOCK_TIMEOUT_MS)) {
                throw new RuntimeException("Failed to acquire lock for deposit credit");
            }
            try {
                Wallet wallet = walletRepository.findById(transaction.getToWalletId())
                        .orElseThrow(() -> new IllegalStateException("Wallet not found"));
                wallet.credit(transaction.getAmount());
                transaction.setStatus(TransactionStatus.COMPLETED);

                System.out.println("[TransactionService] Deposit COMPLETED: " + transaction.getFormattedAmount()
                        + " → " + wallet.getAccountNumber() + " (new balance: " + wallet.getFormattedBalance() + ")");

                // Notification
                notificationRouter.send("email", new NotificationMessage(
                        "user@example.com",
                        "Deposit Successful",
                        "Your deposit of " + transaction.getFormattedAmount() + " has been credited."));
            } finally {
                lockService.release(lockKey);
            }
        } else {
            // FAILED or TIMEOUT
            transaction.setStatus(TransactionStatus.FAILED);
            System.out.println("[TransactionService] Deposit FAILED: " + providerRef);
        }
    }

    // =========================================================================
    // USE CASE 3: WITHDRAW (Stub)
    // =========================================================================

    /**
     * Withdraw funds from a wallet.
     * In production, this would integrate with an external payout provider.
     * For demo: creates a PENDING transaction, then immediately marks COMPLETED.
     */
    public Transaction withdraw(String accountNumber, long amount, String description) {
        System.out.println("\n[TransactionService] Withdraw: account=" + accountNumber
                + ", amount=" + String.format("%.2f TUF", amount / 100.0));

        // --- Validation ---
        validateAmount(amount);
        Wallet wallet = walletService.getByAccountNumber(accountNumber);
        validateWalletActive(wallet, "Withdrawal source");

        // --- Acquire lock ---
        String lockKey = "wallet_lock_" + wallet.getId();
        if (!lockService.acquire(lockKey, LOCK_TIMEOUT_MS)) {
            throw new RuntimeException("Failed to acquire lock for withdrawal");
        }

        try {
            if (wallet.getBalance() < amount) {
                throw new IllegalArgumentException("Insufficient funds for withdrawal: balance="
                        + wallet.getFormattedBalance());
            }

            // --- Create transaction ---
            Transaction transaction = new Transaction(
                    wallet.getId(), -1, amount,
                    TransactionType.WITHDRAWAL, description);

            // In production: external payout would happen here
            // On success: debit wallet and mark COMPLETED
            wallet.debit(amount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);

            System.out.println("[TransactionService] Withdrawal COMPLETED: " + transaction);
            System.out.println("[TransactionService] New balance: " + wallet.getFormattedBalance());

            // Notification
            notificationRouter.send("email", new NotificationMessage(
                    "user@example.com",
                    "Withdrawal Processed",
                    "Your withdrawal of " + transaction.getFormattedAmount() + " has been processed."));

            return transaction;

        } finally {
            lockService.release(lockKey);
        }
    }

    // =========================================================================
    // USE CASE 4: ACCOUNT STATEMENT
    // =========================================================================

    /**
     * Generate an account statement for a wallet within a time range.
     * @return AccountStatement DTO (not persisted)
     */
    public AccountStatement getStatement(String accountNumber, LocalDateTime start, LocalDateTime end) {
        System.out.println("\n[TransactionService] Statement: account=" + accountNumber
                + ", from=" + start + ", to=" + end);

        Wallet wallet = walletService.getByAccountNumber(accountNumber);

        // Default range: last 30 days to now
        if (start == null) start = LocalDateTime.now().minusDays(30);
        if (end == null) end = LocalDateTime.now();

        List<Transaction> transactions = transactionRepository.findByWalletAndRange(
                wallet.getId(), start, end);

        AccountStatement statement = new AccountStatement(
                wallet.getId(), wallet.getAccountNumber(),
                transactions, start, end, wallet.getBalance());

        System.out.println("[TransactionService] Statement generated: " + transactions.size() + " transactions");
        return statement;
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private void validateAmount(long amount) {
        if (amount < MIN_AMOUNT) {
            throw new IllegalArgumentException("Amount must be at least " + MIN_AMOUNT
                    + " minor unit(s). Got: " + amount);
        }
    }

    private void validateWalletActive(Wallet wallet, String label) {
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException(label + " wallet is not ACTIVE: "
                    + wallet.getAccountNumber() + " (status=" + wallet.getStatus() + ")");
        }
    }

    private void sendTransferNotification(Wallet from, Wallet to, long amount, Transaction txn) {
        String formattedAmount = String.format("%.2f TUF", amount / 100.0);
        notificationRouter.send("email", new NotificationMessage(
                "sender@example.com",
                "Transfer Sent",
                "You transferred " + formattedAmount + " to " + to.getAccountNumber()
                        + ". Txn: " + txn.getTransactionId()));
        notificationRouter.send("email", new NotificationMessage(
                "receiver@example.com",
                "Transfer Received",
                "You received " + formattedAmount + " from " + from.getAccountNumber()
                        + ". Txn: " + txn.getTransactionId()));
    }
}
