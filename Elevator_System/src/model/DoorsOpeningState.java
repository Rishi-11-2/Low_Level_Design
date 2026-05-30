package model;

public class DoorsOpeningState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        // Already opening/open
    }

    @Override
    public void closeDoors(Elevator elevator) {
        System.out.println("[ElevatorState] Elevator " + elevator.getId() + " doors are closing. Transitioning DOORS_OPENING -> DOORS_CLOSING.");
        elevator.setStateHandler(new DoorsClosingState());
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        System.out.println("[Safety Alert] Cannot enter maintenance while doors are opening/open. Close doors first.");
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
        return "DOORS_OPENING";
    }
}
