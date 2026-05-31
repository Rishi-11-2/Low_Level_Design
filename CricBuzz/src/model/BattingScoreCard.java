package model;

public class BattingScoreCard {
    private int totalRuns = 0;
    private int ballsPlayed = 0;
    private int totalFours = 0;
    private int totalSixes = 0;

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public void incrementRuns(int runs) {
        this.totalRuns += runs;
    }

    public int getBallsPlayed() {
        return ballsPlayed;
    }

    public void setBallsPlayed(int ballsPlayed) {
        this.ballsPlayed = ballsPlayed;
    }

    public void incrementBallsPlayed() {
        this.ballsPlayed++;
    }

    public int getTotalFours() {
        return totalFours;
    }

    public void setTotalFours(int totalFours) {
        this.totalFours = totalFours;
    }

    public void incrementFours() {
        this.totalFours++;
    }

    public int getTotalSixes() {
        return totalSixes;
    }

    public void setTotalSixes(int totalSixes) {
        this.totalSixes = totalSixes;
    }

    public void incrementSixes() {
        this.totalSixes++;
    }

    public double getStrikeRate() {
        if (ballsPlayed == 0) return 0.0;
        return ((double) totalRuns / ballsPlayed) * 100.0;
    }
}
