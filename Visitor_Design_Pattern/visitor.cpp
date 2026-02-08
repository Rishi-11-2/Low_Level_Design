#include <iostream>
#include <vector>
#include <string>

using namespace std;

// ======= Element Interface ==========
class Item {
public:
    virtual void accept(class ItemVisitor* visitor) = 0;
    virtual ~Item() {}
};

// ======= Concrete elements ===========
class PhysicalProduct : public Item {
public:
    string name;
    double weight;

    PhysicalProduct(string name, double weight) : name(name), weight(weight) {}

    void accept(ItemVisitor* visitor) override;
};

// ======= Concrete elements ===========
class DigitalProduct : public Item {
public:
    string name;
    int downloadSizeInMB;

    DigitalProduct(string name, int downloadSizeInMB) : name(name), downloadSizeInMB(downloadSizeInMB) {}

    void accept(ItemVisitor* visitor) override;
};

// ======= Concrete elements ===========
class GiftCard : public Item {
public:
    string code;
    double amount;

    GiftCard(string code, double amount) : code(code), amount(amount) {}

    void accept(ItemVisitor* visitor) override;
};

// ======== Visitor Interface ============
class ItemVisitor {
public:
    virtual void visit(PhysicalProduct* item) = 0;
    virtual void visit(DigitalProduct* item) = 0;
    virtual void visit(GiftCard* item) = 0;
    virtual ~ItemVisitor() {}
};

// ============ Concrete Visitors ==============
class InvoiceVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct* item) override {
        cout << "Invoice: " << item->name << " - Shipping to customer" << endl;
    }

    void visit(DigitalProduct* item) override {
        cout << "Invoice: " << item->name << " - Email with download link" << endl;
    }

    void visit(GiftCard* item) override {
        cout << "Invoice: Gift Card - Code: " << item->code << endl;
    }
};

// ============ Concrete Visitors ==============
class ShippingCostVisitor : public ItemVisitor {
public:
    void visit(PhysicalProduct* item) override {
        cout << "Shipping cost for " << item->name << ": Rs. " << (item->weight * 10) << endl;
    }

    void visit(DigitalProduct* item) override {
        cout << item->name << " is digital -- No shipping cost." << endl;
    }

    void visit(GiftCard* item) override {
        cout << "GiftCard delivery via email -- No shipping cost." << endl;
    }
};

// ========= Concrete element accept methods ============
void PhysicalProduct::accept(ItemVisitor* visitor) {
    visitor->visit(this);
}

void DigitalProduct::accept(ItemVisitor* visitor) {
    visitor->visit(this);
}

void GiftCard::accept(ItemVisitor* visitor) {
    visitor->visit(this);
}

// Client Code
int main() {
    vector<Item*> items;
    items.push_back(new PhysicalProduct("Shoes", 1.2));
    items.push_back(new DigitalProduct("Ebook", 100));
    items.push_back(new GiftCard("TUF500", 500));

    ItemVisitor* invoiceGenerator = new InvoiceVisitor();
    ItemVisitor* shippingCalculator = new ShippingCostVisitor();

    for (auto item : items) {
        item->accept(invoiceGenerator);
        item->accept(shippingCalculator);
        cout << endl;
    }

    // Clean up
    for (auto item : items) {
        delete item;
    }

    delete invoiceGenerator;
    delete shippingCalculator;

    return 0;
}
