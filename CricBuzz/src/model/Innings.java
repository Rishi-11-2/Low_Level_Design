package model;

import java.util.ArrayList;
import java.util.List;

public class Innings {
    private final List<ScoreUpdaterObserver> observers = new ArrayList<>();
    private Team battingTeam;
    private Team bowlingTeam;
    private final List<Over> overs = new ArrayList<>();
    private int totalRuns = 0;
    private int wickets = 0;

    public void addObserver(ScoreUpdaterObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(Ball ball) {
        for (ScoreUpdaterObserver obs : observers) {
            obs.update(ball);
        }
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBowlingTeam(Team bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
    }

    public List<Over> getOvers() {
        return overs;
    }

    public void addOver(Over over) {
        this.overs.add(over);
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void incrementTotalRuns(int runs) {
        this.totalRuns += runs;
    }

    public int getWickets() {
        return wickets;
    }

    public void incrementWickets() {
        this.wickets++;
    }
}
