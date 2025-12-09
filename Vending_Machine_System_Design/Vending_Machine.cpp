#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <stdexcept>

using namespace std;

enum class ItemType {
    COKE,
    PEPSI,
    SODA,
    JUICE
};

class Coin {
public:
    int value;
    Coin(int value) : value(value) {}
};

class Item {
private:
    ItemType type;
    int price;
public:
    Item(ItemType type, int price) : type(type), price(price) {}
    ItemType getType() const { return type; }
    int getPrice() const { return price; }
};

class ItemShelf {
private:
    int code;
    int quantity;
    Item* item;
public:
    ItemShelf(int code, int quantity, Item* item) : code(code), quantity(quantity), item(item) {}
    ~ItemShelf() { delete item; }
    int getCode() const { return code; }
    int getQuantity() const { return quantity; }
    Item* getItem() const { return item; }
    void decreaseQuantity() { if (quantity > 0) --quantity; }
};

class Inventory {
public:
    vector<ItemShelf*> v;
    int num;
    Inventory(int num) : num(num) {}
    ~Inventory() {
        for (auto s : v) delete s;
    }

    void init() {
        srand(static_cast<unsigned>(time(nullptr)));
        int shelfCode = 101;
        for (int i = 0; i < num; ++i) {
            ItemType t = static_cast<ItemType>(rand() % 4);
            int price = 20 + rand() % 81; // 20..100
            int qty = rand() % 11;        // 0..10
            Item* item = new Item(t, price);
            ItemShelf* shelf = new ItemShelf(shelfCode++, qty, item);
            v.push_back(shelf);
        }
    }

    ItemShelf* getShelfByIndex(size_t idx) const {
        if (idx >= v.size()) return nullptr;
        return v[idx];
    }
};

// forward declaration required for circular dependency
class State;

class VendingMachine {
public:
    Inventory* inventory = nullptr;   // not owned
    State* state = nullptr;           // states allocated with new (ownership policy not handled here)
    vector<Coin*> coinList;           // owned coins

    VendingMachine(Inventory* inv) : inventory(inv), state(nullptr) {}
    ~VendingMachine() {
        for (auto c : coinList) delete c;
        // states are not deleted here for simplicity (could be managed with unique_ptr)
    }

    void setVendingMachineState(State* s) { state = s; }

    Inventory* getInventory() const { return inventory; }
    vector<Coin*>& getCoinList() { return coinList; }

    vector<Coin*> refundAllCoins() {
        vector<Coin*> ret = coinList;
        coinList.clear();
        return ret;
    }
};

// State interface
class State {
public:
    virtual ~State() = default;
    virtual void clickOnInsertCoinButton(VendingMachine* machine) = 0;
    virtual void clickOnStartProductSelectionButton(VendingMachine* machine) = 0;
    virtual void insertCoin(VendingMachine* machine, Coin* coin) = 0;
    virtual void chooseProduct(VendingMachine* machine, int codeNumber) = 0;
    virtual int getChange(int returnChangeMoney) = 0;
    virtual Item* dispenseProduct(VendingMachine* machine, int codeNumber) = 0;
    virtual vector<Coin*> refundFullMoney(VendingMachine* machine) = 0;
};

// Declare state classes; define methods that allocate other states only after all classes exist.

class IdleState : public State {
public:
    IdleState() { cout << "Currently Vending Machine is in idle state\n"; }
    void clickOnInsertCoinButton(VendingMachine* machine) override; // implement later
    void clickOnStartProductSelectionButton(VendingMachine* /*machine*/) override {
        throw runtime_error("No money inserted");
    }
    void insertCoin(VendingMachine* /*machine*/, Coin* /*coin*/) override {
        throw runtime_error("Cannot insert coin in idle state");
    }
    void chooseProduct(VendingMachine* /*machine*/, int /*codeNumber*/) override {
        throw runtime_error("No product selection in idle state");
    }
    int getChange(int /*returnChangeMoney*/) override {
        throw runtime_error("No change in idle state");
    }
    Item* dispenseProduct(VendingMachine* /*machine*/, int /*codeNumber*/) override {
        throw runtime_error("No dispense in idle state");
    }
    vector<Coin*> refundFullMoney(VendingMachine* /*machine*/) override {
        throw runtime_error("Nothing to refund");
    }
};

