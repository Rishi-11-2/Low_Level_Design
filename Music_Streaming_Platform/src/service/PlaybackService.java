package service;

import model.Player;
import model.Song;
import model.User;
import repository.UserRepository;

public class PlaybackService {
    private final UserRepository userRepository;

    public PlaybackService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void startPlayback(Player player, Song song, String userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.incrementPlayCount(song.getId());
            System.out.println("[PlaybackService] User '" + user.getUsername() + "' listening history incremented for song: '" + song.getTitle() + "'");
        }
        player.play(song);
    }

    public void pausePlayback(Player player) {
        player.pause();
    }

    public void stopPlayback(Player player) {
        player.stop();
    }

    public void adjustVolume(Player player, int volume) {
        player.setVolume(volume);
    }
}
