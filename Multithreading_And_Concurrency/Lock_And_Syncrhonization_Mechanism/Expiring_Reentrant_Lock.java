import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

// ───────────────────────── ExpiringReentrantLock ───────────────────────── 

// Lock with a built-in “auto-release after N ms” timer
class ExpiringReentrantLock {
    // underlying mutual-exclusion lock
    private final ReentrantLock lock = new ReentrantLock();

    // single-thread scheduler to run the expiry task
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    // flag that tells the expiry task a timed lock is still active
    private volatile boolean isLocked = false;

    // Tries to acquire immediately; if successful, schedules auto-unlock
    public boolean tryLockWithExpiry(long timeoutMillis) {

        // attempt immediate acquisition
        boolean acquired = lock.tryLock();
        if (acquired) {
            // mark as held under the timer
            isLocked = true;

            // schedule unlock after timeout
            scheduler.schedule(() -> {
                if (lock.isHeldByCurrentThread() || isLocked) {
                    System.out.println("Auto-releasing lock after timeout.");
                    unlockSafely(); // delegate to common unlock logic
                }
            }, timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return acquired;
    }

    // Releases the lock either by the owner thread or the timer
    public void unlockSafely() {
        if (lock.isHeldByCurrentThread() || isLocked) {
            isLocked = false; // reset timer flag

            // only the owner may actually call unlock()
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released.");
            }
        }
    }

    // Graceful shutdown for the scheduler
    public void shutdown() {
        scheduler.shutdownNow();
    }
}

// ───────────────────────────── Driver code ──────────────────────────────

public class Expiring_Reentrant_Lock {
    public static void main(String[] args) {
        // shared expiring lock
        ExpiringReentrantLock expLock = new ExpiringReentrantLock();

        /* Idle user grabs the lock, then “goes missing” for 5 s
           (expiry timer is 3 s) */
        Thread idleUser = new Thread(() -> {
            if (expLock.tryLockWithExpiry(3000)) {
                try { Thread.sleep(5000); } // simulate long idle
                catch (InterruptedException ignored) {}
                expLock.unlockSafely(); // in case timer fired
            }
        }, "IdleUser");

        /* Active user starts after 1 s and keeps retrying every 1000 ms
           until it finally books the ticket once the timer frees the lock */
        Thread activeUser = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            while (true) {
                if (expLock.tryLockWithExpiry(3000)) {
                    System.out.println("Active user booked!");
                    expLock.unlockSafely();
                    break;
                } else {
                    System.out.println("Active user still waiting...");
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            }
        }, "ActiveUser");

        // start threads
        idleUser.start();
        activeUser.start();

        // wait for both to finish
        try {
            idleUser.join();
            activeUser.join();
        } catch (InterruptedException ignored) {}

        // shut down scheduler
        expLock.shutdown();
    }
}
