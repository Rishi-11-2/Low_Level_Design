package model;

public class BowlingScoreCard {
    private int totalBallsDelivered = 0;
    private int runsGiven = 0;
    private int wicketsTaken = 0;

    public int getTotalBallsDelivered() {
        return totalBallsDelivered;
    }

    public void setTotalBallsDelivered(int totalBallsDelivered) {
        this.totalBallsDelivered = totalBallsDelivered;
    }

    public void incrementBallsDelivered() {
        this.totalBallsDelivered++;
    }

    public int getRunsGiven() {
        return runsGiven;
    }

    public void setRunsGiven(int runsGiven) {
        this.runsGiven = runsGiven;
    }

    public void incrementRunsGiven(int runs) {
        this.runsGiven += runs;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public void setWicketsTaken(int wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
    }

    public void incrementWickets() {
        this.wicketsTaken++;
    }

    public double getEconomyRate() {
        if (totalBallsDelivered == 0) return 0.0;
        double overs = totalBallsDelivered / 6.0;
        if (overs == 0.0) return 0.0;
        return runsGiven / overs;
    }
}
