package model;

public class StoppedState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        System.out.println("[ElevatorState] Elevator " + elevator.getId() + " is stopped. Transitioning STOPPED -> DOORS_OPENING.");
        elevator.setStateHandler(new DoorsOpeningState());
    }

    @Override
    public void closeDoors(Elevator elevator) {
        // Doors are already closed
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        System.out.println("[ElevatorState] Stopped elevator " + elevator.getId() + " transitioning to MAINTENANCE.");
        elevator.setStateHandler(new MaintenanceState());
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
        return "STOPPED";
    }
}
