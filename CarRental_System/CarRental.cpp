#include<iostream>
using namespace std;


// keep the design as simple as possible 


enum class VehicleType{
    CAR,
    BIKE,
    SCOOTY
};

enum class Status{
    ACTIVE,
    INACTIVE
};

enum class ReservationStatus{
    SCHEDULED,
    INPROGRESS,
    COMPLETED,
    CANCELLED
};

enum class ReservationType{
    HOURLY,
    DAILY
};
class Vehicle{

    private:
    int id;
    int vehicleId;

    VehicleType vType ;
    int kmDriven;
    int dailyRentalCost;
    int hourlyRentalCost;
    int numberOfSeats;


    public:
    Vehicle(int id,int vehicleId) : id(id),vehicleId(vehicleId) {}
    int getVehicleId()
    {
        return vehicleId;
    }

    int setVehicleId(int id)
    {
        vehicleId = id;
    }



};

class Car : public Vehicle{

};


class VehicleInventoryManagement{ // has a Vehicle 

    public:
    //CRUD
    set<Vehicle*>vList;

};

class Location{
    public:
    string address;
    string city ;
    string state ;
    int pincode;

    Location(string address,string city,string state,int pincode): address(address),city(city),state(state),pincode(pincode){}
};


class Reservation{
    public:
    int reservationId;
    User* user;
    Vehicle* vehicle ;
    Date bookingDate;
    Date dateBookedFrom;
    Date dateBookedTo;
    long fromTimestamp;
    long toTimestamp;
    Location pickUpLocation;
    Location dropLocation;

    ReservationType reservationType;
    ReservationStatus reservationStatus;


};
class Store{ // has a VehicleInventoryManagement
    public:

    int storeId;
    VehicleInventoryManagement* vobj;

    Location* loc;

    set<Reservation*>res;


    

};


class User{
    private:
    int  userId;
    string drivingLicense;
    string username;

    User(int userId,string drivingLicense,string username):userId(userId),drivingLicense(drivingLicense),username(username){}
};

class VehicleRentalSystem{

    private:
    set<User*>user;
    set<Store*>store;
    // CRUD operations on stores

    public:
    VehicleRentalSystem(set<User*>&user,set<Store*>&store) : user(user),store(store){}

    Store getStore(Location loc)
    {// filter store based on locations
        return *store.begin();
    }
};


class Bill{
    public:
    bool isPaid;
    double amount;
    Reservation* res;

    Bill(Reservation* res):res(res){}

    void payBill()
    {
        amount = computeAmount();
        isPaid = true;

    }

    double computeAmount()
    {
        return 100.0; 
    }
};


class Payment{
    public:
    Bill* bill;

    Payment(Bill* bill):bill(bill){}

    void payBill()
    {
        bill->payBill();
    }
};