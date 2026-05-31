package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String id;
    private final String username;
    private final String email;
    private final String favoriteGenre;
    private final List<String> playlistIds;
    private final Map<String, Integer> playCounts;

    public User(String id, String username, String email, String favoriteGenre) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.favoriteGenre = favoriteGenre;
        this.playlistIds = new ArrayList<>();
        this.playCounts = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public List<String> getPlaylistIds() {
        return playlistIds;
    }

    public Map<String, Integer> getPlayCounts() {
        return playCounts;
    }

    public void addPlaylist(String playlistId) {
        this.playlistIds.add(playlistId);
    }

    public void incrementPlayCount(String songId) {
        this.playCounts.put(songId, this.playCounts.getOrDefault(songId, 0) + 1);
    }
}
