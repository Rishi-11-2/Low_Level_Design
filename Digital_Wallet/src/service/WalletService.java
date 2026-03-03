package service;

import model.Wallet;
import model.enums.WalletStatus;
import repository.UserRepository;
import repository.WalletRepository;
import java.util.UUID;

/**
 * WalletService — manages wallet lifecycle.
 * Enforces one-wallet-per-user constraint.
 */
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new wallet for a user.
     * Enforces: one wallet per user, user must exist.
     * @throws IllegalArgumentException if user not found or already has a wallet
     */
    public Wallet createWallet(int userId) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // Enforce one-wallet-per-user
        if (walletRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("User already has a wallet: userId=" + userId);
        }

        // Generate unique account number
        String accountNumber = "TUF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Wallet wallet = new Wallet(accountNumber, userId);
        walletRepository.save(wallet);

        System.out.println("[WalletService] Created wallet: " + wallet);
        return wallet;
    }

    /**
     * Get wallet by account number.
     * @throws IllegalArgumentException if wallet not found
     */
    public Wallet getByAccountNumber(String accountNumber) {
        return walletRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found: " + accountNumber));
    }

    /**
     * Check if a wallet is active.
     */
    public boolean isActive(String accountNumber) {
        Wallet wallet = getByAccountNumber(accountNumber);
        return wallet.getStatus() == WalletStatus.ACTIVE;
    }

    /**
     * Suspend a wallet (admin action).
     * Blocks transfers, deposits, and withdrawals. Allows statements.
     */
    public void suspendWallet(String accountNumber) {
        Wallet wallet = getByAccountNumber(accountNumber);
        if (wallet.getStatus() == WalletStatus.CLOSED) {
            throw new IllegalStateException("Cannot suspend a closed wallet: " + accountNumber);
        }
        wallet.setStatus(WalletStatus.SUSPENDED);
        System.out.println("[WalletService] Wallet SUSPENDED: " + accountNumber);
    }

    /**
     * Close a wallet permanently (admin action).
     */
    public void closeWallet(String accountNumber) {
        Wallet wallet = getByAccountNumber(accountNumber);
        if (wallet.getBalance() > 0) {
            System.out.println("[WalletService] WARNING: Closing wallet with non-zero balance: "
                    + wallet.getFormattedBalance());
        }
        wallet.setStatus(WalletStatus.CLOSED);
        System.out.println("[WalletService] Wallet CLOSED: " + accountNumber);
    }

    /**
     * Reopen a suspended wallet (admin action).
     */
    public void reopenWallet(String accountNumber) {
        Wallet wallet = getByAccountNumber(accountNumber);
        if (wallet.getStatus() != WalletStatus.SUSPENDED) {
            throw new IllegalStateException("Only SUSPENDED wallets can be reopened. Current status: "
                    + wallet.getStatus());
        }
        wallet.setStatus(WalletStatus.ACTIVE);
        System.out.println("[WalletService] Wallet REOPENED: " + accountNumber);
    }
}
