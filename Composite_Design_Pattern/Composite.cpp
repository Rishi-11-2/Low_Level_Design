#include <bits/stdc++.h>
using namespace std;

// Interface for items that can be added to the cart
class CartItem {
public:
    virtual double getPrice() const = 0;
    virtual void display(const string& indent) const = 0;
    virtual ~CartItem() {}
};

// Product class implementing CartItem
class Product : public CartItem {
private:
    string name;
    double price;

public:
    Product(const string& name, double price) : name(name), price(price) {}

    double getPrice() const override {
        return price;
    }

    void display(const string& indent) const override {
        cout << indent << "Product: " << name << " – ₹" << price << endl;
    }
};

// ProductBundle class implementing CartItem
class ProductBundle : public CartItem {
private:
    string bundleName;
    vector<CartItem*> items;

public:
    ProductBundle(const string& name) : bundleName(name) {}

    void addItem(CartItem* item) {
        items.push_back(item);
    }

    double getPrice() const override {
        double total = 0;
        for (const auto& item : items) {
            total += item->getPrice();
        }
        return total;
    }

    void display(const string& indent) const override {
        cout << indent << "Bundle: " << bundleName << endl;
        for (const auto& item : items) {
            item->display(indent + "  ");
        }
    }

    ~ProductBundle() {
        for (auto& item : items) {
            delete item;
        }
    }
};

// Main logic
int main() {
    // Individual Products
    CartItem* book = new Product("Atomic Habits", 499);
    CartItem* phone = new Product("iPhone 15", 79999);
    CartItem* earbuds = new Product("AirPods", 15999);
    CartItem* charger = new Product("20W Charger", 1999);

    // Combo Deal
    ProductBundle* iphoneCombo = new ProductBundle("iPhone Essentials Combo");
    iphoneCombo->addItem(phone);
    iphoneCombo->addItem(earbuds);
    iphoneCombo->addItem(charger);

    // Back to School Kit
    ProductBundle* schoolKit = new ProductBundle("Back to School Kit");
    schoolKit->addItem(new Product("Notebook Pack", 249));
    schoolKit->addItem(new Product("Pen Set", 99));
    schoolKit->addItem(new Product("Highlighter", 149));

    // Add everything to cart
    vector<CartItem*> cart;
    cart.push_back(book);
    cart.push_back(iphoneCombo);
    cart.push_back(schoolKit);

    // Display cart
    cout << "Your Amazon Cart:" << endl;
    double total = 0;
    for (const auto& item : cart) {
        item->display("  ");
        total += item->getPrice();
    }

    cout << "\nTotal: ₹" << total << endl;

    // Cleanup
    for (auto& item : cart) {
        delete item;
    }

    return 0;
}
