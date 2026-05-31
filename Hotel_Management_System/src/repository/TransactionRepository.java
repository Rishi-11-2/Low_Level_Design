package repository;

import model.Transaction;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionRepository {
    private final Map<String, Transaction> transactionMap = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        transactionMap.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactionMap.get(id));
    }

    public Optional<Transaction> findByProviderRef(String providerRef) {
        return transactionMap.values().stream()
                .filter(t -> t.getProviderRef().equals(providerRef))
                .findFirst();
    }
}
