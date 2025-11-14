using namespace std;

#pragma once

class NotificationAlertObserver {
    public:
    virtual void update() =0;
    virtual ~NotificationAlertObserver() {} 
};