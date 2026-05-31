package model;

public class BattingScoreCardUpdater implements ScoreUpdaterObserver {

    @Override
    public void update(Ball ball) {
        Player batter = ball.getOnStrike();
        if (batter == null) return;
        System.out.println("[BattingObserver] " + batter.getName() + " scored " + ball.getRuns() +
                " runs on ball " + ball.getBallNumber());
    }
}
