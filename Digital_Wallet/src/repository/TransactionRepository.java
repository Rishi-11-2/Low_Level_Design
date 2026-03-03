package repository;

import model.Transaction;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory Transaction repository.
 * Supports lookup by transaction ID, payment gateway ID, and wallet + time range.
 */
public class TransactionRepository {

    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Transaction> byTransactionId = new ConcurrentHashMap<>();
    private final Map<String, Transaction> byPaymentGatewayId = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        transactions.add(transaction);
        byTransactionId.put(transaction.getTransactionId(), transaction);
        if (transaction.getPaymentGatewayId() != null) {
            byPaymentGatewayId.put(transaction.getPaymentGatewayId(), transaction);
        }
        return transaction;
    }

    public Optional<Transaction> findByTransactionId(String transactionId) {
        return Optional.ofNullable(byTransactionId.get(transactionId));
    }

    public Optional<Transaction> findByPaymentGatewayId(String paymentGatewayId) {
        return Optional.ofNullable(byPaymentGatewayId.get(paymentGatewayId));
    }

    /**
     * Find all transactions involving a wallet within a time range.
     * A transaction involves a wallet if it's either sender or receiver.
     */
    public List<Transaction> findByWalletAndRange(int walletId, LocalDateTime start, LocalDateTime end) {
        return transactions.stream()
                .filter(t -> t.getFromWalletId() == walletId || t.getToWalletId() == walletId)
                .filter(t -> !t.getTimestamp().isBefore(start) && !t.getTimestamp().isAfter(end))
                .collect(Collectors.toList());
    }
}
