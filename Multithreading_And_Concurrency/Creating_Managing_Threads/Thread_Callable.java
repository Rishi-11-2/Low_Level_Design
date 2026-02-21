import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
class SMSTask implements Runnable{
    public void run()
    {
        try{
            Thread.sleep(2000);
            System.out.println("SMS sent using runnable");
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
class EmailTask implements Runnable{
    public void run()
    {
        try{
            Thread.sleep(3000);
            System.out.println("EMAIl sent using runnable");
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

class ETATask implements Callable<String>{
    public String call() throws Exception
    {
        
        Thread.sleep(5000);
        System.out.println("Eta calculated");
        return "ETA is : 25 minutes";
        
    }
}
public class Thread_Callable {
    public static void main(String args[])
    {
        SMSTask smsTask = new SMSTask();
        EmailTask emailTask = new EmailTask();

        Thread smsThread = new Thread(smsTask);
        Thread emailThread = new Thread(emailTask);

        FutureTask<String> etaThreadRunnable = new FutureTask<>(new ETATask()); //Future Task  implements RunnableFuture which extends Runnable , Future
        Thread etaThread = new Thread(etaThreadRunnable);
                // Start all threads
        smsThread.start();
        System.out.println("Task 1 ongoing...");

        emailThread.start();
        System.out.println("Task 2 ongoing...");

        etaThread.start();
        System.out.println("Task 3 ongoing");


        // Wait for all threads to finish
        try {
            smsThread.join();
            emailThread.join();
            String eta = etaThreadRunnable.get(); // waiting for etaThread to complete
            System.out.println(eta);
            System.out.println("All tasks completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
