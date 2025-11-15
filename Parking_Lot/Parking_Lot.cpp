#include<iostream>
#include<date>
using namespace std;





/* -----------------------------------------------------------------------------------------*/


enum class VehicleType{
    TWO_WHEELER,
    FOUR_WHEELER
};

class Vehicle{ // Parking spot has vehicle 

    public:
    VehicleType type;

    int id;
    
    Vehicle(int id, VehicleType type): id(id),type(type){}
};


/* -----------------------------------------------------------------------------------------*/
class ParkingSpot{ // Parking Spot has-a vehicle 

    protected:
    int id ;
    bool isEmpty ;

    Vehicle* vehicle ;
    int price;

    public:

    ParkingSpot(int id,int price): id(id),isEmpty(true),vehicle(nullptr),price(price){

    }


    bool isParkingEmpty()
    {
        return this->isEmpty;
    }

    void parkVehicle(Vehicle* vehicle)
    {
        this->vehicle=vehicle;
        this->isEmpty=false;
    }

    void removeVehicle()
    {
        this->vehicle = nullptr;
        this->isEmpty = true;
    }

    virtual int getPrice() = 0;
};

class TwoWheelerSpot : public ParkingSpot{

    public:

    TwoWheelerSpot(int id, int price): ParkingSpot(id,price){}
    int getPrice() override
    {
        return 15 + price;
    }

};

class FourWheelerSpot : public ParkingSpot{

    public:

    FourWheelerSpot(int id, int price): ParkingSpot(id,price){}
    int getPrice() override
    {
        return 40 + price;
    }

};
/* -----------------------------------------------------------------------------------------*/


class Ticket{ // Ticket has a parking spot and a vehicle
    public:
    long entryTime;
    Vehicle* v;
    ParkingSpot* ps;
    Ticket(Vehicle* v, ParkingSpot* ps):entryTime(date()),v(v),ps(ps){}

    
};
/* -----------------------------------------------------------------------------------------*/





// strategy design pattern for choosing different strategies

class ParkingStrategy{
    public:
    virtual ParkingSpot* find() = 0;
};

class NearToEntranceStrategy : ParkingStrategy{
    public:
    ParkingSpot* find()
    {
        cout<<"Using NearToEntrance Strategy"<<endl;
    }
};


class NearToEntranceAndElevatorStrategy : ParkingStrategy{
    public:
    ParkingSpot* find()
    {
        cout<<"Using NearToEntrance Strategy"<<endl;
    }
};


/* -----------------------------------------------------------------------------------------*/






class ParkingSpotManager { // it has-a relationship with ParkingSpots


    protected:
    set<ParkingSpot*>ps;

    public:
    ParkingSpotManager(set<ParkingSpot*>ps): ps(ps){}

    virtual ParkingSpot* findAndBookPS() = 0;

    void removeVehicle(ParkingSpot* spot)
    {
        spot->remove();
        ps.insert(spot)
    }



};

class TwoWheelerManager : public ParkingSpotManager{
    public:
    ParkingStrategy* ParkingStrategy;
    TwoWheelerManager(set<ParkingSpot*>&ps):ParkingSpotManager(ps){
        ParkingStrategy = new NearToEntranceStrategy();
    }

    ParkingSpot* findAndBookPS(Vehicle* v)
    {
        ParkingSpot* spot =  ParkingStrategy->find();
        spot->parkVehicle(v);
        ps.erase(spot);
        return spot ;
    }

};

class FourWheelerManager : public ParkingSpotManager{
    public:
    ParkingStrategy* ParkingStrategy;
    FourWheelerManager(set<ParkingSpot*>&ps):ParkingSpotManager(ps){
        ParkingStrategy = new NearToEntranceAndElevatorStrategy();
    }

    ParkingSpot* findAndBookPS(Vehicle* v)
    {
        ParkingSpot* spot =  ParkingStrategy->find();
        spot->parkVehicle(v);
        ps.erase(spot);
        return spot ;
    }

};


/* -----------------------------------------------------------------------------------------*/

// Factory design pattern which return different objects based on different conditions

// has-a relationship with ParkingSpotManager
class ParkingManagerFactory{  


    // factory OWNS these managers
    // they will be deleted when factory is deleted