class HasMoneyState : public State {
public:
    HasMoneyState() { cout << "Currently Vending Machine is in hasMoney state\n"; }
    void clickOnInsertCoinButton(VendingMachine* /*machine*/) override { /* already in has-money */ }
    void clickOnStartProductSelectionButton(VendingMachine* machine) override; // implement later
    void insertCoin(VendingMachine* machine, Coin* coin) override {
        cout << "Inserted the coin\n";
        machine->getCoinList().push_back(coin);
    }
    void chooseProduct(VendingMachine* /*machine*/, int /*codeNumber*/) override {
        throw runtime_error("Start selection first");
    }
    int getChange(int /*returnChangeMoney*/) override {
        throw runtime_error("Not applicable in HasMoneyState");
    }
    Item* dispenseProduct(VendingMachine* /*machine*/, int /*codeNumber*/) override {
        throw runtime_error("Not applicable in HasMoneyState");
    }
    vector<Coin*> refundFullMoney(VendingMachine* machine) override {
        return machine->refundAllCoins();
    }
};

class SelectionState : public State {
public:
    SelectionState() { cout << "Vending machine is currently in selection state\n"; }
    void clickOnInsertCoinButton(VendingMachine* /*machine*/) override { /* ignore */ }
    void clickOnStartProductSelectionButton(VendingMachine* /*machine*/) override { /* already selecting */ }
    void insertCoin(VendingMachine* /*machine*/, Coin* /*coin*/) override {
        throw runtime_error("Cannot insert during selection");
    }

    void chooseProduct(VendingMachine* machine, int codeNumber) override {
        ItemShelf* shelf = machine->getInventory()->getShelfByIndex(static_cast<size_t>(codeNumber));
        if (!shelf) throw runtime_error("Invalid code/index");
        Item* item = shelf->getItem();

        int amount = 0;
        for (auto c : machine->getCoinList()) amount += c->value;

        if (amount < item->getPrice()) {
            cout << "Insufficient Amount:"<<item->getPrice()-amount<<endl;
            refundFullMoney(machine);
            throw runtime_error("Insufficient Amount");
        }

        if (amount > item->getPrice()) {
            int changeVal = getChange(amount - item->getPrice());
            cout << "Change to return: " << changeVal << "\n";
        }

        Item* dispensed = dispenseProduct(machine, codeNumber);
        (void)dispensed;

        machine->setVendingMachineState(new IdleState());
    }

    int getChange(int returnChangeMoney) override {
        cout << "Return extra amount: " << returnChangeMoney << "\n";
        return returnChangeMoney;
    }

    Item* dispenseProduct(VendingMachine* machine, int codeNumber) override {
        ItemShelf* shelf = machine->getInventory()->getShelfByIndex(static_cast<size_t>(codeNumber));
        if (!shelf) throw runtime_error("Invalid shelf on dispense");
        if (shelf->getQuantity() <= 0) {
            cout << "Sold out\n";
            refundFullMoney(machine);
            throw runtime_error("Sold out");
        }
        cout << "Dispensing product with index: " << codeNumber << "\n";
        shelf->decreaseQuantity();
        return shelf->getItem();
    }

    vector<Coin*> refundFullMoney(VendingMachine* machine) override {
        machine->setVendingMachineState(new IdleState());
        return machine->refundAllCoins();
    }
};

// Now that HasMoneyState and SelectionState are complete, implement the previously-declared methods.

void IdleState::clickOnInsertCoinButton(VendingMachine* machine) {
    // allocate HasMoneyState (type is complete now)
    machine->setVendingMachineState(new HasMoneyState());
}

void HasMoneyState::clickOnStartProductSelectionButton(VendingMachine* machine) {
    // allocate SelectionState (type is complete now)
    machine->setVendingMachineState(new SelectionState());
}

// main driver

int main() {
    Inventory inv(5);
    inv.init();

    VendingMachine vm(&inv);
    vm.setVendingMachineState(new IdleState());

    try {
        vm.state->clickOnInsertCoinButton(&vm);          // Idle -> HasMoney
        vm.state->insertCoin(&vm, new Coin(50));         // insert a coin
        vm.state->clickOnStartProductSelectionButton(&vm); // HasMoney -> Selection
        vm.state->chooseProduct(&vm, 4);                 // choose product at index 0
    } catch (const exception& e) {
        cerr << "Error: " << e.what() << '\n';
    }

    // cleanup remaining coins (if any) owned by machine
    for (auto c : vm.getCoinList()) delete c;
    vm.getCoinList().clear();

    // Note: state objects allocated with new are not deleted here.
    // Add deletion or use smart pointers if you want to avoid leaks.

    return 0;
}
