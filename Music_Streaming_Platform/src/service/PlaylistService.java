package service;

import model.Playlist;
import model.User;
import repository.PlaylistRepository;
import repository.UserRepository;

public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    public Playlist createPlaylist(String id, String title, String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User ID " + userId + " does not exist.");
        }
        Playlist playlist = new Playlist(id, title, userId);
        playlistRepository.save(playlist);
        user.addPlaylist(id);
        System.out.println("[PlaylistService] Created playlist '" + title + "' (ID: " + id + ") for user: " + user.getUsername());
        return playlist;
    }

    public void addSongToPlaylist(String playlistId, String songId) {
        Playlist playlist = playlistRepository.findById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist ID " + playlistId + " does not exist.");
        }
        playlist.addSong(songId);
        System.out.println("[PlaylistService] Added Song ID " + songId + " to Playlist '" + playlist.getTitle() + "'");
    }

    public void removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = playlistRepository.findById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist ID " + playlistId + " does not exist.");
        }
        playlist.removeSong(songId);
        System.out.println("[PlaylistService] Removed Song ID " + songId + " from Playlist '" + playlist.getTitle() + "'");
    }

    public Playlist getPlaylist(String id) {
        return playlistRepository.findById(id);
    }
}
