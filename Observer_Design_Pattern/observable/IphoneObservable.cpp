#include<iostream>
using namespace std;

#include "observable.h"

#include "../observer/NotificationAlertObserver.h"



class IphoneObservable:public StockObservable{

    private:
    int stockCount = 0;
    set<NotificationAlertObserver*>observerList;
    public:


    void add(NotificationAlertObserver* observer)
    {
        observerList.insert(observer);
    }
    void remove(NotificationAlertObserver* observer)
    {
        observerList.erase(observer);
    }

    void notifySubscribers()
    {
        for(auto observer : observerList)
        {
            observer->update();
        }
    }
    void setStockCount(int newData)
    {
        if(stockCount ==0)
        notifySubscribers();
        
        stockCount = stockCount + newData;
    }

    int getStockCount()
    {
        return stockCount;
    }

};