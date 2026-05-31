package controller;

import model.Song;
import model.SongSubscriber;
import service.ArtistNotificationService;

public class ArtistNotificationController {
    private final ArtistNotificationService notificationService;

    public ArtistNotificationController(ArtistNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void subscribeUser(String userId, String artistId, SongSubscriber subscriber) {
        notificationService.subscribeUserToArtist(userId, artistId, subscriber);
    }

    public void unsubscribeUser(String userId, String artistId, SongSubscriber subscriber) {
        notificationService.unsubscribeUserFromArtist(userId, artistId, subscriber);
    }

    public void releaseSong(String artistId, Song song) {
        notificationService.releaseNewSong(artistId, song);
    }
}
