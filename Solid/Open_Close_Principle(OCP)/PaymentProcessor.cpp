#include<bits/stdc++.h>
using namespace std ;

// this code violates open-close principle because whenever we add new payment method we have to change the existing code.
class PaymentProcessor{

    public:
    void processPayment(string paymentMethod,double amount)
   {
      if (paymentMethod == "creditCard")
      {
          // business logic 
          cout<<"Processing credit card payment:"<<amount<<endl;
      }
      else if (paymentMethod == "debitCard")
      {
          // business logic 
          cout<<"Processing debit card payment:"<<amount<<endl;
      }
      else if (paymentMethod == "Paypal")
      {
         // business logic 
        cout<<"Processing Paypal payment:"<<amount<<endl;
      }
      else
      {
        throw invalid_argument("Invalid payment method.")
      }
   }
};