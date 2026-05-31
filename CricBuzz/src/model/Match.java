package model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private final Team teamA;
    private final Team teamB;
    private final String venue;
    private final String date;
    private final List<Innings> inningsList = new ArrayList<>();
    private final Matchtype matchType;
    private int tossWinnerIndex = -1;

    public Match(Team teamA, Team teamB, String venue, String date, Matchtype matchType) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.venue = venue;
        this.date = date;
        this.matchType = matchType;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public List<Innings> getInningsList() {
        return inningsList;
    }

    public void addInnings(Innings innings) {
        this.inningsList.add(innings);
    }

    public Matchtype getMatchType() {
        return matchType;
    }

    public int getTossWinnerIndex() {
        return tossWinnerIndex;
    }

    public void setTossWinnerIndex(int tossWinnerIndex) {
        this.tossWinnerIndex = tossWinnerIndex;
    }
}