    private:
    map<VehicleType,ParkingSpotManager*>registry;
    public:

    ParkingSpotManager* getManager(VehicleType type, set<ParkingSpot*>&ps){

        if(registry.count(type))
        return registry[type];
        switch(type){
            case VehicleType::TWO_WHEELER:
            registry[type] =new TwoWheelerManager(ps,strategy);
            return registry[type];

            case VehicleType::FOUR_WHEELER:
            registry[type] = new FourWheelerManager(ps,strategy);
            return registry[type];

            default :
            return nullptr;
        }
    }


    ~ParkingManagerFactory()
    {
        for(auto &it:registry)
        delete it.second;

    }
};

/* -----------------------------------------------------------------------------------------*/

// Entrance gate has-a relationship with Factory

class EntranceGate{

    private:
    ParkingManagerFactory* factory;  // not owned 
    ParkingSpotManager* manager ;   // not owned 

    public:
    EntranceGate(ParkingManagerFactory* factory,VehicleType type,set<ParkingSpot>&ps): factory(factory){
        manager = factory->getManager(type,ps);
    }

    ParkingSpot* admitVehicle(Vehicle* v)
    {
        return manager->findAndBookPS(v);
    } 

    Ticket generateTicket(Vehicle* v)
    {
        spot = admitVehicle(v);
        return new Ticket(v,spot);
    }

};
/* -----------------------------------------------------------------------------------------*/


// strategy design pattern 

class PricingStrategy{

    protected:
    Ticket* ticket;
    public:
    PricingStrategy(Ticket* ticket): ticket(ticket){}
    virtual int getCost(){
        return ticket->ps->getPrice();
    }
};

class HourlyPricingStrategy: public PricingStrategy{


    public:
    HourlyPricingStrategy(Ticket* ticket): PricingStrategy(ticket){}
    int getCost()
    {
        int hours = date.now() - ticket->entryTime;
        return (hours * ticket->ps->getPrice()) ;
    }
};


class MinutePricingStrategy: public PricingStrategy{

    public:
    MinutePricingStrategy(Ticket* ticket): PricingStrategy(ticket){}
    int getCost()
    {
        int hours = date.now() - ticket->entryTime;
        return (hours * ticket->ps->getPrice() * 60) ;
    }
};



/* -----------------------------------------------------------------------------------------*/

class CostComputation{

    protected:
    Ticket* ticket;
    PricingStrategy* strategy;
    public:
    CostComputation(Ticket* ticket): ticket(ticket){}
    virtual int  getCost()= 0;
};


class TwoWheelerCostComputation{

    public:

    TwoWheelerCostComputation(Ticket* ticket):CostComputation(ticket){
        strategy = new HourlyPricingStrategy(ticket);
    }

    int getCost()
    {
        return strategy->getCost();
    }

};

class FourWheelerCostComputation{

    public:
    FourWheelerCostComputation(Ticket* ticket):CostComputation(ticket){
        strategy = new MinutePricingStrategy(ticket);
    }

    int getCost()
    {
        return strategy->getCost();
    }

};
/* -----------------------------------------------------------------------------------------*/


// factory design pattern 

class CostComputationFactory{

    
    public:
    CostComputationFactory(){}

    CostComputation* getCC(Ticket* ticket)
    {
        VehicleType type = ticket->v->type;

        switch (type){
            case VehicleType::TWO_WHEELER:
            return new TwoWheelerCostComputation(ticket);

            case VehicleType::FOUR_WHEELER:
            return new FourWheelerCostComputation(ticket);

            default:
            return nullptr;
        }
    }
};


/* -----------------------------------------------------------------------------------------*/

class ExitGate{

    // has-a relationship with CostComputationFactory
    protected:
    CostComputationFactory* obj;
    ParkingManagerFactory* factory
    ExitGate(ParkingManagerFactory* factory):factory(factory)
    {
        obj = new CostComputationFactory();
    }

    int getCost(Ticket* ticket)
    {
        CostComputation* cc = getCC(ticket);

        remove(ticket);
        return cc->getCost();
    }

    void remove(Ticket* ticket)
    {
        auto v = ticket->v;
        auto ps = ticket->v;

        ParkingSpotManager* manager = factory->getManager(v->type,ps);
        manager->removeVehicle(ps);
    }
};