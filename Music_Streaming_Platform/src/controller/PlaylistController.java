package controller;

import model.Playlist;
import service.PlaylistService;

public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public Playlist createPlaylist(String id, String title, String userId) {
        return playlistService.createPlaylist(id, title, userId);
    }

    public void addSongToPlaylist(String playlistId, String songId) {
        playlistService.addSongToPlaylist(playlistId, songId);
    }

    public void removeSongFromPlaylist(String playlistId, String songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
    }
}
