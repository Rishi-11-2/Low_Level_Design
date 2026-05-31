package model;

public class Ball {
    private final int ballNumber;
    private final Innings innings;
    private BowlType bType = BowlType.NORMALBALL;
    private int runs = 0;
    private Player onStrike;
    private Player bowler;

    public Ball(Innings innings, int ballNumber) {
        this.innings = innings;
        this.ballNumber = ballNumber;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public Innings getInnings() {
        return innings;
    }

    public BowlType getbType() {
        return bType;
    }

    public void setbType(BowlType bType) {
        this.bType = bType;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public Player getOnStrike() {
        return onStrike;
    }

    public void setOnStrike(Player onStrike) {
        this.onStrike = onStrike;
    }

    public Player getBowler() {
        return bowler;
    }

    public void setBowler(Player bowler) {
        this.bowler = bowler;
    }

    public void startBallDeliver() {
        if (bType == BowlType.OUT) {
            runs = 0;
        }

        if (onStrike != null && innings != null) {
            innings.incrementTotalRuns(runs);
            onStrike.getBattingScoreCard().incrementRuns(runs);
            onStrike.getBattingScoreCard().incrementBallsPlayed();
            if (runs == 4) {
                onStrike.getBattingScoreCard().incrementFours();
            } else if (runs == 6) {
                onStrike.getBattingScoreCard().incrementSixes();
            }
        }

        if (bowler != null) {
            bowler.getBowlingScoreCard().incrementBallsDelivered();
            bowler.getBowlingScoreCard().incrementRunsGiven(runs);
            if (bType == BowlType.OUT) {
                bowler.getBowlingScoreCard().incrementWickets();
                if (innings != null) {
                    innings.incrementWickets();
                }
            }
        }

        notifyObservers();
    }

    private void notifyObservers() {
        if (innings != null) {
            innings.notifyObservers(this);
        }
    }
}
