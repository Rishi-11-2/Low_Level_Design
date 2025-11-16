#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <functional> // For std::greater

using namespace std;

// Forward declarations
class ElevatorCar;
class ElevatorController;

// Enum for direction
enum class Direction {
    UP,
    DOWN
};

// Enum for elevator state
enum class ElevatorState {
    MOVING,
    IDLE
};

// --- Class Definitions ---

// Manages the elevator door
class ElevatorDoor {
public:
    void openDoor() {
        cout << "Opening the Elevator door." << endl;
    }
    void closeDoor() {
        cout << "Closing the Elevator door." << endl;
    }
};

// Manages the display inside the elevator
class ElevatorDisplay {
private:
    int floor;
    Direction direction;
public:
    void setDisplay(int floor, Direction direction) {
        this->floor = floor;
        this->direction = direction;
    }
    void showDisplay() {
        cout << "Display: Floor " << floor << " | Direction: " << (direction == Direction::UP ? "UP" : "DOWN") << endl;
    }
};

// Dispatches requests made from inside the elevator
class InternalDispatcher {
public:
    static vector<ElevatorController*> elevatorControllerList;
    void submitInternalRequest(int floor, ElevatorCar* elevatorCar);
};

// Manages the buttons inside the elevator
class InternalButtons {
private:
    InternalDispatcher* dispatcher;
public:
    InternalButtons() { dispatcher = new InternalDispatcher(); }
    ~InternalButtons() { delete dispatcher; }
    void pressButton(int destination, ElevatorCar* elevatorCar) {
        dispatcher->submitInternalRequest(destination, elevatorCar);
    }
};

// Represents the elevator car itself
class ElevatorCar {
public:
    int id;
    ElevatorDisplay* display;
    InternalButtons* internalButtons;
    ElevatorState elevatorState;
    int currentFloor;
    Direction elevatorDirection;
    ElevatorDoor* elevatorDoor;

    ElevatorCar(int id) {
        this->id = id;
        display = new ElevatorDisplay();
        internalButtons = new InternalButtons();
        elevatorState = ElevatorState::IDLE;
        currentFloor = 0; // Ground floor
        elevatorDirection = Direction::UP;
        elevatorDoor = new ElevatorDoor();
    }

    ~ElevatorCar() {
        delete display;
        delete internalButtons;
        delete elevatorDoor;
    }

    void showDisplay() {
        display->showDisplay();
    }

    void pressButton(int destination) {
        internalButtons->pressButton(destination, this);
    }

    void setDisplay() {
        display->setDisplay(currentFloor, elevatorDirection);
    }

    // Corrected moveElevator logic
    void moveElevator(int destinationFloor) {
        cout << "\nElevator " << id << " moving from " << currentFloor << " to " << destinationFloor << endl;
        if (currentFloor < destinationFloor) {
            elevatorDirection = Direction::UP;
             for (int i = currentFloor; i <= destinationFloor; ++i) {
                currentFloor = i;
                setDisplay();
                showDisplay();
            }
        } else {
            elevatorDirection = Direction::DOWN;
            for (int i = currentFloor; i >= destinationFloor; --i) {
                currentFloor = i;
                setDisplay();
                showDisplay();
            }
        }
        cout << "Elevator " << id << " arrived at floor " << currentFloor << endl;
    }
};

// The main logic for controlling the elevator
class ElevatorController {
private:
    // Min-heap for UP requests
    priority_queue<int, vector<int>, greater<int>> upMinPQ;
    // Max-heap for DOWN requests
    priority_queue<int> downMaxPQ;

public:
    ElevatorCar* elevatorCar;

    ElevatorController(ElevatorCar* elevatorCar) {
        this->elevatorCar = elevatorCar;
    }

    void submitExternalRequest(int floor, Direction direction) {
        cout << "External request at floor " << floor << " to go " << (direction == Direction::UP ? "UP" : "DOWN") << endl;
        if (direction == Direction::UP) {
            upMinPQ.push(floor);
        } else {
            downMaxPQ.push(floor);
        }
    }

    void submitInternalRequest(int floor) {
        cout << "Internal request for floor " << floor << endl;
        if (elevatorCar->elevatorState == ElevatorState::IDLE) {
            if (elevatorCar->currentFloor < floor) {
                 upMinPQ.push(floor);
                 elevatorCar->elevatorDirection = Direction::UP;
                 elevatorCar->elevatorState = ElevatorState::MOVING;
            } else if (elevatorCar->currentFloor > floor) {
                downMaxPQ.push(floor);
                elevatorCar->elevatorDirection = Direction::DOWN;
                elevatorCar->elevatorState = ElevatorState::MOVING;
            }
        }
        else if(elevatorCar->elevatorDirection == Direction::UP) {
             if (floor > elevatorCar->currentFloor) {
                upMinPQ.push(floor);
             } else {
                downMaxPQ.push(floor); // Add to other queue for later
             }
        }
        else { // Direction is DOWN
            if (floor < elevatorCar->currentFloor) {
                downMaxPQ.push(floor);
            } else {
                upMinPQ.push(floor); // Add to other queue for later
            }
        }
    }

