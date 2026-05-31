package service;

import model.Artist;
import model.Song;
import model.SongSubscriber;
import model.User;
import repository.ArtistRepository;
import repository.UserRepository;

public class ArtistNotificationService {
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    public ArtistNotificationService(ArtistRepository artistRepository, UserRepository userRepository) {
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
    }

    public void subscribeUserToArtist(String userId, String artistId, SongSubscriber subscriber) {
        User user = userRepository.findById(userId);
        Artist artist = artistRepository.findById(artistId);
        if (user == null || artist == null) {
            throw new IllegalArgumentException("Invalid user or artist ID.");
        }
        artist.subscribe(subscriber);
        System.out.println("[ArtistNotificationService] User '" + user.getUsername() + "' subscribed to Artist: '" + artist.getName() + "'");
    }

    public void unsubscribeUserFromArtist(String userId, String artistId, SongSubscriber subscriber) {
        User user = userRepository.findById(userId);
        Artist artist = artistRepository.findById(artistId);
        if (user == null || artist == null) {
            throw new IllegalArgumentException("Invalid user or artist ID.");
        }
        artist.unsubscribe(subscriber);
        System.out.println("[ArtistNotificationService] User '" + user.getUsername() + "' unsubscribed from Artist: '" + artist.getName() + "'");
    }

    public void releaseNewSong(String artistId, Song song) {
        Artist artist = artistRepository.findById(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist ID " + artistId + " does not exist.");
        }
        artist.notifySubscribers(song);
    }
}
