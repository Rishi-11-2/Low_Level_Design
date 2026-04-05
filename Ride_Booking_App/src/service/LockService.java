package service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed lock simulation using ConcurrentHashMap.
 * In production, this would use Redis or ZooKeeper.
 */
public class LockService {

    private final Set<String> locks = ConcurrentHashMap.newKeySet();

    public boolean acquire(String key, long timeoutMs) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (locks.add(key)) {
                System.out.println("[Lock] Acquired lock: " + key);
                return true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        System.out.println("[Lock] Failed to acquire lock: " + key + " (timeout: " + timeoutMs + "ms)");
        return false;
    }

    public void release(String key) {
        locks.remove(key);
        System.out.println("[Lock] Released lock: " + key);
    }
}
