import java.util.*;

class RunnableSMSTask implements Runnable {
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("SMS sent using runnable");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class RunnableEmailTask implements Runnable {
    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println("EMAIL sent using runnable");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class RunnableETATask implements Runnable {
    public void run() {
        try {
            Thread.sleep(5000); // 5-second delay for ETA calculation
            System.out.println("ETA Calculated using Runnable. Estimated Time: 25 minutes.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Thread_Runnable {
    public static void main(String args[]) {
        RunnableSMSTask smsTask = new RunnableSMSTask();
        RunnableEmailTask emailTask = new RunnableEmailTask();
        RunnableETATask etaTask = new RunnableETATask();

        Thread smsThread = new Thread(smsTask);
        Thread emailThread = new Thread(emailTask);
        Thread etaThread = new Thread(etaTask);

        // Start all threads
        smsThread.start();
        System.out.println("Task 1 ongoing...");

        emailThread.start();
        System.out.println("Task 2 ongoing...");

        etaThread.start();
        System.out.println("Task 3 ongoing...");

        // Wait for all threads to finish
        try {
            smsThread.join();
            emailThread.join();
            etaThread.join();
            System.out.println("All tasks completed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
