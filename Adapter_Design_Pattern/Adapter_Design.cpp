

#include<iostream>
#include<string>


using namespace std;


class WeightMachine{ // Adaptee

    public:
    virtual double getWeightInPound() =0;
};

class WeightMachineForBabies: public WeightMachine{

    public:
    double getWeightInPound()
    {
        return 28.0;
    }
};


class WeightMachineAdapter{

    public:
    virtual double getWeightInKG() = 0;
};

class WeightMachineAdapterImpl:public WeightMachineAdapter{

    // has-a relationship with WeightMachine
    public:
    WeightMachine* wm = new WeightMachineForBabies();

    WeightMachineAdapterImpl(WeightMachine* wm):wm(wm){

    }
    double getWeightInKG()
    {
      double weightInPound = wm->getWeightInPound();
      return weightInPound*.45;
    }
};

int main()
{
    WeightMachine* wm = new WeightMachineForBabies();
    WeightMachineAdapter* wma = new WeightMachineAdapterImpl(wm);
    cout<<wma->getWeightInKG()<<endl;
}