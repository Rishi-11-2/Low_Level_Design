package model;

public class ExitResult {
    private final boolean success;
    private final Receipt receipt;
    private final String message;

    public ExitResult(boolean success, Receipt receipt, String message) {
        this.success = success;
        this.receipt = receipt;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public String getMessage() {
        return message;
    }
}
