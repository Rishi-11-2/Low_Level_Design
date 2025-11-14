#include <iostream>
using namespace std;

#include "observer/EmailObserver.cpp"
#include "observer/MobileObserver.cpp"
#include "observable/IphoneObservable.cpp"


int main() {
    StockObservable* iphoneObservable = new IphoneObservable();
    
    NotificationAlertObserver* observer1 = new EmailObserver("xyz@gmail.com",iphoneObservable);
    NotificationAlertObserver* observer2 = new EmailObserver("abc@gmail.com",iphoneObservable);
    NotificationAlertObserver* observer3 = new MobileObserver("def@gmail.com",iphoneObservable);

    iphoneObservable->add(observer1);
    iphoneObservable->add(observer2);
    iphoneObservable->add(observer3);
    iphoneObservable->setStockCount(10);

}