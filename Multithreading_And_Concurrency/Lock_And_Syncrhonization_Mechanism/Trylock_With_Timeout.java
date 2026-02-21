import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// Booking system that waits up to 2 s for a lock
class TicketBookingTryLock {

    // shared resource: initial seat count
    private int availableSeats = 1;

    // exclusive lock protecting seat updates
    private final ReentrantLock lock = new ReentrantLock();

    // attempts to book a ticket for the given user
    public void bookTicket(String user) {
        System.out.println(user + " is trying to book...");

        // remember whether we actually got the lock
        boolean lockAcquired = false;
        try {
            // wait up to 2 s before giving up
            lockAcquired = lock.tryLock(2, TimeUnit.SECONDS);

            if (lockAcquired) {
                System.out.println(user + " acquired lock.");

                // critical section – safe to inspect/update seats
                if (availableSeats > 0) {
                    System.out.println(user + " successfully booked the ticket.");
                    availableSeats--;
                } else {
                    System.out.println(user + " could not book the ticket. No seats left.");
                }

                // simulate a long operation that holds the lock
                // this helps demonstrate the timeout behavior for the next user
                Thread.sleep(3000); 
            } else {
                System.out.println(user + " could not acquire lock. Try again later.");
            }
        } 
        catch (InterruptedException e) {
            // restore interrupt status and log
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } 
        finally {
            // release only if we were the owner
            if (lockAcquired) {
                System.out.println(user + " is releasing the lock.");
                lock.unlock();
            }
        }
    }
}


public class Trylock_With_Timeout{
    public static void main(String[] args) {

        // one shared booking system
        TicketBookingTryLock booking = new TicketBookingTryLock();

        // first user attempts booking immediately
        Thread user1 = new Thread(() -> booking.bookTicket("User 1"));

        // second user arrives 500 ms later and may time-out
        Thread user2 = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            booking.bookTicket("User 2");
        });

        user1.start();
        user2.start();
    }
}
