import java.util.*;

// ==============================
// Observer Interface
// ==============================
interface Subscriber {
    void update(String videoTitle);
}

// ==============================
// Concrete Observer: Email
// ==============================
class EmailSubscriber implements Subscriber {
    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String videoTitle) {
        System.out.println("Email sent to " + email + ": New video uploaded - " + videoTitle);
    }
}

// ==============================
// Concrete Observer: Mobile App
// ==============================
class MobileAppSubscriber implements Subscriber {
    private String username;

    public MobileAppSubscriber(String username) {
        this.username = username;
    }

    @Override
    public void update(String videoTitle) {
        System.out.println("In-app notification for " + username + ": New video - " + videoTitle);
    }
}

// ==============================
// Subject Interface
// ==============================
interface Channel {
    void subscribe(Subscriber subscriber);
    void unsubscribe(Subscriber subscriber);
    void notifySubscribers(String videoTitle);
}

// ==============================
// Concrete Subject: YouTubeChannel
// ==============================
class YouTubeChannel implements Channel {
    private List<Subscriber> subscribers = new ArrayList<>();
    private String channelName;

    public YouTubeChannel(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String videoTitle) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(videoTitle);
        }
    }

    // Simulates video upload and triggers notifications
    public void uploadVideo(String videoTitle) {
        System.out.println(channelName + " uploaded: " + videoTitle + "\n");
        notifySubscribers(videoTitle);
    }
}

// ==============================
// Client Code
// ==============================
class Main {
    public static void main(String[] args) {
        YouTubeChannel tuf = new YouTubeChannel("takeUforward");

        // Add subscribers
        tuf.subscribe(new MobileAppSubscriber("raj"));
        tuf.subscribe(new EmailSubscriber("rahul@example.com"));

        // Upload video and notify all observers
        tuf.uploadVideo("observer-pattern");
    }
}
