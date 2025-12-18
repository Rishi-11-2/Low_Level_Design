#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <map>
#include<cmath>


using namespace std;

enum class SplitType {
    EQUAL,
    UNEQUAL,
    PERCENTAGE
};

/* ---------- Forward Decls ---------- */
class BalanceSheet;
class User;




/* ---------- Balance Sheet ---------- */

class BalanceSheet {
    public:
    int userId;
    map<User*,double>details;

    double totalPayment;
    double totalYourExpense;
    double totalYouOwe;
    double totalYouGetBack;

    BalanceSheet(int userId):userId(userId)
    {
        totalPayment  = 0;
        totalYouGetBack = 0;
        totalYouOwe = 0;
        totalYourExpense = 0;
    }

    void add(User* u,double amount)
    {
        if(!details.count(u))
        details[u]=amount;
        else
        details[u]+=amount;
    }


    void addPayment(double amount)
    {
        totalPayment+=amount;
    }

    void addExpense(double amount)
    {
        totalYourExpense+=amount;
    }

    void displayTotalYouOwe();
    void displayTotalYouGetBack();
};






/* ---------- User ---------- */



class User {
public:
    int id;
    string name;
    vector<int> friends;
    BalanceSheet* bs;

    User(int id, string name) : id(id), name(name) {
        bs = new BalanceSheet(id);
    }

    ~User()
    {
        delete bs;
    }
};


void BalanceSheet::displayTotalYouOwe()
{

    totalYouOwe=0;
    
    for(auto it:details)
    {
        if(it.second<0)
        {
            totalYouOwe+=it.second;
            cout<<"You owe "<<it.first->name<< "  :"<<abs(it.second)<<endl;
        }
    }
    cout<<"Total You owe: "<<abs(totalYouOwe)<<endl;
}

void BalanceSheet::displayTotalYouGetBack()
{
    totalYouGetBack = 0;
    for(auto it:details)
    {
        if(it.second>0)
        {
            totalYouGetBack +=it.second;
            cout<<"You will get back from  "<<it.first->name<< "  :"<<abs(it.second)<<endl;
        }
    }
    cout<<"Total You will Get back: "<<totalYouGetBack<<endl;
}



/* ---------- UserController ---------- */
class UserController {
public:
    set<User*> users;

    void createUser(int id, string name) {
        for (auto u : users)
            if (u->id == id) return; // avoid duplicate id

        users.insert(new User(id, name));
    }

    void removeUser(int id) {
        for (auto it = users.begin(); it != users.end(); ++it) {
            if ((*it)->id == id) {
                delete *it;
                users.erase(it);
                return;
            }
        }
    }

    ~UserController() {
        for (auto u : users)
            delete u;
    }
};

/* ---------- Split ---------- */
class Split {
public:
    map<User*, double> details;

    void addDetails(User* u, double amount) {
        details[u] += amount;
    }
};

/* ---------- Expense ---------- */
class Expense {
public:
    int expenseId;
    string description;
    double amount;
    User* paidByUser;
    SplitType type;
    Split* split;

    Expense(int id, string desc, double amt, User* paidBy, SplitType t, Split* s)
        : expenseId(id), description(desc), amount(amt),
          paidByUser(paidBy), type(t), split(s) {}
};

/* ---------- ExpenseSplit Strategy ---------- */
class ExpenseSplit {
public:
    virtual map<User*, double> process(double amount, User* paidByUser, Split* split) = 0;
    virtual ~ExpenseSplit() {}
};

/* ---------- Equal ---------- */
class EqualExpenseSplit : public ExpenseSplit {
public:
    map<User*, double> process(double amount, User* paidByUser, Split* split) override {
        map<User*, double> res;

        int n = split->details.size() + 1;
        double share = amount / n;

        res[paidByUser] += share;
        for (auto& p : split->details)
            res[p.first] += share;

        return res;
    }
};

/* ---------- Unequal ---------- */
class UnEqualExpenseSplit : public ExpenseSplit {
public:
    map<User*, double> process(double amount, User* paidByUser, Split* split) override {
        map<User*, double> res;
        double remaining = amount;

        for (auto& p : split->details) {
            res[p.first] += p.second;
            remaining -= p.second;
        }

        res[paidByUser] += remaining;
        return res;
    }
};

/* ---------- Percentage ---------- */
class PercentageExpenseSplit : public ExpenseSplit {
public:
    map<User*, double> process(double amount, User* paidByUser, Split* split) override {
        map<User*, double> res;
        double remaining = amount;

        for (auto& p : split->details) {
            double val = amount * p.second * 0.01;
            res[p.first] += val;
            remaining -= val;
        }

        res[paidByUser] += remaining;
        return res;
    }
};

