#include<iostream>
using namespace std;


class Vehicle{
    public:
    virtual int getTankCapacity() = 0;
    virtual int getSeatingCapatiy() = 0;
};

class Car: public Vehicle{
    public:
    int getTankCapacity()
    {
        return 100;

    }
    int getSeatingCapatiy()
    {
        return 10;
    }
};

class Bike : public Vehicle{
    public:
    int getTankCapacity()
    {
        return 200;

    }
    int getSeatingCapatiy()
    {
        return 2;
    }
};

class NullObject : public Vehicle{
    public:
    int getSeatingCapatiy()
    {
        return 0;
    }
    int getTankCapacity()
    {
        return 0;
    }
};

class VehicleFactory: public Vehicle{
    public:
    Vehicle* getVehicle(string type)
    {
        if(type=="CAR")
        return new Car();
        else if(type=="BIKE")
        return new Bike();

        else
        return new NullObject();
    }
};

int main()
{
    VehicleFactory* vf;

    auto it = vf->getVehicle("TRUCK");
    cout<<it->getSeatingCapatiy()<<endl;
    cout<<it->getTankCapacity()<<endl;

    it = vf->getVehicle("CAR");
    cout<<it->getSeatingCapatiy()<<endl;
    cout<<it->getTankCapacity()<<endl;
}