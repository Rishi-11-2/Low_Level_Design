import model.*;
import service.MatchService;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("          CRICBUZZ LOW LEVEL DESIGN SYSTEM BOOT               ");
        System.out.println("==============================================================");

        System.out.println("\n--- Setup: Initializing Teams and Squads ---");
        Team india = new Team("India");
        Team australia = new Team("Australia");

        Player rohit = new Player(new Person("Rohit Sharma", "Mumbai", 37), PlayerType.BATTER);
        Player virat = new Player(new Person("Virat Kohli", "Delhi", 35), PlayerType.BATTER);
        Player bumrah = new Player(new Person("Jasprit Bumrah", "Ahmedabad", 31), PlayerType.BOWLER);

        india.addPlayerToPlaying(rohit);
        india.addPlayerToPlaying(virat);
        india.addPlayerToPlaying(bumrah);
        for (int i = 4; i <= 11; i++) {
            india.addPlayerToPlaying(new Player(new Person("IndPlayer" + i, "City", 25 + i), PlayerType.ALLROUNDER));
        }

        Player warner = new Player(new Person("David Warner", "Sydney", 37), PlayerType.BATTER);
        Player starc = new Player(new Person("Mitchell Starc", "Sydney", 34), PlayerType.BOWLER);
        Player cummins = new Player(new Person("Pat Cummins", "Sydney", 31), PlayerType.BOWLER);

        australia.addPlayerToPlaying(warner);
        australia.addPlayerToPlaying(starc);
        australia.addPlayerToPlaying(cummins);
        for (int i = 4; i <= 11; i++) {
            australia.addPlayerToPlaying(new Player(new Person("AusPlayer" + i, "City", 24 + i), PlayerType.ALLROUNDER));
        }

        Matchtype simulatedFormat = new Matchtype(2, 1, 1) {}; 

        Match match = new Match(india, australia, "Wankhede Stadium, Mumbai", "2026-05-31", simulatedFormat);

        List<Integer> over1Runs = new ArrayList<>();
        over1Runs.add(4); 
        over1Runs.add(1); 
        over1Runs.add(0); 
        over1Runs.add(6); 
        over1Runs.add(1); 
        over1Runs.add(0); 
        
        List<BowlType> over1Types = new ArrayList<>();
        for (int i = 0; i < 6; i++) over1Types.add(BowlType.NORMALBALL);

        List<Integer> over2Runs = new ArrayList<>();
        over2Runs.add(1); 
        over2Runs.add(0); 
        over2Runs.add(2); 
        over2Runs.add(4); 
        over2Runs.add(0); 
        over2Runs.add(1); 
        
        List<BowlType> over2Types = new ArrayList<>();
        over2Types.add(BowlType.NORMALBALL);
        over2Types.add(BowlType.OUT);
        for (int i = 0; i < 4; i++) over2Types.add(BowlType.NORMALBALL);

        List<List<Integer>> matchRuns = new ArrayList<>();
        matchRuns.add(over1Runs);
        matchRuns.add(over2Runs);

        List<List<BowlType>> matchTypes = new ArrayList<>();
        matchTypes.add(over1Types);
        matchTypes.add(over2Types);

        MatchService matchService = new MatchService();
        matchService.startSimulatedMatch(match, matchRuns, matchTypes);

        System.out.println("\n==============================================================");
        System.out.println("               CRICBUZZ FINAL SCORECARD                       ");
        System.out.println("==============================================================");
        System.out.println("India Batting Card:");
        for (Player p : india.getPlaying11()) {
            if (p.getBattingScoreCard().getBallsPlayed() > 0) {
                System.out.printf("  %-15s | Runs: %-3d | Balls: %-3d | 4s: %-2d | 6s: %-2d | SR: %-5.2f\n",
                        p.getName(), p.getBattingScoreCard().getTotalRuns(), p.getBattingScoreCard().getBallsPlayed(),
                        p.getBattingScoreCard().getTotalFours(), p.getBattingScoreCard().getTotalSixes(),
                        p.getBattingScoreCard().getStrikeRate());
            }
        }

        System.out.println("\nAustralia Bowling Card:");
        for (Player p : australia.getPlaying11()) {
            if (p.getBowlingScoreCard().getTotalBallsDelivered() > 0) {
                System.out.printf("  %-15s | Balls: %-3d | Runs: %-3d | Wickets: %-2d | Econ: %-5.2f\n",
                        p.getName(), p.getBowlingScoreCard().getTotalBallsDelivered(), p.getBowlingScoreCard().getRunsGiven(),
                        p.getBowlingScoreCard().getWicketsTaken(), p.getBowlingScoreCard().getEconomyRate());
            }
        }
        System.out.println("\n==============================================================");
        System.out.println("          CRICBUZZ SYSTEM SIMULATION COMPLETE                 ");
        System.out.println("==============================================================");
    }
}
