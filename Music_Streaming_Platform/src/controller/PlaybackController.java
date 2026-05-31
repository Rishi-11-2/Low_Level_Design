package controller;

import model.Player;
import model.Song;
import service.PlaybackService;

public class PlaybackController {
    private final PlaybackService playbackService;

    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    public void playSong(Player player, Song song, String userId) {
        playbackService.startPlayback(player, song, userId);
    }

    public void pauseSong(Player player) {
        playbackService.pausePlayback(player);
    }

    public void stopSong(Player player) {
        playbackService.stopPlayback(player);
    }

    public void adjustVolume(Player player, int volume) {
        playbackService.adjustVolume(player, volume);
    }
}
