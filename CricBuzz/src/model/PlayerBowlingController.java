package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerBowlingController {
    private final List<Player> bowlers = new ArrayList<>();
    private final Map<Player, Integer> overCount = new HashMap<>();
    private Player currentBowler;

    public List<Player> getBowlers() {
        return bowlers;
    }

    public Map<Player, Integer> getOverCount() {
        return overCount;
    }

    public Player getCurrentBowler() {
        return currentBowler;
    }

    public void setCurrentBowler(Player currentBowler) {
        this.currentBowler = currentBowler;
    }

    public void addBowler(Player player) {
        this.bowlers.add(player);
        this.overCount.put(player, 0);
    }

    public void incrementOvers(Player player) {
        this.overCount.put(player, this.overCount.getOrDefault(player, 0) + 1);
    }
}
