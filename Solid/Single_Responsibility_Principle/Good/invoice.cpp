#include<bits/stdc++.h>
using namespace std;


// SOLID principle - single responsibility principle
// bad code
// multiple responsibilites assigned to the invocie class which is not good
// suppose we write unit test, if any of functionalities change , unit test will break



class Invoice{

    double amount;
    string id ;
    public:

    Invoice(double amount,string id):amount(amount),id(id) {}


    private:

    double getAmount()
    {
        return amount;
    }

    string getId()
    {
        return id;
    }
};

class InvoiceRepository{
    Invoice invocie;
    public:
    InvoiceRepository(Invoice &invocie): invocie(invocie) {}
    void saveToDatabase()
    {
        cout<<"Saving invoice to DB"<<endl;
    }
};
class EmailService{

    Invoice invocie;
    public:
    EmailService(Invoice &invocie): invocie(invocie){}
    void sendEmailNotification()
    {
        cout<<"Sending email notifications for invoice "
    }
};