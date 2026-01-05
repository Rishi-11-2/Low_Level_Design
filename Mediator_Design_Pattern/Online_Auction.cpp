#include<iostream>
#include <vector>
#include <string>

using namespace std;

class Person{

    public:
    virtual void placeBid(int bidAmount) =0;
    virtual void receiveNotification(string bidderName ,int bidAmount) =0;
    virtual string getName() =0;
    virtual ~Person() = default ;
}
;
class AuctionMediator{

    public:
    virtual void addBidder(Person& bidder) =0;
    virtual void placeBid(Person& bidder,int bidAmount) =0;
};

class Auction : public AuctionMediator{
    public:
    vector<Person*>persons;
    void addBidder(Person& person)
    {
        persons.push_back(&person);
    }

    void placeBid(Person& bidder,int bidAmount)
    {
        for(auto it:persons)
        {
            if(it->getName() != bidder.getName())
            {
                it->receiveNotification(bidder.getName(),bidAmount);
            }
        }
    }
};


class Bidder : public Person{
    string name;
    AuctionMediator& mediator;

    public:
    Bidder(string name,AuctionMediator& mediator):name(name),mediator(mediator)
    {
        mediator.addBidder(*this);
    }
    void placeBid(int bidAmount)
    {
        mediator.placeBid(*this,bidAmount);
    }

    string getName()
    {
        return name;
    }
    void receiveNotification(string bidderName, int bidAmount)
    {
        cout<<"Information for Bidder :"<<name<<endl;
        cout<<"Bidder: "<<bidderName<<" has bidded a amount of :"<<bidAmount<<endl;
    }
};

int main()
{
    Auction mediator;
    Bidder b1("Rishi",mediator);
    Bidder b2("Ram",mediator);
    Bidder b3("Sita",mediator);

    b1.placeBid(100);
    b3.placeBid(101);
    b2.placeBid(110);
}