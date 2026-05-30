package model;

public class MovingState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        System.out.println("[Safety Alert] Cannot open doors of elevator " + elevator.getId() + " while it is MOVING!");
    }

    @Override
    public void closeDoors(Elevator elevator) {
        // Doors are already closed while moving
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        System.out.println("[ElevatorState] Moving elevator " + elevator.getId() + " requested to enter MAINTENANCE. Transitioning to PRE-MAINTENANCE until stops.");
        elevator.setStateHandler(new PreMaintenanceState());
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
        return "MOVING";
    }
}
