package model;

public class Player {
    private final String id;
    private PlayerState state;
    private PlaybackStatus status;
    private Song currentSong;
    private int volume;

    public Player(String id) {
        this.id = id;
        this.state = new StoppedState();
        this.status = PlaybackStatus.STOPPED;
        this.volume = 50;
    }

    public String getId() {
        return id;
    }

    public PlayerState getPlayerState() {
        return state;
    }

    public void setPlayerState(PlayerState state) {
        this.state = state;
    }

    public PlaybackStatus getStatus() {
        return status;
    }

    public void setStatus(PlaybackStatus status) {
        this.status = status;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if (volume >= 0 && volume <= 100) {
            this.volume = volume;
            System.out.println("[MusicPlayer: " + id + "] Volume adjusted to: " + volume + "%");
        }
    }

    public void play(Song song) {
        state.play(this, song);
    }

    public void pause() {
        state.pause(this);
    }

    public void stop() {
        state.stop(this);
    }
}
