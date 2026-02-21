import java.util.concurrent.Semaphore;

class TufAccount{
    private final Semaphore deviceSlots;

    TufAccount(int numberOfDevices)
    {
        this.deviceSlots = new Semaphore(numberOfDevices);
    }

    public boolean login(String user) throws InterruptedException{
        System.out.println("User is trying to login");

        try{
            if(deviceSlots.tryAcquire())
            {
                System.out.println("User is able to login");
            }
            else{
                System.out.println("User was not able to login as too many devices");
            }
        }  
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }  
    }

    public void logout(){
        System.out.println("Releasing the lock");
        deviceSlots.release();
    }
}
public class Semaphore_Example {
    
}
