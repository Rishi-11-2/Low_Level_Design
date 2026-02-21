import java.util.concurrent.locks.ReentrantLock;

class TicketBooking{


    private int availableSeat = 1;

    private final ReentrantLock lock = new ReentrantLock();


    public void bookTicket(String user)
    {
        lock.lock();
        System.out.println("Trying to buy the ticket");
        try{
            if(availableSeat>0)
            {
                availableSeat--;
                System.out.println("Bought the ticket for user:"+user);
            }
            else
            System.out.println("Unable to buy the ticket for user:"+user);
        }
        finally{
            lock.unlock();
        }
    }
}


public class Reentrant_Lock {
    public static void main(String args[]) throws InterruptedException
    {
        TicketBooking book = new TicketBooking();
        

        

        Thread t1 = new Thread(()->{book.bookTicket("Rishi");});
        Thread t2 = new Thread(()->{book.bookTicket("Shsh");});

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }
}
