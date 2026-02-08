#include <iostream>
#include <string>
using namespace std;

// Abstract class defining the SupportHandler
class SupportHandler {
protected:
    SupportHandler* nextHandler;

public:
    // Method to set the next handler in the chain
    void setNextHandler(SupportHandler* nextHandler) {
        this->nextHandler = nextHandler;
    }

    // Pure virtual method to handle the request
    virtual void handleRequest(string requestType) = 0;
};

// Concrete Handler for General Support
class GeneralSupport : public SupportHandler {
public:
    void handleRequest(string requestType) override {
        if (requestType == "general") {
            cout << "GeneralSupport: Handling general query" << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(requestType);
        }
    }
};

// Concrete Handler for Billing Support
class BillingSupport : public SupportHandler {
public:
    void handleRequest(string requestType) override {
        if (requestType == "refund") {
            cout << "BillingSupport: Handling refund request" << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(requestType);
        }
    }
};

// Concrete Handler for Technical Support
class TechnicalSupport : public SupportHandler {
public:
    void handleRequest(string requestType) override {
        if (requestType == "technical") {
            cout << "TechnicalSupport: Handling technical issue" << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(requestType);
        }
    }
};

// Concrete Handler for Delivery Support
class DeliverySupport : public SupportHandler {
public:
    void handleRequest(string requestType) override {
        if (requestType == "delivery") {
            cout << "DeliverySupport: Handling delivery issue" << endl;
        } else if (nextHandler != nullptr) {
            nextHandler->handleRequest(requestType);
        } else {
            cout << "DeliverySupport: No handler found for request" << endl;
        }
    }
};

// Client Code
int main() {
    SupportHandler* general = new GeneralSupport();
    SupportHandler* billing = new BillingSupport();
    SupportHandler* technical = new TechnicalSupport();
    SupportHandler* delivery = new DeliverySupport();

    // Setting up the chain: general -> billing -> technical -> delivery
    general->setNextHandler(billing);
    billing->setNextHandler(technical);
    technical->setNextHandler(delivery);

    // Testing the chain of responsibility with different request types
    general->handleRequest("refund");
    general->handleRequest("delivery");
    general->handleRequest("unknown");

    // Cleaning up memory
    delete general;
    delete billing;
    delete technical;
    delete delivery;

    return 0;
}
