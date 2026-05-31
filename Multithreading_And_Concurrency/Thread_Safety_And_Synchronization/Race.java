import java.util.*;
import java.util.concurrent.*;

class RacePurchaseCounter {
    private int counter = 0;

    public void increment() {
        counter++;
    }

    int getCounter() {
        return counter;
    }
}

public class Race {
    public static void main(String args[]) throws Exception {
        RacePurchaseCounter counter = new RacePurchaseCounter();

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

        System.out.println("Race counter (should be <= 2000): " + counter.getCounter());
    }
}