    // Corrected elevator control logic
    void controlElevator() {
        if (elevatorCar->elevatorState == ElevatorState::MOVING) {
            if (elevatorCar->elevatorDirection == Direction::UP) {
                if (!upMinPQ.empty()) {
                    int dest = upMinPQ.top();
                    upMinPQ.pop();
                    elevatorCar->moveElevator(dest);
                } else {
                    if (!downMaxPQ.empty()) {
                         elevatorCar->elevatorDirection = Direction::DOWN;
                    } else {
                         elevatorCar->elevatorState = ElevatorState::IDLE;
                         cout << "Elevator " << elevatorCar->id << " is now IDLE." << endl;
                    }
                }
            } else { // Direction is DOWN
                if (!downMaxPQ.empty()) {
                    int dest = downMaxPQ.top();
                    downMaxPQ.pop();
                    elevatorCar->moveElevator(dest);
                } else {
                     if (!upMinPQ.empty()) {
                        elevatorCar->elevatorDirection = Direction::UP;
                     } else {
                        elevatorCar->elevatorState = ElevatorState::IDLE;
                        cout << "Elevator " << elevatorCar->id << " is now IDLE." << endl;
                     }
                }
            }
        }
        else // if the elevator is idle we first go up otherwise we go down
        {
            if(!upMinPQ.empty())
            {
                int dest = upMinPQ.top();
                upMinPQ.pop();
                elevatorCar->moveElevator(dest);
            }
            else if(!downMaxPQ.empty())
            {
                int dest = downMaxPQ.top();
                downMaxPQ.pop();
                elevatorCar->moveElevator(dest);
            }

        }
    }

    // void controlElevator()
    // {
    //     while(elevatorCar->elevatorDirection)
    // }
};

// Dispatches requests made from outside the elevator
class ExternalDispatcher {
public:
    static vector<ElevatorController*>& getElevatorControllerList() {
        return InternalDispatcher::elevatorControllerList;
    }
    void submitExternalRequest(int floor, Direction direction) {
        // Simple even/odd floor logic for assigning elevator
        for (ElevatorController* controller : getElevatorControllerList()) {
            int elevatorID = controller->elevatorCar->id;
            if (elevatorID % 2 != 0 && floor % 2 != 0) {
                controller->submitExternalRequest(floor, direction);
            } else if (elevatorID % 2 == 0 && floor % 2 == 0) {
                controller->submitExternalRequest(floor, direction);
            }
        }
    }
};

// Represents a floor in the building
class Floor {
public:
    int floorNumber;
    ExternalDispatcher* externalDispatcher;
    Floor(int floorNumber) {
        this->floorNumber = floorNumber;
        externalDispatcher = new ExternalDispatcher();
    }
    ~Floor() { delete externalDispatcher; }
    void pressButton(Direction direction) {
        externalDispatcher->submitExternalRequest(floorNumber, direction);
    }
};

// Represents the building with floors
class Building {
private:
    vector<Floor*> floorList;
public:
    Building(vector<Floor*> floors) {
        this->floorList = floors;
    }
    ~Building() {
        for(auto floor : floorList) delete floor;
    }
};

// --- Static Initialization & Method Implementations ---

vector<ElevatorController*> InternalDispatcher::elevatorControllerList;

void InternalDispatcher::submitInternalRequest(int floor, ElevatorCar* elevatorCar) {
    for (ElevatorController* controller : elevatorControllerList) {
        if (controller->elevatorCar->id == elevatorCar->id) {
            controller->submitInternalRequest(floor);
            return;
        }
    }
}

// Creates and initializes elevators
class ElevatorCreator {
public:
    static void createElevators(int count) {
        for (int i = 1; i <= count; ++i) {
            ElevatorCar* newCar = new ElevatorCar(i);
            ElevatorController* newController = new ElevatorController(newCar);
            InternalDispatcher::elevatorControllerList.push_back(newController);
        }
    }
};

// --- Main Program ---
int main() {
    // Setup
    ElevatorCreator::createElevators(2);
    vector<Floor*> floors;
    for(int i = 0; i < 10; ++i) floors.push_back(new Floor(i));
    Building building(floors);

    // --- Simulation ---
    ElevatorController* controller1 = InternalDispatcher::elevatorControllerList[0];
    ElevatorCar* car1 = controller1->elevatorCar;

    // 1. Someone on floor 3 presses the UP button
    floors[3]->pressButton(Direction::UP);
    
    // 2. Elevator controller picks up the request
    controller1->controlElevator(); // Will move to floor 3
    
    // 3. Once at floor 3, person gets in and presses 7
    car1->pressButton(7);
    
    // 4. Controller processes the internal request
    controller1->controlElevator(); // Will move to floor 7

    // --- Cleanup ---
    for(auto controller : InternalDispatcher::elevatorControllerList) {
        delete controller->elevatorCar;
        delete controller;
    }

    return 0;
}
