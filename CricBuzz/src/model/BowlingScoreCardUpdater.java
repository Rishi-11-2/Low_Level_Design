package model;

public class BowlingScoreCardUpdater implements ScoreUpdaterObserver {

    @Override
    public void update(Ball ball) {
        Player bowler = ball.getBowler();
        if (bowler == null) return;
        System.out.println("[BowlingObserver] " + bowler.getName() + " conceded " + ball.getRuns() +
                " runs on ball " + ball.getBallNumber());
    }
}
