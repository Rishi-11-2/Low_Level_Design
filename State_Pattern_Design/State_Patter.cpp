#include <iostream>
#include <string>
using namespace std;

//===============================
// OrderState class defines the behavior of the order states
//===============================
class OrderState {
public:
    virtual void next(class OrderContext* context) = 0; // Move to the next state
    virtual void cancel(class OrderContext* context) = 0; // Cancel the order
    virtual string getStateName() = 0; // Get the name of the state
};

//===============================
// Forward declaration of the OrderContext class
//===============================
class OrderContext;

//===============================
// Concrete states for each stage of the order
//===============================

// OrderPlacedState handles the behavior when the order is placed
class OrderPlacedState : public OrderState {
public:
    void next(OrderContext* context) override;
    void cancel(OrderContext* context) override;
    string getStateName() override;
};

// PreparingState handles the behavior when the order is being prepared
class PreparingState : public OrderState {
public:
    void next(OrderContext* context) override;
    void cancel(OrderContext* context) override;
    string getStateName() override;
};

// OutForDeliveryState handles the behavior when the order is out for delivery
class OutForDeliveryState : public OrderState {
public:
    void next(OrderContext* context) override;
    void cancel(OrderContext* context) override;
    string getStateName() override;
};

// DeliveredState handles the behavior when the order is delivered
class DeliveredState : public OrderState {
public:
    void next(OrderContext* context) override;
    void cancel(OrderContext* context) override;
    string getStateName() override;
};

// CancelledState handles the behavior when the order is cancelled
class CancelledState : public OrderState {
public:
    void next(OrderContext* context) override;
    void cancel(OrderContext* context) override;
    string getStateName() override;
};

//===============================
// OrderContext class manages the current state of the order
//===============================
class OrderContext {
private:
    OrderState* currentState;

public:
    // Constructor initializes the state to ORDER_PLACED
    OrderContext();

    // Method to set a new state for the order
    void setState(OrderState* state);

    // Method to move the order to the next state
    void next();

    // Method to cancel the order
    void cancel();

    // Method to get the current state of the order
    string getCurrentState();
};

//===============================
// OrderContext constructor initializes the default state to OrderPlacedState
//===============================
OrderContext::OrderContext() {
    currentState = new OrderPlacedState(); // default state
}

// Method to set a new state for the order
void OrderContext::setState(OrderState* state) {
    currentState = state;
}

// Method to move the order to the next state
void OrderContext::next() {
    currentState->next(this);
}

// Method to cancel the order
void OrderContext::cancel() {
    currentState->cancel(this);
}

// Method to get the current state of the order
string OrderContext::getCurrentState() {
    return currentState->getStateName();
}

//===============================
// OrderPlacedState handles the behavior when the order is placed
//===============================
void OrderPlacedState::next(OrderContext* context) {
    context->setState(new PreparingState());
    cout << "Order is now being prepared." << endl;
}

void OrderPlacedState::cancel(OrderContext* context) {
    context->setState(new CancelledState());
    cout << "Order has been cancelled." << endl;
}

string OrderPlacedState::getStateName() {
    return "ORDER_PLACED";
}

//===============================
// PreparingState handles the behavior when the order is being prepared
//===============================
void PreparingState::next(OrderContext* context) {
    context->setState(new OutForDeliveryState());
    cout << "Order is out for delivery." << endl;
}

void PreparingState::cancel(OrderContext* context) {
    context->setState(new CancelledState());
    cout << "Order has been cancelled." << endl;
}

string PreparingState::getStateName() {
    return "PREPARING";
}

//===============================
// OutForDeliveryState handles the behavior when the order is out for delivery
//===============================
void OutForDeliveryState::next(OrderContext* context) {
    context->setState(new DeliveredState());
    cout << "Order has been delivered." << endl;
}

void OutForDeliveryState::cancel(OrderContext* context) {
    cout << "Cannot cancel. Order is out for delivery." << endl;
}

string OutForDeliveryState::getStateName() {
    return "OUT_FOR_DELIVERY";
}

//===============================
// DeliveredState handles the behavior when the order is delivered
//===============================
void DeliveredState::next(OrderContext* context) {
    cout << "Order is already delivered." << endl;
}

void DeliveredState::cancel(OrderContext* context) {
    cout << "Cannot cancel a delivered order." << endl;
}

string DeliveredState::getStateName() {
    return "DELIVERED";
}

//===============================
// CancelledState handles the behavior when the order is cancelled
//===============================
void CancelledState::next(OrderContext* context) {
    cout << "Cancelled order cannot move to next state." << endl;
}

void CancelledState::cancel(OrderContext* context) {
    cout << "Order is already cancelled." << endl;
}

string CancelledState::getStateName() {
    return "CANCELLED";
}

//===============================
// Main function to demonstrate the state transitions
//===============================
int main() {
    OrderContext order;

    // Display initial state
    cout << "Current State: " << order.getCurrentState() << endl;

    // Moving through states
    order.next();  // ORDER_PLACED -> PREPARING
    order.next();  // PREPARING -> OUT_FOR_DELIVERY
    order.cancel(); // Should fail, as order is out for delivery
    order.next();  // OUT_FOR_DELIVERY -> DELIVERED
    order.cancel(); // Should fail, as order is delivered

    // Display final state
    cout << "Final State: " << order.getCurrentState() << endl;

    return 0;
}
