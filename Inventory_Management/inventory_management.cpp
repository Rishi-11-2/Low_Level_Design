#include <iostream>
#include <vector>
#include <unordered_map>
#include <set>
#include <string>
#include <algorithm>

using namespace std;

class Order;

enum class OrderStatus {
    INPROGRESS,
    DELIVERED,
    CANCELLED
};

class Product {
public:
    int productId;
    string name;
    int price;

    Product(int id = 0, const string& n = "", int p = 0)
        : productId(id), name(n), price(p) {}
};

class ProductCategory {
public:
    int categoryId;
    vector<Product*> products;
    string categoryName;
    double price; // optional aggregated price or other usage

    ProductCategory(int id = 0, const string& name = "", double pr = 0.0)
        : categoryId(id), categoryName(name), price(pr) {}

    // add a product pointer to this category
    void addProductToTheCategory(Product* p) {
        if (!p) return;
        // avoid duplicates by productId
        auto it = find_if(products.begin(), products.end(),
                          [&](Product* ex){ return ex && ex->productId == p->productId; });
        if (it == products.end()) products.push_back(p);
    }

    // remove a product pointer from this category by pointer match or productId
    void removeProductFromTheCategory(Product* p) {
        if (!p) return;
        products.erase(remove_if(products.begin(), products.end(),
                         [&](Product* ex){ return !ex || ex->productId == p->productId; }),
                       products.end());
    }

    // convenience: remove by productId
    void removeProductFromTheCategory(int productId) {
        products.erase(remove_if(products.begin(), products.end(),
                         [&](Product* ex){ return !ex || ex->productId == productId; }),
                       products.end());
    }
};

class Inventory {
public:
    vector<ProductCategory*> categories;

    // add a category pointer
    void addProductCategory(ProductCategory* cat) {
        if (!cat) return;
        auto it = find_if(categories.begin(), categories.end(),
                          [&](ProductCategory* c){ return c && c->categoryId == cat->categoryId; });
        if (it == categories.end()) categories.push_back(cat);
    }

    // remove category by pointer
    void removeProductCategory(ProductCategory* cat) {
        if (!cat) return;
        categories.erase(remove_if(categories.begin(), categories.end(),
                         [&](ProductCategory* c){ return !c || c->categoryId == cat->categoryId; }),
                       categories.end());
    }

    // remove category by id
    void removeProductCategory(int categoryId) {
        categories.erase(remove_if(categories.begin(), categories.end(),
                         [&](ProductCategory* c){ return !c || c->categoryId == categoryId; }),
                       categories.end());
    }

    // find category by id (returns nullptr if not found)
    ProductCategory* findCategory(int categoryId) {
        for (auto c : categories) if (c && c->categoryId == categoryId) return c;
        return nullptr;
    }
};

class Warehouse {
public:
    Inventory* inventory;
    string address;

    Warehouse(Inventory* inv = nullptr, const string& addr = "")
        : inventory(inv), address(addr) {}

    // add item to inventory into matching category; if category not found, no-op
    void addItemToInventory(Product* product, int categoryId) {
        if (!inventory || !product) return;
        ProductCategory* cat = inventory->findCategory(categoryId);
        if (cat) cat->addProductToTheCategory(product);
    }

    // remove item from any category that contains it
    void removeItemsFromInventory(int productId) {
        if (!inventory) return;
        for (auto cat : inventory->categories) {
            if (!cat) continue;
            cat->removeProductFromTheCategory(productId);
        }
    }

    // convenience: check whether product exists (by id)
    bool hasProduct(int productId) {
        if (!inventory) return false;
        for (auto cat : inventory->categories) {
            if (!cat) continue;
            for (auto p : cat->products) if (p && p->productId == productId) return true;
        }
        return false;
    }
};

class WarehouseSelectionStrategy {
public:
    virtual Warehouse* selectWarehouse(const vector<Warehouse*>& warehouses, int productId = -1) = 0;
    virtual ~WarehouseSelectionStrategy() = default;
};

class NearestWarehouseSelectionStrategy : public WarehouseSelectionStrategy {
public:
    // naive: return first warehouse that contains the product, else first warehouse
    Warehouse* selectWarehouse(const vector<Warehouse*>& warehouses, int productId = -1) override {
        if (productId != -1) {
            for (auto w : warehouses) if (w && w->hasProduct(productId)) return w;
        }
        return warehouses.empty() ? nullptr : warehouses.front();
    }
};

class CompleteWarehouseSelectionStrategy : public WarehouseSelectionStrategy {
public:
    // naive: return warehouse that has all products (not implemented here) - fallback to first
    Warehouse* selectWarehouse(const vector<Warehouse*>& warehouses, int productId = -1) override {
        // For this simplified design, return last warehouse (could represent 'complete' coverage)
        if (warehouses.empty()) return nullptr;
        return warehouses.back();
    }
};

class WarehouseController {
public:
    vector<Warehouse*> warehouses;
    WarehouseSelectionStrategy* strategy = nullptr;

    void addWarehouse(Warehouse* w) {
        if (!w) return;
        auto it = find(warehouses.begin(), warehouses.end(), w);
        if (it == warehouses.end()) warehouses.push_back(w);
    }

    void removeWarehouse(Warehouse* w) {
        if (!w) return;
        warehouses.erase(remove(warehouses.begin(), warehouses.end(), w), warehouses.end());
    }

