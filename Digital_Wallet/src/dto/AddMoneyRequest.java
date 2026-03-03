package dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Request DTO for adding money (deposit) to a wallet.
 */
public class AddMoneyRequest {

    private String accountNumber;
    private long amount; // in minor units (actual amount * 100)
    private String paymentMethod;
    private String paymentGateway;
    private Map<String, String> paymentDetails;

    public AddMoneyRequest(String accountNumber, long amount, String paymentMethod,
                           String paymentGateway, Map<String, String> paymentDetails) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentGateway = paymentGateway;
        this.paymentDetails = paymentDetails != null ? paymentDetails : new HashMap<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public long getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentGateway() { return paymentGateway; }
    public Map<String, String> getPaymentDetails() { return paymentDetails; }

    @Override
    public String toString() {
        return "AddMoneyRequest{account='" + accountNumber + "', amount="
                + String.format("%.2f TUF", amount / 100.0)
                + ", method='" + paymentMethod + "', gateway='" + paymentGateway + "'}";
    }
}
