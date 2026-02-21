package Lock_And_Syncrhonization_Mechanism;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class StockData{

    private int price = 0;

    private final static ReadWriteLock  lock = new ReentrantReadWriteLock();

    public void updatePrice()
    {
        lock.writeLock().lock();
        try{
            System.out.println("Lock acquired by thread:"+Thread.currentThread().getName());
            price++;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            lock.writeLock().unlock();
        }
    }

    public void readPrice()
    {
        lock.readLock().lock();
        try{
            System.out.println("Thread :"+Thread.currentThread().getName()+"is reading the price");

        }
        finally{
            lock.readLock().unlock();
        }
    }
    
}
public class Read_Write_Lock {
    public static void main(String args[]) throws InterruptedException
    {

        StockData st = new StockData();

        Runnable task1 = ()->{
            st.updatePrice();
        };

        Runnable task2 = () ->{
            st.readPrice();
        
        };
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        Thread t3 = new Thread(task2);

        t2.start();
        t1.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
