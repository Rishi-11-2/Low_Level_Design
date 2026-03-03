package controller;

import dto.AccountStatement;
import model.Wallet;
import service.TransactionService;
import service.WalletService;
import java.time.LocalDateTime;

/**
 * WalletController — REST-style controller for wallet operations.
 * Endpoints:
 *   POST /api/wallets           → createWallet(userId)
 *   GET  /api/wallets/{acct}/balance  → getBalance(accountNumber)
 *   GET  /api/wallets/{acct}/statement → getStatement(accountNumber, start, end)
 */
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    /**
     * POST /api/wallets
     * Create a new wallet for the given user.
     */
    public Wallet createWallet(int userId) {
        System.out.println("\n>>> POST /api/wallets (userId=" + userId + ")");
        try {
            Wallet wallet = walletService.createWallet(userId);
            System.out.println("<<< 201 Created: " + wallet);
            return wallet;
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
            return null;
        }
    }

    /**
     * GET /api/wallets/{accountNumber}/balance
     * Get the current balance.
     */
    public long getBalance(String accountNumber) {
        System.out.println("\n>>> GET /api/wallets/" + accountNumber + "/balance");
        try {
            Wallet wallet = walletService.getByAccountNumber(accountNumber);
            System.out.println("<<< 200 OK: " + wallet.getFormattedBalance());
            return wallet.getBalance();
        } catch (Exception e) {
            System.out.println("<<< 404 Not Found: " + e.getMessage());
            return -1;
        }
    }

    /**
     * GET /api/wallets/{accountNumber}/statement?start=...&end=...
     * Get account statement for a time range.
     */
    public AccountStatement getStatement(String accountNumber, LocalDateTime start, LocalDateTime end) {
        System.out.println("\n>>> GET /api/wallets/" + accountNumber + "/statement");
        try {
            AccountStatement statement = transactionService.getStatement(accountNumber, start, end);
            System.out.println("<<< 200 OK:\n" + statement);
            return statement;
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
            return null;
        }
    }
}
