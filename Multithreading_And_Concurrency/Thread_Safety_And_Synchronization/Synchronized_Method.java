import java.util.*;
import java.util.concurrent.*;

class MethodPurchaseCounter {
    private int counter = 0;

    synchronized public void increment() {
        counter++;
    }

    int getCounter() {
        return counter;
    }
}

public class Synchronized_Method {
    public static void main(String args[]) throws Exception {
        MethodPurchaseCounter counter = new MethodPurchaseCounter();

        Runnable task = () -> {
            for (int i = 1; i <= 1000; i++)
                counter.increment();
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Synchronized method counter (should be 2000): " + counter.getCounter());
    }
}
