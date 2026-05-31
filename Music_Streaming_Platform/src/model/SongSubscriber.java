package model;

public interface SongSubscriber {
    void onNewRelease(Artist artist, Song song);
}
