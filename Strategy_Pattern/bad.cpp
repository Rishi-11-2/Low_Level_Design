#include<bits/stdc++.h>
using namespace std;

class Vehicle{

    public:
    virtual void drive()
    {
        cout<<"Normal Drive Capability"<<endl;
    }
};

class SportsVehicle : public Vehicle{
    public:
    void drive()
    {
        // different drive capability 
        cout<<"Sports drive capability"<<endl;
    }
};

class PassengerVehicle : public Vehicle{
    public:

};

class OffRoadVehicle : public Vehicle{
    public:

    void drive()
    {
        // different drive capability
        cout<<"Offload drive capability"<<endl;
    }
    
};