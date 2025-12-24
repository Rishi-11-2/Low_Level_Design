#include<iostream>
using namespace std;



class LivingBeings{

    public:


    virtual void breathe() = 0;

};


class Bird: public LivingBeings{

    public:
    void breathe()
    {

    }
};

class Plant: public LivingBeings{

    public:
    void breathe()
    {

    }
};



// now suppose we want a new breathing process for bird,  So we cannot add a new breathing process without adding a new class. So its tightly coupled

// unitl and unless a new child class is not present which use this breathing process, we cannot add a new breathing process