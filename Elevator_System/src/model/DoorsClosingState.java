package model;

public class DoorsClosingState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        System.out.println("[ElevatorState] Safety sensor triggered! Re-opening doors. Transitioning DOORS_CLOSING -> DOORS_OPENING.");
        elevator.setStateHandler(new DoorsOpeningState());
    }

    @Override
    public void closeDoors(Elevator elevator) {
        // Already closing/closed
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        System.out.println("[Safety Alert] Cannot enter maintenance while doors are closing.");
    }

    @Override
    public void exitMaintenance(Elevator elevator) {
        // Not in maintenance
    }

    @Override
    public boolean canAcceptExternalRequests(Elevator elevator) {
        return true;
    }

    @Override
    public boolean canAcceptInternalRequests(Elevator elevator) {
        return true;
    }

    @Override
    public String getStateName() {
        return "DOORS_CLOSING";
    }
}
