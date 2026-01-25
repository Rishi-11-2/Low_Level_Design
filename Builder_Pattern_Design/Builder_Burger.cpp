#include <bits/stdc++.h>
using namespace std;

// Represents a customizable Burger Meal
class BurgerMeal {
private:
    // Required components
    string bunType;
    string patty;

    // Optional components
    bool hasCheese;
    vector<string> toppings;
    string side;
    string drink;

public:
    // Static nested Builder class
    class BurgerBuilder {
    private:
        // Required
        string bunType;
        string patty;

        // Optional
        bool hasCheese = false;
        vector<string> toppings;
        string side = "";
        string drink = "";

        // Give BurgerMeal access to private members
        friend class BurgerMeal;

    public:
        // Builder constructor with required fields
        BurgerBuilder(string bunType, string patty)
            : bunType(bunType), patty(patty) {}

        // Method to set cheese
        BurgerBuilder& withCheese(bool hasCheese) {
            this->hasCheese = hasCheese;
            return *this;
        }

        // Method to set toppings
        BurgerBuilder& withToppings(vector<string> toppings) {
            this->toppings = toppings;
            return *this;
        }

        // Method to set side
        BurgerBuilder& withSide(string side) {
            this->side = side;
            return *this;
        }

        // Method to set drink
        BurgerBuilder& withDrink(string drink) {
            this->drink = drink;
            return *this;
        }

        // Final build method
        BurgerMeal build() {
            return BurgerMeal(*this);
        }
    };

    // Private constructor to force use of Builder
    BurgerMeal(const BurgerBuilder& builder) {
        // we declared friend above so that BurgerMeal can access the private variables of BurgerBuilder
        bunType = builder.bunType;
        patty = builder.patty;
        hasCheese = builder.hasCheese;
        toppings = builder.toppings;
        side = builder.side;
        drink = builder.drink;
    }
};

int main() {
    // Creating burger with only required fields
    BurgerMeal plainBurger = BurgerMeal::BurgerBuilder("wheat", "veg").build();

    // Burger with cheese only
    BurgerMeal burgerWithCheese = BurgerMeal::BurgerBuilder("wheat", "veg")
                                        .withCheese(true)
                                        .build();

    // Fully loaded burger
    vector<string> toppings = {"lettuce", "onion", "jalapeno"};
    BurgerMeal loadedBurger = BurgerMeal::BurgerBuilder("multigrain", "chicken")
                                    .withCheese(true)
                                    .withToppings(toppings)
                                    .withSide("fries")
                                    .withDrink("coke")
                                    .build();

    return 0;
}
