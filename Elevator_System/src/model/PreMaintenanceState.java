package model;

public class PreMaintenanceState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        // Doors stay locked until it stops at target floor
    }

    @Override
    public void closeDoors(Elevator elevator) {
        // Doors are already closed while moving
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        // Already transitioning
    }

    @Override
    public void exitMaintenance(Elevator elevator) {
        System.out.println("[ElevatorState] Pre-maintenance cancelled. Restoring to STOPPED state.");
        elevator.setStateHandler(new StoppedState());
    }

    @Override
    public boolean canAcceptExternalRequests(Elevator elevator) {
        return false; // Blocks new external requests
    }

    @Override
    public boolean canAcceptInternalRequests(Elevator elevator) {
        return true; // Finishes current internal requests
    }

    @Override
    public String getStateName() {
        return "PRE_MAINTENANCE";
    }
}
