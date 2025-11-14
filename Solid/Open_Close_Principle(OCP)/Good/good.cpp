#include <iostream>
using namespace std;

class PaymentMethod {
public:
    virtual void pay(double amount) = 0;
    virtual ~PaymentMethod() {}
};

class CreditCard : public PaymentMethod {
public:
    void pay(double amount) override {
        cout << "Paying using Credit card: " << amount << endl;
    }
};

class DebitCard : public PaymentMethod {
public:
    void pay(double amount) override {
        cout << "Paying using Debit card: " << amount << endl;
    }
};

class PayPal : public PaymentMethod {
public:
    void pay(double amount) override {
        cout << "Paying using PayPal: " << amount << endl;
    }
};

class UPI : public  PaymentMethod{

    public:
    void pay(double amount) override {
        cout<<"Paying using UPI: "<<amount<<endl;
    }
};

class PaymentProcessor {
public:
    void processPayment(PaymentMethod* paymentMethod, double amount) {
        paymentMethod->pay(amount);
    }
};

int main() {
    // PaymentMethod class follows OCP 
    // as a result of OCP, we can add new payment methods without modifying the existing code
    CreditCard* c = new CreditCard();
    DebitCard d;
    UPI upi;
    PaymentProcessor p;
    p.processPayment(c, 100.00); // run time polymorphism
    p.processPayment(&d, 129.00); // run time polymorphism
    p.processPayment(&upi, 300.00); // run time polymorphism
    delete c; 
    return 0;
}
