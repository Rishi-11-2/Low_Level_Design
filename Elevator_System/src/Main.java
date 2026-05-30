import controller.*;
import model.Building;
import model.Elevator;
import repository.*;
import service.*;
import strategy.NearestElevatorStrategy;
import strategy.ScanStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("         ELEVATOR SYSTEM DESIGN LOW LEVEL SYSTEM BOOT         ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        BuildingRepository buildingRepository = new BuildingRepository();
        ElevatorRepository elevatorRepository = new ElevatorRepository();
        ExternalRequestRepository externalRequestRepository = new ExternalRequestRepository();
        InternalRequestRepository internalRequestRepository = new InternalRequestRepository();

        // 2. Initialize Services
        BuildingService buildingService = new BuildingService(buildingRepository);
        ElevatorService elevatorService = new ElevatorService(elevatorRepository);
        RequestService requestService = new RequestService(externalRequestRepository, internalRequestRepository);
        DispatcherService dispatcherService = new DispatcherService(externalRequestRepository, elevatorRepository);
        MovementService movementService = new MovementService(
                elevatorRepository, externalRequestRepository, internalRequestRepository, requestService, dispatcherService
        );

        // Configure Strategies
        dispatcherService.setElevatorSelectionStrategy(new NearestElevatorStrategy());
        movementService.setMovementStrategy(new ScanStrategy());

        // 3. Initialize Controllers
        ElevatorController elevatorController = new ElevatorController(elevatorService, buildingService, movementService);
        FloorPanelController floorPanelController = new FloorPanelController(requestService, buildingService);
        ElevatorPanelController elevatorPanelController = new ElevatorPanelController(requestService, elevatorService, buildingService);

        // 4. Create Building B1 (Burj Khalifa LLD, floors 0 to 10)
        Building building = buildingService.createBuilding("Burj Khalifa LLD", 0, 10, 2);
        String buildingId = building.getId();

        // 5. Create Elevator Cabins: ELV-1 (capacity 2) and ELV-2 (capacity 4)
        Elevator elv1 = elevatorController.createElevator(buildingId, 2);
        Elevator elv2 = elevatorController.createElevator(buildingId, 4);

        // 6. Start the Elevator System (Initializes background schedulers)
        elevatorController.startElevatorSystem(buildingId);

        // 7. Simulation 1: Outside passengers call elevator (External Requests)
        System.out.println("\n--- Simulation: Outside passengers pressing Floor Buttons ---");
        // Floor 3 passenger wants to go UP
        floorPanelController.pressUpButton(3, buildingId);
        // Floor 7 passenger wants to go DOWN
        floorPanelController.pressDownButton(7, buildingId);

        // Force dispatcher to assign requests
        dispatcherService.processPendingRequests(buildingId);

        // 8. Simulation 2: Schedulers process cabin movements
        System.out.println("\n--- Simulation: Elevator Cabin Movements ---");
        
        // Cabins are at floor 0. Schedulers move them step-by-step
        System.out.println("\n[Scheduler Tick 1]");
        movementService.processAllElevatorMovements(buildingId);
        System.out.println("\n[Scheduler Tick 2]");
        movementService.processAllElevatorMovements(buildingId);
        System.out.println("\n[Scheduler Tick 3]");
        movementService.processAllElevatorMovements(buildingId); // ELV-1 reaches Floor 3!

        // 9. Simulation 3: Passenger boards ELV-1 and selects floor inside cabin (Internal Requests)
        System.out.println("\n--- Simulation: Inside Cabin Floor Selections ---");
        // Passenger inside ELV-1 selects Floor 5
        elevatorPanelController.selectFloor(elv1.getId(), 5);

        // Schedulers move ELV-1 to Floor 5
        System.out.println("\n[Scheduler Tick 4]");
        movementService.processAllElevatorMovements(buildingId);
        System.out.println("\n[Scheduler Tick 5]");
        movementService.processAllElevatorMovements(buildingId); // ELV-1 reaches Floor 5!

        // 10. Simulation 4: Overload Capacity Protection (Edge Case)
        System.out.println("\n--- Simulation: Overload Capacity Protection ---");
        System.out.println("[Simulation] ELV-1 has capacity 2. Let's fill the cabin...");
        elv1.setCurrentLoad(2); // Cabin is full

        // Passenger inside ELV-1 tries to select Floor 8
        elevatorPanelController.selectFloor(elv1.getId(), 8);

        // 11. Simulation 5: Maintenance Mode Engagement (Edge Case)
        System.out.println("\n--- Simulation: Maintenance Mode engagement ---");
        elevatorController.setElevatorMaintenance(elv2.getId(), true);
        
        // Make another call from Floor 2 UP. ELV-2 is in maintenance, so it must not be chosen!
        floorPanelController.pressUpButton(2, buildingId);
        dispatcherService.processPendingRequests(buildingId); // ELV-1 is busy or nearest active, should be chosen
        
        // Restore ELV-2 from maintenance
        elevatorController.setElevatorMaintenance(elv2.getId(), false);

        // 12. Simulation 6: Graceful Shutdown
        System.out.println("\n--- Simulation: Graceful Shutdown ---");
        elevatorController.stopElevatorSystem(buildingId);

        System.out.println("==============================================================");
        System.out.println("       ELEVATOR SYSTEM DESIGN SYSTEM SIMULATION COMPLETE      ");
        System.out.println("==============================================================");
    }
}
