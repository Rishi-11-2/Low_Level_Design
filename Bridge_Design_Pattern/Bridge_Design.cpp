#include<iostream>
using namespace std;



class BreathingImplementor{

    public:
    virtual void breatheProcess() = 0;

};

class LandBreatheImplementor: public BreathingImplementor{

    public:
    void breatheProcess()
    {

    }
};

class WaterBreatheImplementor: public BreathingImplementor{

    public:
    void breatheProcess()
    {

    }
};

class TreeBreatheImplementor: public BreathingImplementor{

    public:
    void breatheProcess()
    {

    }
};

class LivingBeings{

    public:

    BreathingImplementor* bi;

    LivingBeings(BreathingImplementor* bi):bi(bi)
    {

    }
    virtual void breathe() = 0;

};


class Bird: public LivingBeings{

    public:
    Bird(BreathingImplementor* bi):LivingBeings(bi)
    {

    }
    void breathe()
    {
        bi.breatheProcess();
    }
};

class Plant: public LivingBeings{

    public:
    Plant(BreathingImplementor* bi):LivingBeings(bi)
    {

    }
    void breathe()
    {
        bi.breatheProcess();
    }
};


class Fish: public LivingBeings{

    public:
    Fish(BreathingImplementor* bi):LivingBeings(bi)
    {

    }
    void breathe()
    {
        bi.breatheProcess();
    }
};