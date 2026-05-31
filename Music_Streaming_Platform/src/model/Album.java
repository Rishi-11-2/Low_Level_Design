package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private final String id;
    private final String title;
    private final String artistId;
    private final List<String> songIds;

    public Album(String id, String title, String artistId) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.songIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistId() {
        return artistId;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void addSong(String songId) {
        this.songIds.add(songId);
    }
}