/* ---------- Factory ---------- */
class SplitFactory {
public:
    ExpenseSplit* getSplitObject(SplitType type) {
        if (type == SplitType::EQUAL) return new EqualExpenseSplit();
        if (type == SplitType::UNEQUAL) return new UnEqualExpenseSplit();
        if (type == SplitType::PERCENTAGE) return new PercentageExpenseSplit();
        return nullptr;
    }
};

/* ---------- ExpenseController ---------- */
class ExpenseController {
public:
    vector<Expense*> expenses;
    SplitFactory* factory;

    ExpenseController() {
        factory = new SplitFactory();
    }

    void createExpense(int id, string desc, double amount,
                       Split* split, SplitType type, User* paidBy) {

        Expense* e = new Expense(id, desc, amount, paidBy, type, split);
        expenses.push_back(e);

        ExpenseSplit* es = factory->getSplitObject(type);
        auto result = es->process(amount, paidBy, split);

        paidBy->bs->addPayment(amount);
        for(auto it:result)
        {
            if(paidBy->name!=it.first->name)
            {
                paidBy->bs->add(it.first,it.second);
                it.first->bs->add(paidBy,-it.second);
            }
            else
            {
                paidBy->bs->addExpense(it.second);
            }
        }
        delete es;
    }

    ~ExpenseController() {
        for (auto e : expenses) {
            delete e->split;
            delete e;
        }
        delete factory;
    }
};

/* ---------- Group ---------- */
class Group {
public:
    int id;
    string name;
    set<User*> members;
    ExpenseController* ec;

    Group(int id, string name, set<User*> m)
        : id(id), name(name), members(m) {
        ec = new ExpenseController();
    }

    void addExpense(int expenseId, string desc, double amount,
                    SplitType type, User* paidBy) {

        Split* s = new Split();

        for (auto u : members) {
            if (u != paidBy)
                s->addDetails(u, 0); // default equal
        }

        ec->createExpense(expenseId, desc, amount, s, type, paidBy);
    }

    ~Group() {
        delete ec;
    }
};


class GroupController{

    public:
    set<Group*>gs;

    void addGroup(Group* g)
    {
        gs.insert(g);
    }
    void removeGroup(Group* g)
    {
        gs.erase(g);
    }

};
int main()
{
 UserController uc;
    User* alice = new User(1, "Alice");
    User* bob = new User(2, "Bob");
    User* charlie = new User(3, "Charlie");

    // register users with controller (controller will own them and delete in destructor)
    uc.users.insert(alice);
    uc.users.insert(bob);
    uc.users.insert(charlie);

    // Create a group with members
    set<User*> members = { alice, bob, charlie };
    Group* trip = new Group(1, "Trip", members);

    // Group controller
    GroupController gc;
    gc.addGroup(trip);

    // 1) Equal split expense: Alice paid 300 for Dinner (split equally among Alice,Bob,Charlie)
    trip->addExpense(1, "Dinner", 300.0, SplitType::EQUAL, alice);

    // 2) Unequal split expense: Bob paid 250 for Taxi.
    //    We'll specify that Alice owes 50 and Charlie owes 150; remaining goes to Bob.
    Split* taxiSplit = new Split();
    taxiSplit->addDetails(alice, 50.0);
    taxiSplit->addDetails(charlie, 150.0);
    cout << "\n--- Balances ---\n";
    for (auto u : uc.users) {
        cout << "User: " << u->name << '\n';
        u->bs->displayTotalYouGetBack();
        u->bs->displayTotalYouOwe();
        cout << '\n';
    }
    // createExpense called directly on group's expense controller
    trip->ec->createExpense(2, "Taxi", 250.0, taxiSplit, SplitType::UNEQUAL, bob);

    // 3) Percentage expense: Charlie paid 400 for Hotel.
    //    Alice: 20%, Bob: 30%, Charlie pays remaining 50%.
    Split* hotelSplit = new Split();
    hotelSplit->addDetails(alice, 20.0); // percent
    hotelSplit->addDetails(bob, 30.0);   // percent


    cout << "\n--- Balances ---\n";
    for (auto u : uc.users) {
        cout << "User: " << u->name << '\n';
        u->bs->displayTotalYouGetBack();
        u->bs->displayTotalYouOwe();
        cout << '\n';
    }
    trip->ec->createExpense(3, "Hotel", 400.0, hotelSplit, SplitType::PERCENTAGE, charlie);

    // Display each user's balance summary
    cout << "\n--- Balances ---\n";
    for (auto u : uc.users) {
        cout << "User: " << u->name << '\n';
        u->bs->displayTotalYouGetBack();
        u->bs->displayTotalYouOwe();
        cout << '\n';
    }

    // Cleanup: delete groups (their destructors will clean expenses / splits)
    for (auto gptr : gc.gs) {
        delete gptr;
    }
    gc.gs.clear();

    // UserController destructor will delete users and their balance sheets when uc goes out of scope.
    return 0;
}