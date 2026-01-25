#include <iostream>
#include <string>
using namespace std;

// ==============================
// Strategy Interface
// ==============================
class MatchingStrategy {
public:
    virtual void match(const string& riderLocation) = 0;
    virtual ~MatchingStrategy() {}
};

// ==============================
// Concrete Strategy: Nearest Driver
// ==============================
class NearestDriverStrategy : public MatchingStrategy {
public:
    void match(const string& riderLocation) override {
        cout << "Matching with the nearest available driver to " << riderLocation << endl;
        // Distance-based matching logic
    }
};

// ==============================
// Concrete Strategy: Airport Queue
// ==============================
class AirportQueueStrategy : public MatchingStrategy {
public:
    void match(const string& riderLocation) override {
        cout << "Matching using FIFO airport queue for " << riderLocation << endl;
        // Match first-in-line driver for airport pickup
    }
};

// ==============================
// Concrete Strategy: Surge Priority
// ==============================
class SurgePriorityStrategy : public MatchingStrategy {
public:
    void match(const string& riderLocation) override {
        cout << "Matching rider using surge pricing priority near " << riderLocation << endl;
        // Prioritize high-surge zones or premium drivers
    }
};

// ==============================
// Context Class: RideMatchingService
// ==============================
class RideMatchingService {
private:
    MatchingStrategy* strategy;

public:
    // Constructor injection of strategy
    RideMatchingService(MatchingStrategy* strategy) {
        this->strategy = strategy;
    }

    // Setter injection for changing strategy dynamically
    void setStrategy(MatchingStrategy* strategy) {
        this->strategy = strategy;
    }

    // Delegates the matching logic to the strategy
    void matchRider(const string& location) {
        strategy->match(location);
    }
};

// ==============================
// Client Code
// ==============================
int main() {
    // Using airport queue strategy
    RideMatchingService rideMatchingService(new AirportQueueStrategy());
    rideMatchingService.matchRider("Terminal 1");

    // Using nearest driver strategy and later switching to surge priority
    RideMatchingService rideMatchingService2(new NearestDriverStrategy());
    rideMatchingService2.matchRider("Downtown");
    rideMatchingService2.setStrategy(new SurgePriorityStrategy());
    rideMatchingService2.matchRider("Downtown");

    return 0;
}
