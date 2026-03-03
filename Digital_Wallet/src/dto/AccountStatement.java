package dto;

import model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for account statements.
 * This is a generated response, not a stored entity.
 */
public class AccountStatement {

    private int walletId;
    private String walletAccountNumber;
    private List<Transaction> transactions;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private long currentBalance; // in minor units

    public AccountStatement(int walletId, String walletAccountNumber,
                            List<Transaction> transactions,
                            LocalDateTime startDate, LocalDateTime endDate,
                            long currentBalance) {
        this.walletId = walletId;
        this.walletAccountNumber = walletAccountNumber;
        this.transactions = transactions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentBalance = currentBalance;
    }

    // Getters
    public int getWalletId() { return walletId; }
    public String getWalletAccountNumber() { return walletAccountNumber; }
    public List<Transaction> getTransactions() { return transactions; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public long getCurrentBalance() { return currentBalance; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccountStatement{\n");
        sb.append("  walletId=").append(walletId).append(", account='").append(walletAccountNumber).append("'\n");
        sb.append("  currentBalance=").append(String.format("%.2f TUF", currentBalance / 100.0)).append("\n");
        if (startDate != null) sb.append("  from=").append(startDate).append("\n");
        if (endDate != null) sb.append("  to=").append(endDate).append("\n");
        sb.append("  transactions (").append(transactions.size()).append("):\n");
        for (Transaction t : transactions) {
            sb.append("    ").append(t).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
