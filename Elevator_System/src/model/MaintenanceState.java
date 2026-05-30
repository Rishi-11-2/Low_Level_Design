package model;

public class MaintenanceState implements ElevatorStateHandler {
    @Override
    public void openDoors(Elevator elevator) {
        System.out.println("[ElevatorState] Elevator " + elevator.getId() + " doors opened in MAINTENANCE mode.");
    }

    @Override
    public void closeDoors(Elevator elevator) {
        System.out.println("[ElevatorState] Elevator " + elevator.getId() + " doors closed in MAINTENANCE mode.");
    }

    @Override
    public void enterMaintenance(Elevator elevator) {
        // Already in maintenance
    }

    @Override
    public void exitMaintenance(Elevator elevator) {
        System.out.println("[ElevatorState] Elevator " + elevator.getId() + " exiting maintenance. Transitioning MAINTENANCE -> STOPPED.");
        elevator.setStateHandler(new StoppedState());
    }

    @Override
    public boolean canAcceptExternalRequests(Elevator elevator) {
        return false; // Out of service
    }

    @Override
    public boolean canAcceptInternalRequests(Elevator elevator) {
        return false; // Out of service
    }

    @Override
    public String getStateName() {
        return "MAINTENANCE";
    }
}