    // select warehouse using the set strategy; if none, return first
    Warehouse* selectWarehouse(int productId = -1) {
        if (strategy) return strategy->selectWarehouse(warehouses, productId);
        return warehouses.empty() ? nullptr : warehouses.front();
    }
};

class Cart {
public:
    unordered_map<int,int> productVsCount;

    void addItemsToCart(int productId) {
        productVsCount[productId]++;
    }

    void removeItemsFromCart(int productId) {
        auto it = productVsCount.find(productId);
        if (it == productVsCount.end()) return;
        it->second--;
        if (it->second <= 0) productVsCount.erase(it);
    }

    void viewCart() {
        if (productVsCount.empty()) {
            cout << "Cart is empty\n";
            return;
        }
        for (const auto &it : productVsCount) {
            cout << "Count of Product id: " << it.first << " is " << it.second << endl;
        }
    }

    void clearCart() {
        productVsCount.clear();
    }

    bool isEmpty() const {
        return productVsCount.empty();
    }
};

class User {
public:
    int id;
    string name;
    Cart* cart;
    vector<Order*> orderHistory;

    User(int uid = 0, const string& uname = "")
        : id(uid), name(uname), cart(new Cart()) {}

    ~User() { delete cart; }
};

class UserController {
public:
    set<User*> users;

    void addUser(User* user) {
        if (!user) return;
        users.insert(user);
    }

    void removeUser(User* user) {
        if (!user) return;
        users.erase(user);
    }

    User* findUserById(int uid) {
        for (auto u : users) if (u && u->id == uid) return u;
        return nullptr;
    }
};

class PaymentMode {
public:
    virtual void pay(int amount) = 0;
    virtual ~PaymentMode() = default;
};

class CreditCard : public PaymentMode {
public:
    void pay(int amount) override {
        cout << "Paid " << amount << " using Credit Card\n";
    }
};

class UPI : public PaymentMode {
public:
    void pay(int amount) override {
        cout << "Paid " << amount << " using UPI\n";
    }
};

class Payment {
public:
    PaymentMode* mode = nullptr;
    int amount = 0;

    Payment(PaymentMode* m = nullptr, int a = 0) : mode(m), amount(a) {}

    void doPay() {
        if (mode) mode->pay(amount);
    }
};

class Invoice {
public:
    int invoiceId = 0;
    int totalAmount = 0;
    Invoice(int id = 0, int total = 0) : invoiceId(id), totalAmount(total) {}
};

class Order {
public:
    User* user = nullptr;
    Cart* cart = nullptr;
    Warehouse* warehouseFulfilled = nullptr;
    Invoice* invoice = nullptr;
    Payment* payment = nullptr;
    OrderStatus status = OrderStatus::INPROGRESS;
    int orderId = 0;

    Order(int id = 0) : orderId(id) {}
};

class OrderController {
public:
    set<Order*> listOfOrders;

    void addOrder(Order* o) {
        if (!o) return;
        listOfOrders.insert(o);
    }

    void removeOrder(Order* o) {
        if (!o) return;
        listOfOrders.erase(o);
    }

    Order* findOrderById(int id) {
        for (auto o : listOfOrders) if (o && o->orderId == id) return o;
        return nullptr;
    }
};

class App {
public:
    UserController* uc;
    WarehouseController* wc;
    OrderController* oc;

    App() {
        uc = new UserController();
        wc = new WarehouseController();
        oc = new OrderController();
    }

    ~App() {
        delete uc;
        delete wc;
        delete oc;
    }
};

// Simple demonstration
int main() {
    App app;

    // create products
    Product* p1 = new Product(1, "Phone", 20000);
    Product* p2 = new Product(2, "Charger", 500);

    // categories
    ProductCategory* cat1 = new ProductCategory(10, "Electronics");
    ProductCategory* cat2 = new ProductCategory(20, "Accessories");

    // inventory and warehouse
    Inventory* inv1 = new Inventory();
    inv1->addProductCategory(cat1);
    inv1->addProductCategory(cat2);

    Warehouse* w1 = new Warehouse(inv1, "Addr-1");
    w1->addItemToInventory(p1, 10);
    w1->addItemToInventory(p2, 20);

    app.wc->addWarehouse(w1);
    NearestWarehouseSelectionStrategy nstrat;
    app.wc->strategy = &nstrat;

    // user
    User* u1 = new User(101, "Rishi");
    app.uc->addUser(u1);

    // add items to cart
    u1->cart->addItemsToCart(1);
    u1->cart->addItemsToCart(2);
    u1->cart->viewCart();

    // create order
    Order* ord = new Order(5001);
    ord->user = u1;
    ord->cart = u1->cart;
    ord->warehouseFulfilled = app.wc->selectWarehouse(1); // select warehouse that has product 1
    ord->invoice = new Invoice(9001, p1->price + p2->price);
    PaymentMode* pm = new UPI();
    ord->payment = new Payment(pm, ord->invoice->totalAmount);

    app.oc->addOrder(ord);

    // perform payment
    ord->payment->doPay();
    ord->status = OrderStatus::DELIVERED;

    cout << "Order " << ord->orderId << " status: " << (ord->status == OrderStatus::DELIVERED ? "DELIVERED" : "OTHER") << endl;

    // cleanup (simple)
    delete ord->invoice;
    delete ord->payment;
    delete pm;
    delete ord;
    delete u1;
    delete p1;
    delete p2;
    delete cat1;
    delete cat2;
    delete inv1;
    delete w1;

    return 0;
}
