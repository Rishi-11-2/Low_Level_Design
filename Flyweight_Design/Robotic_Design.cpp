#include<iostream>
using namespace std;



class IRobot{

    public:
    virtual void display() = 0;
};


// Flyweight Class
class HumanoidRobot:public IRobot{

    private:
    string type;
    Sprites* body;
    public:

    HumanoidRobot(string type,Sprites* body):type(type),body(body)
    {

    }

    string getType()
    {
        return type;
    }
    Sprites* getBody()
    {
        return body;
    }

    void display(int x,int y)
    {
        cout<<"Robot at x-coordinate: "<<x<<" Y-cooridinate: "<<y<<endl;
    }
};
class RoboticDog:public IRobot{

    private:
    string type;
    Sprites* body;
    public:

    HumanoidRobot(string type,Sprites* body):type(type),body(body)
    {

    }

    string getType()
    {
        return type;
    }
    Sprites* getBody()
    {
        return body;
    }

    void display(int x,int y)
    {
        cout<<"Robot at x-coordinate: "<<x<<" Y-cooridinate: "<<y<<endl;
    }
};


class RobotFactory{

    private:
    static map<type,IRobot*>mp;

    public:
    static IRobot* createRobot(string type)
    {
        if(mp.find(type)!=mp.end())
        return mp[type];

        if(type=="HUMANOIDROBOT")
        {
            Sprites* humanoidSprites = new Sprites();
            mp[type] = new HumanoidRobot(type,humanoidSprites);
        }
        else if(type=="ROBOTICDOG")
        {
            Sprites* roboticDogSprites = new Sprites();
            mp[type] = new RoboticDog(type,roboticDogSprites);
        }
        return nullptr;
    }

};

int 