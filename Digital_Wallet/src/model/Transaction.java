package model;

import model.enums.TransactionStatus;
import model.enums.TransactionType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity — audit trail for all wallet operations.
 * fromWalletId is NULL (represented as -1) for DEPOSIT.
 * toWalletId is NULL (represented as -1) for WITHDRAWAL.
 * Amount is stored in minor units (long).
 */
public class Transaction {

    private static int idCounter = 0;

    private int id;
    private String transactionId;
    private int fromWalletId;   // -1 means NULL (DEPOSIT)
    private int toWalletId;     // -1 means NULL (WITHDRAWAL)
    private long amount;        // stored in minor units
    private TransactionType transactionType;
    private TransactionStatus status;
    private String paymentGatewayId;  // nullable
    private String paymentMethod;     // nullable
    private String description;
    private LocalDateTime timestamp;

    public Transaction(int fromWalletId, int toWalletId, long amount,
                       TransactionType transactionType, String description) {
        this.id = ++idCounter;
        this.transactionId = UUID.randomUUID().toString();
        this.fromWalletId = fromWalletId;
        this.toWalletId = toWalletId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = TransactionStatus.PENDING;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public int getFromWalletId() { return fromWalletId; }
    public int getToWalletId() { return toWalletId; }
    public long getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public TransactionStatus getStatus() { return status; }
    public String getPaymentGatewayId() { return paymentGatewayId; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setStatus(TransactionStatus status) { this.status = status; }
    public void setPaymentGatewayId(String paymentGatewayId) { this.paymentGatewayId = paymentGatewayId; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getFormattedAmount() {
        return String.format("%.2f TUF", amount / 100.0);
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", txnId='" + transactionId + "'"
                + ", type=" + transactionType + ", status=" + status
                + ", amount=" + getFormattedAmount()
                + ", from=" + (fromWalletId == -1 ? "EXTERNAL" : fromWalletId)
                + ", to=" + (toWalletId == -1 ? "EXTERNAL" : toWalletId)
                + ", desc='" + description + "'"
                + ", time=" + timestamp + "}";
    }
}
