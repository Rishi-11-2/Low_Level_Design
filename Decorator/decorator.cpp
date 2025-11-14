#include<iostream>
using namespace std;

// design a pizza showroom which calculates the cost 
class BasePizza{
    public:
    virtual  int cost() = 0;
    virtual ~BasePizza(){}
};

class Farmhouse : public BasePizza{

    public:
    int cost()
    {
        return 200;
    }
};
class VegDelight : public BasePizza{
    public:
    int cost()
    {
        return 300;
    }
};

class Margaherita : public BasePizza{
    public:
    int cost()
    {
        return 300;
    }
};

class ToppingDecorator : public BasePizza{
};

class ExtraCheese : public ToppingDecorator{

    public:
    BasePizza* bp;

    ExtraCheese(BasePizza* bp):bp(bp){}


    int cost() override{
        return bp->cost() + 50;
    }
};

class Mushroom : public ToppingDecorator{

    public:
    BasePizza* bp;

    Mushroom(BasePizza* bp):bp(bp){}


    int cost() override{
        return bp->cost() + 30;
    }
};

int main()
{

    ExtraCheese* ec = new ExtraCheese(new Mushroom(new Farmhouse()));

    cout<<ec->cost()<<endl;


}