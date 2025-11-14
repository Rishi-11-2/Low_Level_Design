#include<bits/stdc++.h>
using namespace std;

class RestaurantEmployee{

    public:
    virtual void washDishes() = 0;
    virtual void serveCustomers() = 0;
    virtual void cookFood () = 0;
};

class Waiter : public RestaurantEmployee{

    public:
    void washDishes()
    {
        // not my job
    }
    void serveCustomers()
    {
        cout<<"Serving Customers"<<endl;
    }
    void cookFood()
    {
        // not my job
    }
};

