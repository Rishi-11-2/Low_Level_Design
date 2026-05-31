package model;

public class PushNotificationSubscriber implements SongSubscriber {
    private final String username;

    public PushNotificationSubscriber(String username) {
        this.username = username;
    }

    @Override
    public void onNewRelease(Artist artist, Song song) {
        System.out.println("[Push Notification] Sending mobile alert to screen of user '" + username + "': " +
                artist.getName() + " dropped a new single: '" + song.getTitle() + "'!");
    }
}
