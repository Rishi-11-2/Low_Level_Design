package model;

public class EmailNotificationSubscriber implements SongSubscriber {
    private final String emailAddress;

    public EmailNotificationSubscriber(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void onNewRelease(Artist artist, Song song) {
        System.out.println("[Email Alert] Sending message to " + emailAddress + ": " +
                artist.getName() + " just released a new song: '" + song.getTitle() + "' (" + song.getGenre() + "). Listen now!");
    }
}
