#include<bits/stdc++.h>
using namespace std;

class Vehicle{

    public:
    virtual int getNumberOfWheels(){
        return 2;
    }
    virtual ~Bike() {}
};

class EngineVehicle{
    public:
    virtual bool hasEngine()
    {
        return true;
    }
    virtual ~EngineVehicle(){}
};
class MotorCycle : public Vehicle, public EngineVehicle{

    public:
    int speed;

    void accelerate(){
        speed+=10;
    }
};

class Car: public  Vehicle,public EngineVehicle{
    public:
    int speed ;
    int getNumberOfWheels()
    {
        return 4;
    }
    void accelerate()
    {
        speed+=10;
    }
};

class Bicycle : public Vehicle{

    public:
    int speed;
    void accelerate()
    {
        speed= speed+1;
    }
};