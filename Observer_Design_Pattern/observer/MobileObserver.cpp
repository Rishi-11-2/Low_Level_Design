#include<iostream>
using namespace std;

#include "../observable/observable.h"
#include "NotificationAlertObserver.h"

class MobileObserver : public NotificationAlertObserver{
    public:
    StockObservable* obj;
    string username;

    MobileObserver(string username, StockObservable* obj): username(username), obj(obj){}


    void update() override{
        sendMsg(username,"going through mobile");
    };

    void sendMsg(string& username, string msg)
    {
        cout<<"Going to username: "<<username<<"Message is : "<<msg<<endl;
    }
};