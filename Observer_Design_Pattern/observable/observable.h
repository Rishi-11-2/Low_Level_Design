using namespace std;
#include<set>
#pragma once 
class NotificationAlertObserver ;

class StockObservable{

    public:
    virtual void add(NotificationAlertObserver* observer) = 0;
    virtual void remove(NotificationAlertObserver* observer) = 0;

    virtual void notifySubscribers()  = 0;
    virtual void setStockCount(int newStockAdded) =0;
    virtual int getStockCount() = 0;

    virtual ~StockObservable() {}
};