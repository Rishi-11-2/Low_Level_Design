#include<bits/stdc++.h>
using namespace std;


// SOLID principle - single responsibility principle
// bad code
// multiple responsibilites assigned to the invocie class which is not good
// suppose we write unit test, if any of functionalities change , unit test will break



class Invoice{

    double amount;

    public:

    Invoice(double amount)
    {
        this->amount = amount;
    }

    void generateInvoice()
    {
        cout<<"Invoice generated and printed for the "<<amount<<endl;
    }

    void saveToDatabase()
    {
        cout<<"Saving invoice to DB"<<endl;
    }

    void sendEmailNotification()
    {
        cout<<"Sending email notifications for invoice "
    }

};