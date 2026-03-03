import controller.AdminController;
import controller.TransactionController;
import controller.WalletController;
import dto.AddMoneyRequest;
import dto.TransactionRequest;
import model.Transaction;
import model.User;
import model.Wallet;
import model.enums.TransactionStatus;
import repository.TransactionRepository;
import repository.UserRepository;
import repository.WalletRepository;
import service.*;
import strategy.notification.EmailNotificationChannel;
import strategy.notification.NotificationRouter;
import strategy.notification.SmsNotificationChannel;
import strategy.payment.PaymentGatewayRouter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Main demo class showcasing the complete Digital Wallet System.
 *
 * Demonstrates:
 *   1. Wallet Creation (one wallet per user)
 *   2. Deposit — Two-Phase (Initiate + Callback via PaymentGateway)
 *   3. Transfer — Locked, atomic balance movement with audit trail
 *   4. Withdraw — Stub illustrating payout workflow
 *   5. Account Statement — Time-windowed transaction history
 *   6. Admin Operations — Suspend, Reopen, Close
 *   7. Edge Cases — Self-transfer, insufficient funds, closed wallet
 *
 * Architecture: Controller → Service → Repository (CSR)
 * Patterns: Strategy (Payment Gateway, Notifications), Repository, Distributed Locking
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("        DIGITAL WALLET SYSTEM - Low Level Design Demo");
        System.out.println("=".repeat(70));

        // ====================================================================
        // SETUP: Wire all dependencies (Manual DI simulation)
        // ====================================================================
        System.out.println("\n--- Wiring Dependencies ---");

        // Repositories
        UserRepository userRepository = new UserRepository();
        WalletRepository walletRepository = new WalletRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Infrastructure Services
        LockService lockService = new LockService();

        // Notification Strategy
        NotificationRouter notificationRouter = new NotificationRouter();
        notificationRouter.register("email", new EmailNotificationChannel());
        notificationRouter.register("sms", new SmsNotificationChannel());

        // Payment Gateway Strategy
        PaymentGatewayRouter paymentGatewayRouter = new PaymentGatewayRouter();
        PaymentGatewayService paymentGatewayService = new PaymentGatewayService(paymentGatewayRouter);

        // Domain Services
        WalletService walletService = new WalletService(walletRepository, userRepository);
        TransactionService transactionService = new TransactionService(
                walletRepository, transactionRepository, walletService,
                paymentGatewayService, lockService, notificationRouter);

        // Controllers
        WalletController walletController = new WalletController(walletService, transactionService);
        TransactionController transactionController = new TransactionController(transactionService);
        AdminController adminController = new AdminController(walletService);

        // ====================================================================
        // SEED DATA: Register Users
        // ====================================================================
        System.out.println("\n--- Seeding Users ---");
        User user1 = new User(1, "john_doe", "john@example.com", "John Doe");
        User user2 = new User(2, "jane_smith", "jane@example.com", "Jane Smith");
        User user3 = new User(3, "bob_jones", "bob@example.com", "Bob Jones");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        System.out.println("Registered: " + user1);
        System.out.println("Registered: " + user2);
        System.out.println("Registered: " + user3);

        // ====================================================================
        // FLOW 1: WALLET CREATION
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 1: Wallet Creation");
        System.out.println("=".repeat(70));

        Wallet wallet1 = walletController.createWallet(1);
        Wallet wallet2 = walletController.createWallet(2);
        Wallet wallet3 = walletController.createWallet(3);

        // Edge Case: Duplicate wallet for same user
        System.out.println("\n--- Edge Case: Duplicate Wallet ---");
        walletController.createWallet(1); // Should fail

        String acct1 = wallet1.getAccountNumber();
        String acct2 = wallet2.getAccountNumber();
        String acct3 = wallet3.getAccountNumber();

        // ====================================================================
        // FLOW 2: DEPOSIT (Two-Phase)
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 2: Deposit (Two-Phase via Payment Gateway)");
        System.out.println("=".repeat(70));

        // Phase 1: Initiate deposit for wallet1 — 500.00 TUF via Stripe
        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("card_number", "4242424242424242");
        paymentDetails.put("expiry", "12/28");

        String providerRef1 = transactionController.initiateDeposit(
                new AddMoneyRequest(acct1, 50000, "credit_card", "stripe", paymentDetails));

        // Phase 2: Payment callback — SUCCESS
        transactionController.handlePaymentCallback(providerRef1, TransactionStatus.COMPLETED);

        // Deposit to wallet2 — 300.00 TUF via Razorpay
        String providerRef2 = transactionController.initiateDeposit(
                new AddMoneyRequest(acct2, 30000, "upi", "razorpay", new HashMap<>()));
        transactionController.handlePaymentCallback(providerRef2, TransactionStatus.COMPLETED);

        // Deposit to wallet3 — 100.00 TUF via Mock
        String providerRef3 = transactionController.initiateDeposit(
                new AddMoneyRequest(acct3, 10000, "netbanking", "mock", null));
        transactionController.handlePaymentCallback(providerRef3, TransactionStatus.COMPLETED);

        // Check balances
        System.out.println("\n--- Balances After Deposits ---");
        walletController.getBalance(acct1);
        walletController.getBalance(acct2);
        walletController.getBalance(acct3);

        // Edge Case: Failed payment callback
        System.out.println("\n--- Edge Case: Failed Deposit ---");
        String providerRef4 = transactionController.initiateDeposit(
                new AddMoneyRequest(acct1, 20000, "credit_card", "stripe", paymentDetails));
        transactionController.handlePaymentCallback(providerRef4, TransactionStatus.FAILED);
        walletController.getBalance(acct1); // Should still be 500.00

        // Edge Case: Duplicate callback (idempotent)
        System.out.println("\n--- Edge Case: Duplicate Callback (Idempotent) ---");
        transactionController.handlePaymentCallback(providerRef1, TransactionStatus.COMPLETED);

        // ====================================================================
        // FLOW 3: TRANSFER
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 3: Transfer (Locked, Atomic)");
        System.out.println("=".repeat(70));

        // Transfer 150.00 TUF from wallet1 to wallet2
        transactionController.transfer(new TransactionRequest(
                acct1, acct2, 15000, "Payment for services"));

        // Transfer 50.00 TUF from wallet2 to wallet3
        transactionController.transfer(new TransactionRequest(
                acct2, acct3, 5000, "Gift to Bob"));

        System.out.println("\n--- Balances After Transfers ---");
        walletController.getBalance(acct1); // 500.00 - 150.00 = 350.00
        walletController.getBalance(acct2); // 300.00 + 150.00 - 50.00 = 400.00
        walletController.getBalance(acct3); // 100.00 + 50.00 = 150.00

        // Edge Case: Self-transfer
        System.out.println("\n--- Edge Case: Self-Transfer ---");
        transactionController.transfer(new TransactionRequest(
                acct1, acct1, 1000, "Self transfer"));

        // Edge Case: Insufficient funds
        System.out.println("\n--- Edge Case: Insufficient Funds ---");
        transactionController.transfer(new TransactionRequest(
                acct3, acct1, 99999, "Too much"));

        // Edge Case: Minimum amount violation
        System.out.println("\n--- Edge Case: Minimum Amount (0 TUF) ---");
        transactionController.transfer(new TransactionRequest(
                acct1, acct2, 0, "Zero amount"));

        // ====================================================================
        // FLOW 4: WITHDRAW (Stub)
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 4: Withdrawal (Stub)");
        System.out.println("=".repeat(70));

        transactionController.withdraw(acct1, 10000, "ATM withdrawal");

        System.out.println("\n--- Balance After Withdrawal ---");
        walletController.getBalance(acct1); // 350.00 - 100.00 = 250.00

        // Edge Case: Withdraw more than balance
        System.out.println("\n--- Edge Case: Insufficient Funds for Withdrawal ---");
        transactionController.withdraw(acct3, 99999, "Too much withdrawal");

        // ====================================================================
        // FLOW 5: ACCOUNT STATEMENT
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 5: Account Statement");
        System.out.println("=".repeat(70));

        walletController.getStatement(acct1,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusMinutes(1));

        walletController.getStatement(acct2,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusMinutes(1));

        // ====================================================================
        // FLOW 6: ADMIN OPERATIONS
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 6: Admin Operations (Suspend / Reopen / Close)");
        System.out.println("=".repeat(70));

        // Suspend wallet3
        adminController.suspendWallet(acct3);

        // Edge Case: Transfer to suspended wallet
        System.out.println("\n--- Edge Case: Transfer to Suspended Wallet ---");
        transactionController.transfer(new TransactionRequest(
                acct1, acct3, 1000, "To suspended wallet"));

        // Edge Case: Deposit to suspended wallet
        System.out.println("\n--- Edge Case: Deposit to Suspended Wallet ---");
        transactionController.initiateDeposit(
                new AddMoneyRequest(acct3, 5000, "upi", "mock", null));

        // Statement still allowed on suspended wallet
        System.out.println("\n--- Statement on Suspended Wallet (allowed) ---");
        walletController.getStatement(acct3,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusMinutes(1));

        // Reopen wallet3
        adminController.reopenWallet(acct3);

        // Transfer should work again after reopen
        System.out.println("\n--- Transfer After Reopen ---");
        transactionController.transfer(new TransactionRequest(
                acct1, acct3, 1000, "Post-reopen transfer"));

        // Close wallet3
        adminController.closeWallet(acct3);

        // Edge Case: Reopen a closed wallet
        System.out.println("\n--- Edge Case: Reopen Closed Wallet ---");
        adminController.reopenWallet(acct3);

        // Edge Case: Transfer from closed wallet
        System.out.println("\n--- Edge Case: Transfer from Closed Wallet ---");
        transactionController.transfer(new TransactionRequest(
                acct3, acct1, 500, "From closed wallet"));

        // ====================================================================
        // FINAL BALANCES
        // ====================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FINAL BALANCES");
        System.out.println("=".repeat(70));
        walletController.getBalance(acct1);
        walletController.getBalance(acct2);
        walletController.getBalance(acct3);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("  DEMO COMPLETE");
        System.out.println("=".repeat(70));
    }
}
