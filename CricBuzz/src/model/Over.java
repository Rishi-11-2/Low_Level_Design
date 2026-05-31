package model;

import java.util.ArrayList;
import java.util.List;

public class Over {
    private final int overNumber;
    private final List<Ball> balls = new ArrayList<>();
    private final Innings innings;
    private final Player bowler;

    public Over(Innings innings, int overNumber, Player bowler) {
        this.innings = innings;
        this.overNumber = overNumber;
        this.bowler = bowler;
    }

    public int getOverNumber() {
        return overNumber;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Innings getInnings() {
        return innings;
    }

    public Player getBowler() {
        return bowler;
    }

    public void startOver(List<Integer> ballRuns, List<BowlType> ballTypes) {
        for (int i = 1; i <= 6; ++i) {
            Ball b = new Ball(innings, (overNumber - 1) * 6 + i);
            b.setBowler(bowler);
            
            PlayerBattingController battingController = null;
            if (innings != null && innings.getBattingTeam() != null) {
                battingController = innings.getBattingTeam().getBattingController();
            }

            if (battingController != null) {
                b.setOnStrike(battingController.getStriker());
            }

            if (ballRuns != null && i - 1 < ballRuns.size()) {
                b.setRuns(ballRuns.get(i - 1));
            }
            if (ballTypes != null && i - 1 < ballTypes.size()) {
                b.setbType(ballTypes.get(i - 1));
            }

            balls.add(b);
            b.startBallDeliver();

            // Swap strikers on odd runs
            if (battingController != null && (b.getRuns() == 1 || b.getRuns() == 3)) {
                Player temp = battingController.getStriker();
                battingController.setStriker(battingController.getNonStriker());
                battingController.setNonStriker(temp);
            }

            // Handle Wicket / OUT
            if (battingController != null && b.getbType() == BowlType.OUT) {
                if (!battingController.getYetToPlay().isEmpty()) {
                    Player nextBatter = battingController.getYetToPlay().poll();
                    System.out.println("[Wicket] " + battingController.getStriker().getName() + " is OUT! Next batter in: " + nextBatter.getName());
                    battingController.setStriker(nextBatter);
                } else {
                    System.out.println("[Wicket] " + battingController.getStriker().getName() + " is OUT! No more batsmen left.");
                    break;
                }
            }
        }
    }
}
