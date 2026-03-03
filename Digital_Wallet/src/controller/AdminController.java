package controller;

import service.WalletService;

/**
 * AdminController — REST-style controller for admin operations.
 * Endpoints:
 *   POST /api/admin/wallets/{acct}/suspend → suspendWallet(accountNumber)
 *   POST /api/admin/wallets/{acct}/close   → closeWallet(accountNumber)
 *   POST /api/admin/wallets/{acct}/reopen  → reopenWallet(accountNumber)
 */
public class AdminController {

    private final WalletService walletService;

    public AdminController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * POST /api/admin/wallets/{accountNumber}/suspend
     */
    public void suspendWallet(String accountNumber) {
        System.out.println("\n>>> POST /api/admin/wallets/" + accountNumber + "/suspend");
        try {
            walletService.suspendWallet(accountNumber);
            System.out.println("<<< 200 OK: Wallet suspended");
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/wallets/{accountNumber}/close
     */
    public void closeWallet(String accountNumber) {
        System.out.println("\n>>> POST /api/admin/wallets/" + accountNumber + "/close");
        try {
            walletService.closeWallet(accountNumber);
            System.out.println("<<< 200 OK: Wallet closed");
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/wallets/{accountNumber}/reopen
     */
    public void reopenWallet(String accountNumber) {
        System.out.println("\n>>> POST /api/admin/wallets/" + accountNumber + "/reopen");
        try {
            walletService.reopenWallet(accountNumber);
            System.out.println("<<< 200 OK: Wallet reopened");
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
        }
    }
}
