#include<bits/stdc++.h>
using namespace std;

class WaiterInterface{

    public:
    virtual void serveCustomers() = 0;
    virtual void takeOrder() = 0;
};

class CookInterface{

    public:
    virtual void cookFood () = 0;
};

class DishwasherInterface{
    public:
    virtual void washDishes() = 0;

};

class Waiter : public WaiterInterface{

    public:
    void serveCustomers()
    {
        cout<<"Serving Customers"<<endl;
    }
    void takeOrder()
    {
        // takes order by interacting with the customer 
    }
};

class Chef : public CookInterface
{
    public:
    void cookFood()
    {
        cout<<"Cooking Delicious food"<<endl;
    }
}
class Washer : public DishwasherInterface{

    public:
    void washDishes()
    {
        cout<<"Washing Dishes"<<endl;
    }
};