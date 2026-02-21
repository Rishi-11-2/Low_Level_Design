import java.util.concurrent.*;
import java.util.*;




class PurchaseCounter{

    private int counter = 0;

    public void increment()
    {
        synchronized(this)
        {
            counter++;
        }
    }

    int getCounter()
    {
        return counter;
    }
}
public class Synchronized_Block {
    
    public static void main(String args[]) throws Exception
    {
        PurchaseCounter counter = new PurchaseCounter();

        Runnable task = ()->{

            for(int i = 1; i<=1000;i++)
                counter.increment();
        };


        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(counter.getCounter());

    }
}
