package model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String id;
    private final String title;
    private final String userId;
    private final List<String> songIds;

    public Playlist(String id, String title, String userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.songIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void addSong(String songId) {
        if (!this.songIds.contains(songId)) {
            this.songIds.add(songId);
        }
    }

    public void removeSong(String songId) {
        this.songIds.remove(songId);
    }
}
