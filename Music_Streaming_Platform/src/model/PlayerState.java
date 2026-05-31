package model;

public interface PlayerState {
    void play(Player player, Song song);
    void pause(Player player);
    void stop(Player player);
}
