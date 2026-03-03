package model;

import model.enums.WalletStatus;
import java.time.LocalDateTime;

/**
 * Wallet entity — core domain object.
 * Balance is stored in minor units (long): e.g., 100.50 TUF = 10050.
 * One-to-One with User (one user has one wallet).
 */
public class Wallet {

    private static int idCounter = 0;

    private int id;
    private String accountNumber;
    private long balance; // stored in minor units (amount * 100)
    private int userId;
    private WalletStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wallet(String accountNumber, int userId) {
        this.id = ++idCounter;
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.userId = userId;
        this.status = WalletStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public long getBalance() { return balance; }
    public int getUserId() { return userId; }
    public WalletStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setStatus(WalletStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Thread-safe credit operation.
     * @param amount amount in minor units (must be positive)
     */
    public synchronized void credit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Credit amount must be positive");
        this.balance += amount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Thread-safe debit operation.
     * @param amount amount in minor units (must be positive)
     * @throws IllegalArgumentException if insufficient funds
     */
    public synchronized void debit(long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Debit amount must be positive");
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient funds: balance=" + balance + ", amount=" + amount);
        this.balance -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the balance formatted as a readable TUF string.
     */
    public String getFormattedBalance() {
        return String.format("%.2f TUF", balance / 100.0);
    }

    @Override
    public String toString() {
        return "Wallet{id=" + id + ", accountNumber='" + accountNumber + "', balance=" + getFormattedBalance()
                + ", userId=" + userId + ", status=" + status + "}";
    }
}
