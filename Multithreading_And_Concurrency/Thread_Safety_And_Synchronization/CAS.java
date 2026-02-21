


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;;


class PurchaseAtomicCounter{

    AtomicInteger counter  = new AtomicInteger(0);

    public void increment()
    {
        int prev, next ;

        do{
            prev = counter.get();
            next = prev+1;

        }while(!counter.compareAndSet(prev, next)); // compareAndSet is atomic on hardware level
        // very similar to optimistic locking 


        // Java's compareAndSet uses hardware-level compareAndSwap lock - free instruction which checks if the given
        // location holds a particular value .if so it swaps it with the expected value 
    }

    public int getCount()
    {
        return counter.get();
    }
}
public class CAS {

    public static void main(String args[]) throws InterruptedException
    {
        PurchaseAtomicCounter counter = new PurchaseAtomicCounter();

        Runnable task = ()->{
            for(int i=1;i<=1000;i++)
            {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(counter.getCount());
    }
}
