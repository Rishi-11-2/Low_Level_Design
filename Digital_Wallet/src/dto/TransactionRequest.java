package dto;

/**
 * Request DTO for wallet-to-wallet fund transfers.
 */
public class TransactionRequest {

    private String fromAccountNumber;
    private String toAccountNumber;
    private long amount; // in minor units (actual amount * 100)
    private String description;

    public TransactionRequest(String fromAccountNumber, String toAccountNumber,
                              long amount, String description) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.description = description;
    }

    public String getFromAccountNumber() { return fromAccountNumber; }
    public String getToAccountNumber() { return toAccountNumber; }
    public long getAmount() { return amount; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "TransactionRequest{from='" + fromAccountNumber + "', to='" + toAccountNumber
                + "', amount=" + String.format("%.2f TUF", amount / 100.0) + ", desc='" + description + "'}";
    }
}
