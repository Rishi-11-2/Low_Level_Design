


import java.util.*;
import java.util.concurrent.*;
class EmailService{

    public static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void sendEmail(String recipient)
    {
        executor.execute(()->{
            System.out.println("Sending mail. to "+recipient+"on this thread:"+Thread.currentThread().getName());

            try{
                Thread.sleep(2000);

            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            System.out.println("Email sent successfully");
        });
    }

    
}

class FutureExample{
    public static void main(String args[]) throws Exception
    {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer>future = executor.submit(()->{
            Thread.sleep(2000);
            return 69;
        });

        System.out.println("Doing other work");
        Integer result = future.get(); // blocks until resut is ready
        System.out.println("Result:"+result);

        executor.shutdown();
    }
}

class SessionCleaner{
    public static void main(String args[])
    {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = ()->System.out.println("Running a task");
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
}
public class Executor_Service_Class {
    public static void main (String args[])throws Exception
    {
        for(int i = 1; i<=30;i++)
        {
            EmailService.sendEmail("user:"+i+"@gmail.com");
        }
        EmailService.executor.shutdown();

        FutureExample.main(args);
        // FutureExample.executor.shutdown();
    }
}
