import controller.*;
import model.Direction;
import model.InvalidStateTransitionException;
import repository.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("        TRAFFIC SIGNAL DESIGN LOW LEVEL SYSTEM BOOT           ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        IntersectionRepository intersectionRepository = new IntersectionRepository();
        EmergencyRepository emergencyRepository = new EmergencyRepository();
        TrafficRepository trafficRepository = new TrafficRepository();
        TimingRepository timingRepository = new TimingRepository();

        // 2. Initialize Services
        IntersectionService intersectionService = new IntersectionService(intersectionRepository, timingRepository);
        EmergencyService emergencyService = new EmergencyService(emergencyRepository, intersectionRepository, intersectionService);
        TrafficService trafficService = new TrafficService(trafficRepository);
        TimingService timingService = new TimingService(timingRepository, trafficRepository);

        // 3. Initialize Controllers
        IntersectionController intersectionController = new IntersectionController(intersectionService);
        EmergencyController emergencyController = new EmergencyController(emergencyService);
        TrafficController trafficController = new TrafficController(trafficService);
        TimingController timingController = new TimingController(timingService);

        // 4. Create and Initialize Intersection (Creation Flow)
        int intersectionId = 1;
        intersectionController.createIntersection(intersectionId, "Times Square Crossing");

        // Display initial dashboard (All signals are OFF)
        intersectionController.displayStatus(intersectionId);

        // 5. Bootstrap Phase: Safe transition to RED for all signals
        System.out.println("\n--- Bootstrapping: Powering On All Signals to RED ---");
        intersectionService.setAllSignalsToRed(intersectionId);
        intersectionController.displayStatus(intersectionId);

        // 6. Normal Cycle Simulation
        System.out.println("\n--- Normal Flow: Simulating Standard Automatic Cycles ---");
        // Phase 0: NORTH
        intersectionController.startCycle(intersectionId);
        // Phase 1: EAST
        intersectionController.startCycle(intersectionId);

        // 7. Sensor Count Update & Dynamic Timing Adjustment Flow
        System.out.println("\n--- Sensor Flow: Heavy Traffic Detected on EAST ---");
        // Enable Dynamic Timing for EAST
        timingController.enableDynamicTiming(intersectionId, Direction.EAST, true);
        // Update sensor data to 12 vehicles waiting on EAST
        trafficController.updateVehicleCount(Direction.EAST, 12);
        // Recalculate optimal timings
        timingController.adjustTimingBasedOnTraffic(intersectionId, Direction.EAST);
        
        // Let's run a cycle to see dynamic timing applied on EAST!
        // Current cycle phase index is 2 (SOUTH). Let's cycle:
        // Phase 2: SOUTH
        intersectionController.startCycle(intersectionId);
        // Phase 3: WEST
        intersectionController.startCycle(intersectionId);
        // Phase 0: NORTH
        intersectionController.startCycle(intersectionId);
        // Phase 1: EAST (Should now utilize our dynamically calculated 20s green duration!)
        intersectionController.startCycle(intersectionId);

        // 8. Emergency Preemption Flow (Priority Override & Cycle Recovery)
        System.out.println("\n--- Emergency Flow: Ambulance approaching from SOUTH ---");
        
        // Dispatch emergency SOUTH
        emergencyController.requestEmergency(intersectionId, Direction.SOUTH, 10);
        intersectionController.displayStatus(intersectionId);

        // Clear emergency
        emergencyController.endEmergency(intersectionId);
        intersectionController.displayStatus(intersectionId);

        // Run cycle step to verify it resumes from the correct phase!
        System.out.println("\n--- Normal Flow: Continuing Automatic Cycle post-Emergency ---");
        // Active phase should continue from where it was paused (Phase 2: SOUTH)
        intersectionController.startCycle(intersectionId);

        // 9. State Transition Safety Validation (Edge Case Protection)
        System.out.println("\n--- Safety Flow: Validating State Transition Constraints ---");
        try {
            System.out.println("[Client] Attempting illegal transition: RED signal -> turnYellow()");
            // Try to set NORTH (which is currently RED) directly to YELLOW
            intersectionService.setSignalToYellow(intersectionId, Direction.NORTH);
        } catch (InvalidStateTransitionException e) {
            System.out.println("[Client] BLOCKED: Illegal transition caught! Message: " + e.getMessage());
        }

        System.out.println("\n==============================================================");
        System.out.println("       TRAFFIC SIGNAL DESIGN SYSTEM SIMULATION COMPLETE       ");
        System.out.println("==============================================================");
    }
}
