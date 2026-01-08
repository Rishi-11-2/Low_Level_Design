
#include<iostream>
using namespace std;
class PaymentFlow{

    protected:


    virtual void validateRequest() = 0;
    virtual void calculateFees() = 0;
    virtual void debitAmount() = 0;
    virtual void creditAmount() = 0;

    public:

    // this is template method which defined the order of steps to execute the tasks
    virtual void sendFlow() final 
    {
        // step 1
        validateRequest();

        // step 2
        calculateFees();

        //step 3
        debitAmount();

        //step 4
        creditAmount();
    }

    virtual ~PaymentFlow() = default ;
};

class PayToFriend : public PaymentFlow{

    public:
    
    void validateRequest()
    {
        cout<<"Validating Request for paying to a friend"<<endl;
    }
    void calculateFees()
    {
        cout<<"Zero fees"<<endl;
    }
    void debitAmount()
    {
        cout<<"Debiting amount "<<endl;
    }
    void creditAmount()
    {
        cout<<"Crediting amount"<<endl;
    }
};
class PayToMerchant : public PaymentFlow{

    public:
    
    void validateRequest()
    {
        cout<<"Validating Request for paying to a merchant"<<endl;
    }
    void calculateFees()
    {
        cout<<"2% fees"<<endl;
    }
    void debitAmount()
    {
        cout<<"Debiting amount "<<endl;
    }
    void creditAmount()
    {
        cout<<"Crediting amount"<<endl;
    }
};

int main()
{
    PayToFriend pf;
    PayToMerchant pm;
    pf.sendFlow();
    pm.sendFlow();
}