package model;

public class PausedState implements PlayerState {

    @Override
    public void play(Player player, Song song) {
        if (player.getCurrentSong() != null && player.getCurrentSong().getId().equals(song.getId())) {
            System.out.println("[PlayerState: Paused] Resuming playback of song: '" + song.getTitle() + "'");
        } else {
            System.out.println("[PlayerState: Paused] Switching to new track from pause: '" + song.getTitle() + "'");
            player.setCurrentSong(song);
        }
        player.setStatus(PlaybackStatus.PLAYING);
        player.setPlayerState(new PlayingState());
    }

    @Override
    public void pause(Player player) {
        System.out.println("[PlayerState: Paused] Playback is already paused.");
    }

    @Override
    public void stop(Player player) {
        if (player.getCurrentSong() != null) {
            System.out.println("[PlayerState: Paused] Halting playback of song: '" + player.getCurrentSong().getTitle() + "'");
            player.setCurrentSong(null);
            player.setStatus(PlaybackStatus.STOPPED);
            player.setPlayerState(new StoppedState());
        }
    }
}
