package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final String name;
    private final List<Player> playing11 = new ArrayList<>();
    private final List<Player> bench = new ArrayList<>();
    private final PlayerBattingController battingController;
    private final PlayerBowlingController bowlingController;

    public Team(String name) {
        this.name = name;
        this.battingController = new PlayerBattingController();
        this.bowlingController = new PlayerBowlingController();
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlaying11() {
        return playing11;
    }

    public List<Player> getBench() {
        return bench;
    }

    public PlayerBattingController getBattingController() {
        return battingController;
    }

    public PlayerBowlingController getBowlingController() {
        return bowlingController;
    }

    public void addPlayerToPlaying(Player p) {
        playing11.add(p);
    }

    public void addPlayerToBench(Player p) {
        bench.add(p);
    }
}
