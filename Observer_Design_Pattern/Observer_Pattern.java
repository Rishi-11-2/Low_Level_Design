


import java.util.*;
interface subscriber{
     void update(String title);
}


class EmailSubscriber implements subscriber{

    private String email;

    public EmailSubscriber(String email)
    {
        this.email = email;
    }
    public void update(String title)
    {
        System.out.println("Notifying through email subscriber with email:"+email+"for title:"+title);
    }
}

class MobileSubscriber implements subscriber{
    private String username;

    public MobileSubscriber(String username)
    {
        this.username = username;
    }
    public void update(String title)
    {
        System.out.println("Notifying through mobile subscriber with username:"+username+"for title:"+title);

    }
}
interface channel{
    void subscribe(subscriber subscriber);
    void unsubscribe(subscriber subscriber);
    void notifySubscriber(String name);
}

class Youtube implements channel{

    private List<subscriber> subscribers = new ArrayList<>();
    private String name;
    public Youtube(String name){
        this.name = name ;
    }
    public void subscribe(subscriber subscriber)
    {
        subscribers.add(subscriber);
    }

    public void unsubscribe(subscriber subscriber)
    {
        subscribers.remove(subscriber);
    }

    public void notifySubscriber(String name)
    {
        for(subscriber subscriber:subscribers)
        {
            subscriber.update(name);
        }
    }
}
public class Observer_Pattern {
    public static void main(String args[])
    {
        Youtube y = new Youtube("h");

        EmailSubscriber s1 = new EmailSubscriber("rius");
        MobileSubscriber s2 = new MobileSubscriber("RIsh");

        y.subscribe(s1);
        y.subscribe(s2);
        y.notifySubscriber("yo");
        y.unsubscribe(s2);
        y.notifySubscriber("shshs");
    }
}
