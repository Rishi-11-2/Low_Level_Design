


class CoffeeMachine{

    private boolean isCoffe = false;

    public synchronized void   makeCoffee() throws InterruptedException
    {

        // while because of spurious wakeups
        // Java threads can sometimes wake up from waits()
        // without being notified -- this is called spurious wakeup
        while(isCoffe)
        {
            wait();
        }

        System.out.println("Making coffee");
        Thread.sleep(1000);
        isCoffe = true;
        System.out.println("Coffee is ready");
        notify();
    }

    public synchronized void consumeCoffee() throws InterruptedException
    {
        while(!isCoffe)
        {
            wait();
        }
        System.out.println("Consuing coffee");
        Thread.sleep(5000);
        isCoffe = false;
        System.out.println("Coffe is consumed");
        notify();
    }

}
public class Coffee_Machine {
    
    
    public static void main(String args[])
    {
        CoffeeMachine machine = new CoffeeMachine();
            // Producer thread that continuously makes coffee
                    Thread producer = new Thread(() -> {
            while (true) {
                try {
                    machine.makeCoffee(); // Try to produce coffee
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consumer thread that continuously drinks coffee
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    machine.consumeCoffee(); // Try to consume coffee
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        producer.start();
        consumer.start();
        }
}
