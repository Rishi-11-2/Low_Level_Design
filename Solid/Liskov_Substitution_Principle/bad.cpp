#include<bits/stdc++.h>
using namespace std;

class Vehicle{

    public:
    virtual int getNumberOfWheels(){
        return 2;
    }
    virtual bool hasEngine()
    {
        return true;
    }

    virtual ~Bike() {}
};

class MotorCycle : public Vehicle{

    public:
    int speed;

    void accelerate(){
        speed+=10;
    }
};

class Car: public  Vehicle{
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
    bool hasEngine()
    {
        throw runtime_error("Bicycle does not have engine"); // has reduced the capability of the parent class 
        // so there is a chance that client code might break
    }
    void accelerate()
    {
        speed= speed+1;
    }
};