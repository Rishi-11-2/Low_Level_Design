package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private final String id;
    private final String name;
    private final List<String> songIds;
    private final List<String> albumIds;
    private final List<SongSubscriber> subscribers;

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
        this.songIds = new ArrayList<>();
        this.albumIds = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public List<String> getAlbumIds() {
        return albumIds;
    }

    public void addSong(String songId) {
        this.songIds.add(songId);
    }

    public void addAlbum(String albumId) {
        this.albumIds.add(albumId);
    }

    public void subscribe(SongSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void unsubscribe(SongSubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    public void notifySubscribers(Song song) {
        System.out.println("[Artist: " + name + "] Releasing new song '" + song.getTitle() + "'. Notifying " + subscribers.size() + " subscribers.");
        for (SongSubscriber sub : subscribers) {
            sub.onNewRelease(this, song);
        }
    }
}
