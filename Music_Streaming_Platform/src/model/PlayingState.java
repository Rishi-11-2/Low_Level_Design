package model;

public class PlayingState implements PlayerState {

    @Override
    public void play(Player player, Song song) {
        System.out.println("[PlayerState: Playing] Already playing active stream. Switching to new track: '" + song.getTitle() + "'");
        player.setCurrentSong(song);
    }

    @Override
    public void pause(Player player) {
        if (player.getCurrentSong() != null) {
            System.out.println("[PlayerState: Playing] Suspending playback of song: '" + player.getCurrentSong().getTitle() + "'");
            player.setStatus(PlaybackStatus.PAUSED);
            player.setPlayerState(new PausedState());
        }
    }

    @Override
    public void stop(Player player) {
        if (player.getCurrentSong() != null) {
            System.out.println("[PlayerState: Playing] Halting playback of song: '" + player.getCurrentSong().getTitle() + "'");
            player.setCurrentSong(null);
            player.setStatus(PlaybackStatus.STOPPED);
            player.setPlayerState(new StoppedState());
        }
    }
}
