#include<iostream>
#include<string>
#include<vector>

using namespace std;

class SingleRoom ;
class DoubleRoom;
class DeluxRoom;

class RoomVisitor{

    public:
    virtual void visit(SingleRoom& single) =0;
    virtual void visit(DoubleRoom& doubl) =0;
    virtual void visit(DeluxRoom& delux) =0;
};
class RoomElement{
    private:
    int roomPrice;
    public:
    virtual void accept(RoomVisitor& visitor) = 0;
    void setRoomPrice(int price){
        roomPrice = price;
    }
    int getRoomPrice()
    {
        return roomPrice;
    }

};
class SingleRoom : public RoomElement{
    public:
    SingleRoom()
    {
        setRoomPrice(1000);
    }
    void accept(RoomVisitor& visitor)
    {
        visitor.visit(*this);
    }
    
};
class DoubleRoom : public RoomElement{
    public:
    DoubleRoom()
    {
        setRoomPrice(2000);
    }
    void accept(RoomVisitor& visitor)
    {
        visitor.visit(*this); // double dispatch happening here
        // first dispatch here the compiler evaluates what visitor is poiting to ?
        //  depending on whether RoomPricingVisitor or RoomMaintenance Visitor, calls the visitor method of corresponding class
        // now one more question  would be which visit method of that particular class?
        // Here second dispatch happens calculating which visit  method to invoke depending on which RoomElement object this pointer is poiting to ...

        // the method which has to be invoked is now dependent on two objects: the caller and the argument 

        // caller --> the particular class in which visit method has to be called
        // argument --> the particular visit method to be called in that particular class
    }
};
class DeluxRoom : public RoomElement{
    public:
    DeluxRoom()
    {
        setRoomPrice(5000);
    }
    void accept(RoomVisitor& visitor)
    {
        visitor.visit(*this);
    }
};
class RoomPricingVisitor: public RoomVisitor{

    public:
    void visit(SingleRoom& single)
    {
        cout<<"Pricing computation of single room"<<endl;
        single.setRoomPrice(1000);
        cout<<"Room Price: "<<single.getRoomPrice()<<endl;
    }
    void visit(DoubleRoom& doubl)
    {
        cout<<"Pricing computation of double room"<<endl;
        doubl.setRoomPrice(4000);
        cout<<"Room Price: "<<doubl.getRoomPrice()<<endl;

    }
        void visit(DeluxRoom& delux)
    {
        cout<<"Pricing computation of delux room"<<endl;
        delux.setRoomPrice(100000);
        cout<<"Room Price: "<<delux.getRoomPrice()<<endl;

    }
};
class RoomMaintenanceVisitor: public RoomVisitor{
    public:
    void visit(SingleRoom& single)
    {
        cout<<"Maintenance of single room"<<endl;
    }
    void visit(DoubleRoom& doubl)
    {
        cout<<"Maintenance of double room"<<endl;
    }
        void visit(DeluxRoom& delux)
    {
        cout<<"Maintenance of delux room"<<endl;
    }
};


int main()
{
    SingleRoom sr;
    DoubleRoom dr;
    DeluxRoom dxr;
    RoomPricingVisitor rpv;
    RoomMaintenanceVisitor rmv;
    sr.accept(rpv);
    dr.accept(rmv);
    sr.accept(rmv);
    dxr.accept(rpv);

}

