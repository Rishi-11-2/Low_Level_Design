#include<iostream>
using namespace std;

#include "../observable/observable.h"
#include "NotificationAlertObserver.h"

class EmailObserver : public NotificationAlertObserver {
    public:
    StockObservable* obj;
    string emailId;

    EmailObserver(string emailId, StockObservable* obj): emailId(emailId), obj(obj){}


    void update() override{
        sendMail(emailId,"going through email");
    };

    void sendMail(string& emailId, string msg)
    {
        cout<<"Going to emailid: "<<emailId<<"Message is : "<<msg<<endl;
    }
};