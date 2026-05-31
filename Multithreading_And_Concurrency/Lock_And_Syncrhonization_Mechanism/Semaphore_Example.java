import java.util.concurrent.Semaphore;

class TufAccount {
    private final Semaphore deviceSlots;

    TufAccount(int numberOfDevices) {
        this.deviceSlots = new Semaphore(numberOfDevices);
    }

    public boolean login(String user) {
        System.out.println(user + " is trying to login");
        if (deviceSlots.tryAcquire()) {
            System.out.println(user + " is able to login");
            return true;
        } else {
            System.out.println(user + " was not able to login as too many devices");
            return false;
        }
    }

    public void logout() {
        System.out.println("Releasing the lock");
        deviceSlots.release();
    }
}

public class Semaphore_Example {
    public static void main(String[] args) {
        TufAccount account = new TufAccount(2);

        System.out.println("Login 1: " + account.login("User1"));
        System.out.println("Login 2: " + account.login("User2"));
        System.out.println("Login 3: " + account.login("User3"));

        account.logout();

        System.out.println("Login 4: " + account.login("User4"));
    }
}
