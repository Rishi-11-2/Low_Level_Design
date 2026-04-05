import java.util.*;
import java.util.concurrent.*;

public class ThreadPool
{
    public static void main(String args[]) throws InterruptedException
    {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(int i=0;i<10;i++)
        {
            final int taskId = i;
            executor.submit(()->{
               System.out.println("Task : "+taskId+" Running on this thread :"+Thread.currentThread().getName()); 
               Thread.sleep(5000);
               return null;
            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            // Timeout expired, some tasks still running
            executor.shutdownNow(); // this tries to cancel running tasks by calling Thread.interrupt() on each worker thread
            // but it is a request not  a gurantee .
        }
    }
}