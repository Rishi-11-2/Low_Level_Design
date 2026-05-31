package model;

public abstract class Matchtype {
    private final int overs;
    private final int maximumOversAllowed;
    private final int days;

    public Matchtype(int overs, int maximumOversAllowed, int days) {
        this.overs = overs;
        this.maximumOversAllowed = maximumOversAllowed;
        this.days = days;
    }

    public int getOvers() {
        return overs;
    }

    public int getMaximumOversAllowed() {
        return maximumOversAllowed;
    }

    public int getDays() {
        return days;
    }
}
