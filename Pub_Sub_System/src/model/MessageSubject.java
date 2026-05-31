package model;

import java.util.ArrayList;
import java.util.List;

public class MessageSubject {
    private final List<SubscriberObserver> emailSubscribers = new ArrayList<>();
    private final List<SubscriberObserver> realtimeSubscribers = new ArrayList<>();

    public void addEmailSubscriber(SubscriberObserver observer) {
        if (!emailSubscribers.contains(observer)) {
            emailSubscribers.add(observer);
        }
    }

    public void removeEmailSubscriber(SubscriberObserver observer) {
        emailSubscribers.remove(observer);
    }

    public void addRealtimeSubscriber(SubscriberObserver observer) {
        if (!realtimeSubscribers.contains(observer)) {
            realtimeSubscribers.add(observer);
        }
    }

    public void removeRealtimeSubscriber(SubscriberObserver observer) {
        realtimeSubscribers.remove(observer);
    }

    public List<SubscriberObserver> getEmailSubscribers() {
        return emailSubscribers;
    }

    public List<SubscriberObserver> getRealtimeSubscribers() {
        return realtimeSubscribers;
    }

    public void notify(Message message) {
        notifyEmailSubscribers(message);
        notifyRealtimeSubscribers(message);
    }

    public void notifyEmailSubscribers(Message message) {
        for (SubscriberObserver observer : emailSubscribers) {
            observer.update(message);
        }
    }

    public void notifyRealtimeSubscribers(Message message) {
        for (SubscriberObserver observer : realtimeSubscribers) {
            observer.update(message);
        }
    }
}
