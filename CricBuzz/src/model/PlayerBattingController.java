package model;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerBattingController {
    private final Queue<Player> yetToPlay = new LinkedList<>();
    private Player striker;
    private Player nonStriker;

    public Queue<Player> getYetToPlay() {
        return yetToPlay;
    }

    public Player getStriker() {
        return striker;
    }

    public void setStriker(Player striker) {
        this.striker = striker;
    }

    public Player getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(Player nonStriker) {
        this.nonStriker = nonStriker;
    }

    public void addPlayerToQueue(Player player) {
        yetToPlay.add(player);
    }
}
