package repository;

import model.Wallet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory Wallet repository.
 * Supports lookup by account number and user ID.
 */
public class WalletRepository {

    private final Map<Integer, Wallet> walletsById = new ConcurrentHashMap<>();
    private final Map<String, Wallet> walletsByAccountNumber = new ConcurrentHashMap<>();
    private final Map<Integer, Wallet> walletsByUserId = new ConcurrentHashMap<>();

    public Wallet save(Wallet wallet) {
        walletsById.put(wallet.getId(), wallet);
        walletsByAccountNumber.put(wallet.getAccountNumber(), wallet);
        walletsByUserId.put(wallet.getUserId(), wallet);
        return wallet;
    }

    public Optional<Wallet> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(walletsByAccountNumber.get(accountNumber));
    }

    public Optional<Wallet> findByUserId(int userId) {
        return Optional.ofNullable(walletsByUserId.get(userId));
    }

    public Optional<Wallet> findById(int walletId) {
        return Optional.ofNullable(walletsById.get(walletId));
    }

    /**
     * Atomically update balance by delta (positive for credit, negative for debit).
     * The actual mutation happens on the Wallet object's synchronized methods.
     */
    public void updateBalance(int walletId, long deltaMinor) {
        Wallet wallet = walletsById.get(walletId);
        if (wallet == null) throw new IllegalArgumentException("Wallet not found: " + walletId);
        if (deltaMinor > 0) {
            wallet.credit(deltaMinor);
        } else if (deltaMinor < 0) {
            wallet.debit(-deltaMinor);
        }
    }
}
