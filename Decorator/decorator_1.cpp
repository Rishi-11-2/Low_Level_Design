#include <bits/stdc++.h>
using namespace std;

// =========== Component Interface ============
class Pizza {
public:
    virtual string getDescription() = 0;
    virtual double getCost() = 0;
    virtual ~Pizza() = default;
};

// ============= Concrete Components: Base pizza ==============
class PlainPizza : public Pizza {
public:
    string getDescription() override {
        return "Plain Pizza";
    }

    double getCost() override {
        return 150.00;
    }
};

class MargheritaPizza : public Pizza {
public:
    string getDescription() override {
        return "Margherita Pizza";
    }

    double getCost() override {
        return 200.00;
    }
};

// ======================== Abstract Decorator ===========================
// ====== Implements Pizza and holds a reference to a Pizza object =======
class PizzaDecorator : public Pizza {
protected:
    Pizza* pizza;
public:
    PizzaDecorator(Pizza* pizza) : pizza(pizza) {}
    virtual ~PizzaDecorator() { delete pizza; }
};

// ============ Concrete Decorator: Adds Extra Cheese ================
class ExtraCheese : public PizzaDecorator {
public:
    ExtraCheese(Pizza* pizza) : PizzaDecorator(pizza) {}

    string getDescription() override {
        return pizza->getDescription() + ", Extra Cheese";
    }

    double getCost() override {
        return pizza->getCost() + 40.0;
    }
};

// ============ Concrete Decorator: Adds Olives ================
class Olives : public PizzaDecorator {
public:
    Olives(Pizza* pizza) : PizzaDecorator(pizza) {}

    string getDescription() override {
        return pizza->getDescription() + ", Olives";
    }

    double getCost() override {
        return pizza->getCost() + 30.0;
    }
};

// =========== Concrete Decorator: Adds Stuffed Crust Cheese =============
class StuffedCrust : public PizzaDecorator {
public:
    StuffedCrust(Pizza* pizza) : PizzaDecorator(pizza) {}

    string getDescription() override {
        return pizza->getDescription() + ", Stuffed Crust";
    }

    double getCost() override {
        return pizza->getCost() + 50.0;
    }
};


// Driver code
int main() {
    // Start with a basic Margherita Pizza
    Pizza* myPizza = new MargheritaPizza();

    // Add Extra Cheese
    myPizza = new ExtraCheese(myPizza);

    // Add Olives
    myPizza = new Olives(myPizza);

    // Add Stuffed Crust
    myPizza = new StuffedCrust(myPizza);

    // Final Description and Cost
    cout << "Pizza Description: " << myPizza->getDescription() << endl;
    cout << "Total Cost: ₹" << myPizza->getCost() << endl;

    delete myPizza;
    return 0;
}
