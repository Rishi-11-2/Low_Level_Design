package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class MatchService {

    public void startSimulatedMatch(Match match, List<List<Integer>> overRuns, List<List<BowlType>> overTypes) {
        System.out.println("[MatchService] Starting match at " + match.getVenue() + " on " + match.getDate());
        System.out.println("[MatchService] Format: " + match.getMatchType().getClass().getSimpleName() + " (" + match.getMatchType().getOvers() + " overs)");

        match.setTossWinnerIndex(0);
        System.out.println("[MatchService] Team '" + match.getTeamA().getName() + "' won the toss and chose to BAT first.");

        Innings inn1 = new Innings();
        inn1.setBattingTeam(match.getTeamA());
        inn1.setBowlingTeam(match.getTeamB());

        inn1.addObserver(new BattingScoreCardUpdater());
        inn1.addObserver(new BowlingScoreCardUpdater());

        Team battingTeam = match.getTeamA();
        if (!battingTeam.getPlaying11().isEmpty()) {
            battingTeam.getBattingController().setStriker(battingTeam.getPlaying11().get(0));
            if (battingTeam.getPlaying11().size() > 1) {
                battingTeam.getBattingController().setNonStriker(battingTeam.getPlaying11().get(1));
            }
            for (int i = 2; i < battingTeam.getPlaying11().size(); i++) {
                battingTeam.getBattingController().addPlayerToQueue(battingTeam.getPlaying11().get(i));
            }
        }

        Team bowlingTeam = match.getTeamB();
        Player currentBowler = null;
        if (!bowlingTeam.getPlaying11().isEmpty()) {
            for (Player p : bowlingTeam.getPlaying11()) {
                if (p.getType() == PlayerType.BOWLER || p.getType() == PlayerType.ALLROUNDER) {
                    currentBowler = p;
                    bowlingTeam.getBowlingController().setCurrentBowler(p);
                    break;
                }
            }
            if (currentBowler == null) {
                currentBowler = bowlingTeam.getPlaying11().get(0);
                bowlingTeam.getBowlingController().setCurrentBowler(currentBowler);
            }
        }

        System.out.println("\n--- Innings 1 Start: " + battingTeam.getName() + " batting ---");
        int totalOvers = match.getMatchType().getOvers();

        for (int o = 1; o <= totalOvers; o++) {
            System.out.println("\n--- Over " + o + " (Bowler: " + currentBowler.getName() + ") ---");
            Over over = new Over(inn1, o, currentBowler);

            List<Integer> customRuns = (overRuns != null && o - 1 < overRuns.size()) ? overRuns.get(o - 1) : null;
            List<BowlType> customTypes = (overTypes != null && o - 1 < overTypes.size()) ? overTypes.get(o - 1) : null;

            over.startOver(customRuns, customTypes);
            inn1.addOver(over);

            if (inn1.getWickets() >= battingTeam.getPlaying11().size() - 1) {
                System.out.println("[Innings] All out! Batting team wickets: " + inn1.getWickets());
                break;
            }

            swapStrikers(battingTeam.getBattingController());

            if (bowlingTeam.getPlaying11().size() > 1) {
                for (Player p : bowlingTeam.getPlaying11()) {
                    if (p != currentBowler && (p.getType() == PlayerType.BOWLER || p.getType() == PlayerType.ALLROUNDER)) {
                        currentBowler = p;
                        bowlingTeam.getBowlingController().setCurrentBowler(p);
                        break;
                    }
                }
            }
        }

        match.addInnings(inn1);
        System.out.println("\n==============================================================");
        System.out.println("   INNINGS 1 SUMMARY: " + battingTeam.getName() + " " + inn1.getTotalRuns() + "/" + inn1.getWickets());
        System.out.println("==============================================================");
    }

    private void swapStrikers(PlayerBattingController controller) {
        Player temp = controller.getStriker();
        controller.setStriker(controller.getNonStriker());
        controller.setNonStriker(temp);
    }
}
