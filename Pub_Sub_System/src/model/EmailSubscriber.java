package model;

public class EmailSubscriber implements SubscriberObserver {
    private final String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void update(Message message) {
        System.out.println("[Email Channel] Dispatching email notification to: " + email + " for Message ID: " + message.getId() + " containing: '" + message.getContent() + "'");
    }
}
