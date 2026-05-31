package service;

import model.*;
import repository.ElevatorRepository;
import repository.ExternalRequestRepository;
import repository.InternalRequestRepository;
import strategy.MovementStrategy;
import strategy.ScanStrategy;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MovementService {
    private final ElevatorRepository elevatorRepository;
    private final ExternalRequestRepository externalRequestRepository;
    private final InternalRequestRepository internalRequestRepository;
    private final RequestService requestService;
    private final DispatcherService dispatcherService;
    
    private MovementStrategy movementStrategy;
    private final Map<String, ScheduledExecutorService> schedulers = new ConcurrentHashMap<>();

    public MovementService(ElevatorRepository elevatorRepository,
                           ExternalRequestRepository externalRequestRepository,
                           InternalRequestRepository internalRequestRepository,
                           RequestService requestService,
                           DispatcherService dispatcherService) {
        this.elevatorRepository = elevatorRepository;
        this.externalRequestRepository = externalRequestRepository;
        this.internalRequestRepository = internalRequestRepository;
        this.requestService = requestService;
        this.dispatcherService = dispatcherService;
        this.movementStrategy = new ScanStrategy(); // Default: SCAN algorithm
    }

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
        System.out.println("[MovementService] Movement pathing strategy switched to: " + strategy.getClass().getSimpleName());
    }

    public void startElevatorSystem(String buildingId) {
        if (schedulers.containsKey(buildingId)) {
            System.out.println("[MovementService] System is already running for building " + buildingId);
            return;
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        
        // Task 1: Run request dispatcher every 1 second
        scheduler.scheduleAtFixedRate(() -> {
            try {
                dispatcherService.processPendingRequests(buildingId);
            } catch (Exception e) {
                System.err.println("Error in Dispatcher Task: " + e.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);

        // Task 2: Run elevator movement processor every 2 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                processAllElevatorMovements(buildingId);
            } catch (Exception e) {
                System.err.println("Error in Movement Task: " + e.getMessage());
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);

        schedulers.put(buildingId, scheduler);
        System.out.println("[MovementService] Background Scheduled Services started successfully.");
    }

    public void stopElevatorSystem(String buildingId) {
        ScheduledExecutorService scheduler = schedulers.remove(buildingId);
        if (scheduler != null) {
            System.out.println("[MovementService] Stopping elevator background services gracefully...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("[MovementService] Background Scheduled Services terminated.");
        }
    }

    public synchronized void processAllElevatorMovements(String buildingId) {
        List<Elevator> elevators = elevatorRepository.findByBuilding(buildingId);
        for (Elevator elevator : elevators) {
            processElevatorMovement(elevator.getId(), elevator);
        }
    }

    public synchronized void processElevatorMovement(String elevatorId, Elevator elevator) {
        if (!elevator.isActive()) {
            return;
        }

        // 1. Fetch all assigned external requests and pending internal requests
        List<ExternalRequest> externalRequests = externalRequestRepository.findAll().stream()
                .filter(r -> elevatorId.equals(r.getAssignedElevatorId()) && r.getStatus() == RequestStatus.ASSIGNED)
                .collect(Collectors.toList());

        List<InternalRequest> internalRequests = internalRequestRepository.findPendingByElevator(elevatorId);

        // Collect all distinct target floor numbers, preserving order for FCFS
        Set<Integer> targetFloors = new LinkedHashSet<>();
        for (ExternalRequest req : externalRequests) {
            targetFloors.add(req.getFloorNumber());
        }
        for (InternalRequest req : internalRequests) {
            targetFloors.add(req.getDestinationFloor());
        }

        // 2. Compute path based on current strategy
        List<Integer> path = movementStrategy.calculatePath(elevator, new ArrayList<>(targetFloors));

        if (path.isEmpty()) {
            if (elevator.getDirection() != Direction.IDLE) {
                elevator.setDirection(Direction.IDLE);
                elevator.setStateHandler(new StoppedState());
                elevatorRepository.save(elevator);
                System.out.println("[Elevator " + elevatorId + "] Stopped. Operational mode: IDLE.");
            }
            
            // Check if elevator is in PRE_MAINTENANCE state. If so, and path is empty, transition to MAINTENANCE!
            if (elevator.getStateHandler() instanceof PreMaintenanceState) {
                System.out.println("[Elevator " + elevatorId + "] Has finished all pending passenger requests. Transitioning PRE_MAINTENANCE -> MAINTENANCE.");
                elevator.setStateHandler(new MaintenanceState());
                elevator.setActive(false);
                elevatorRepository.save(elevator);
            }
            return;
        }

        int targetFloor = path.get(0);
        int currentFloor = elevator.getCurrentFloor();

        if (currentFloor == targetFloor) {
            // Arrival!
            System.out.println("\n--- [Arrival Alert: Elevator " + elevatorId + " reached Floor " + targetFloor + "] ---");
            elevator.setStateHandler(new StoppedState());
            elevator.openDoors();

            // Simulate load changing (passengers get out / get in)
            // Complete all requests matching this floor
            for (ExternalRequest req : externalRequests) {
                if (req.getFloorNumber() == targetFloor) {
                    requestService.completeRequest(req.getId());
                }
            }
            for (InternalRequest req : internalRequests) {
                if (req.getDestinationFloor() == targetFloor) {
                    requestService.completeRequest(req.getId());
                    // Decrease cabin load by 1 passenger
                    elevator.setCurrentLoad(Math.max(0, elevator.getCurrentLoad() - 1));
                }
            }

            elevator.closeDoors();
            elevatorRepository.save(elevator);

        } else if (currentFloor < targetFloor) {
            // Move Up
            elevator.setDirection(Direction.UP);
            elevator.setStateHandler(new MovingState());
            elevator.setCurrentFloor(currentFloor + 1);
            elevatorRepository.save(elevator);
            System.out.println("[MovementService] Elevator " + elevatorId + " cabin moved UP to Floor " + (currentFloor + 1));
        } else {
            // Move Down
            elevator.setDirection(Direction.DOWN);
            elevator.setStateHandler(new MovingState());
            elevator.setCurrentFloor(currentFloor - 1);
            elevatorRepository.save(elevator);
            System.out.println("[MovementService] Elevator " + elevatorId + " cabin moved DOWN to Floor " + (currentFloor - 1));
        }
    }
}
