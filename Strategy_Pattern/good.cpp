#include<iostream>
using namespace std;



// we use strategy pattern when we think that the several subclasses of particular classes share some functionality with each other
// this  functionality is not present in the base class
class DriveStrategy{
    public:
    virtual void drive()=0;
};
class NormalDriveStrategy : public DriveStrategy{
    
    public:
    void drive () override
    {
        cout<<"Normal Drive Strategy"<<endl;
    }
};
class SportsDriveStrategy : public DriveStrategy{
    public:
    void drive () override
    {
        cout<<"Sports Drive Strategy"<<endl;
    }
};
class OffloadDriveStrategy : public DriveStrategy{
    public:
    void drive () override
    {
        cout<<"Offload Drive Strategy"<<endl;
    }
};
class Vehicle{
    public:
    DriveStrategy* driveObject;
    Vehicle(DriveStrategy* driveObject): driveObject(driveObject) {}

    void drive()
    {
        driveObject->drive();
    }

     ~Vehicle()
    {
        delete driveObject;
    }
};
class SportsVehicle : public Vehicle{
    public:
    SportsVehicle():Vehicle(new SportsDriveStrategy()){}
};

class PassengerVehicle : public Vehicle{
    public:
    PassengerVehicle():Vehicle(new NormalDriveStrategy()){}
};

class OffRoadVehicle : public Vehicle{
    public:
    OffRoadVehicle() : Vehicle(new OffloadDriveStrategy()){}
};

int main()
{
    SportsVehicle s;
    s.drive();
}