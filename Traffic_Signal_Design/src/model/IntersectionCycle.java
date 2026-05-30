package model;

public class IntersectionCycle {
    private final int intersectionId;
    private int currentPhase;
    private boolean isPaused;
    private int pausedAtPhase;
    private long phaseStartTime;

    public IntersectionCycle(int intersectionId) {
        this.intersectionId = intersectionId;
        this.currentPhase = 0; // NORTH initially
        this.isPaused = false;
        this.pausedAtPhase = 0;
        this.phaseStartTime = System.currentTimeMillis();
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
        this.phaseStartTime = System.currentTimeMillis();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getPausedAtPhase() {
        return pausedAtPhase;
    }

    public void setPausedAtPhase(int pausedAtPhase) {
        this.pausedAtPhase = pausedAtPhase;
    }

    public long getPhaseStartTime() {
        return phaseStartTime;
    }

    public void setPhaseStartTime(long phaseStartTime) {
        this.phaseStartTime = phaseStartTime;
    }
}
