package service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed lock simulation using ConcurrentHashMap.
 * In production, this would use Redis, ZooKeeper, or similar.
 *
 * Lock Key Format: "wallet_lock_{walletId}"
 * Lock Timeout: configurable (default 5 seconds)
 * Lock Behavior: Waits (retries) up to timeout; if timeout expires, returns false.
 * Lock Ordering: Callers acquire locks in sorted order (by wallet ID) to prevent deadlocks.
 */
public class LockService {

    private final Set<String> locks = ConcurrentHashMap.newKeySet();

    /**
     * Acquire a distributed lock on the given key.
     * Blocks (retries) until lock is available or timeout expires.
     * @param key lock key (e.g., "wallet_lock_1")
     * @param timeoutMs maximum time to wait for the lock in milliseconds
     * @return true if lock acquired, false if timeout
     */
    public boolean acquire(String key, long timeoutMs) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (locks.add(key)) {
                System.out.println("[Lock] Acquired lock: " + key);
                return true;
            }
            try {
                Thread.sleep(10); // Retry interval
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        System.out.println("[Lock] Failed to acquire lock: " + key + " (timeout: " + timeoutMs + "ms)");
        return false;
    }

    /**
     * Release a previously acquired lock.
     * Always call this in a finally block to prevent lock leaks.
     */
    public void release(String key) {
        locks.remove(key);
        System.out.println("[Lock] Released lock: " + key);
    }
}
