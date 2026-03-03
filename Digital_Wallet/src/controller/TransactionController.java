package controller;

import dto.AddMoneyRequest;
import dto.TransactionRequest;
import model.Transaction;
import model.enums.TransactionStatus;
import service.TransactionService;

/**
 * TransactionController — REST-style controller for transaction operations.
 * Endpoints:
 *   POST /api/transactions/transfer   → transfer(request)
 *   POST /api/wallets/{acct}/deposit  → initiateDeposit(request)
 *   POST /api/payments/callback       → handlePaymentCallback(providerRef, status)
 *   POST /api/wallets/{acct}/withdraw → withdraw(accountNumber, amount, description)
 */
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/transactions/transfer
     * Transfer funds between wallets.
     */
    public Transaction transfer(TransactionRequest request) {
        System.out.println("\n>>> POST /api/transactions/transfer");
        try {
            Transaction txn = transactionService.transfer(request);
            System.out.println("<<< 200 OK: " + txn);
            return txn;
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
            return null;
        }
    }

    /**
     * POST /api/wallets/{accountNumber}/deposit
     * Phase 1: Initiate deposit and get provider reference.
     */
    public String initiateDeposit(AddMoneyRequest request) {
        System.out.println("\n>>> POST /api/wallets/" + request.getAccountNumber() + "/deposit");
        try {
            String providerRef = transactionService.initiateDeposit(request);
            System.out.println("<<< 200 OK: providerRef=" + providerRef);
            return providerRef;
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
            return null;
        }
    }

    /**
     * POST /api/payments/callback
     * Phase 2: Handle payment gateway callback.
     */
    public void handlePaymentCallback(String providerRef, TransactionStatus status) {
        System.out.println("\n>>> POST /api/payments/callback (ref=" + providerRef + ", status=" + status + ")");
        try {
            transactionService.handleDepositCallback(providerRef, status);
            System.out.println("<<< 200 OK: Callback processed");
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
        }
    }

    /**
     * POST /api/wallets/{accountNumber}/withdraw
     * Withdraw funds from wallet.
     */
    public Transaction withdraw(String accountNumber, long amount, String description) {
        System.out.println("\n>>> POST /api/wallets/" + accountNumber + "/withdraw");
        try {
            Transaction txn = transactionService.withdraw(accountNumber, amount, description);
            System.out.println("<<< 200 OK: " + txn);
            return txn;
        } catch (Exception e) {
            System.out.println("<<< 400 Bad Request: " + e.getMessage());
            return null;
        }
    }
}
