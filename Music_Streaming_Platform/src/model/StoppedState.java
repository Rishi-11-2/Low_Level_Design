package model;

public class StoppedState implements PlayerState {

    @Override
    public void play(Player player, Song song) {
        System.out.println("[PlayerState: Stopped] Starting playback of song: '" + song.getTitle() + "'");
        player.setCurrentSong(song);
        player.setStatus(PlaybackStatus.PLAYING);
        player.setPlayerState(new PlayingState());
    }

    @Override
    public void pause(Player player) {
        System.out.println("[PlayerState: Stopped] Player is already stopped. Cannot pause.");
    }

    @Override
    public void stop(Player player) {
        System.out.println("[PlayerState: Stopped] Player is already stopped.");
    }
}
