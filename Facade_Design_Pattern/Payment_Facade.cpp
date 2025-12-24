#include<iostream>
using namespace std; 





class ProductDAO{

    public:
    Product* getProduct()
    {
        return new Product();
    }
};

class Payment{

    public:
    bool makePayment()
    {

    }
};

class Invoice{
    public:
    void generateInvoice()
    {

    }
};

class SendNotification{

    public:
    void sendNotification()
    {

    }
};


class OrderFacade{

    public:
    ProductDAO* pd;
    Payment* p;
    Invoice* invoice;
    SendNotification* sn;

    OrderFacade()
    {
        pd = new ProductDAO();
        p = new Payment();
        invoice =new Invoice();
        sn = new SendNotification();
    }


    void createOrder()
    {
        auto product = pd->getProduct();
        p->makePayment();
        invoice->generateInvoice();
        sn->sendNotification();
    }

    ~OrderFacade()
    {
        delete pd;
        delete p;
        delete invoice;
        delete sn ;
    }
};


class OrderClient{

    public:
    OrderFacade* of;

    OrderClient()
    {
        of = new OrderFacade();
    }
    void createOrder()
    {
        of->createOrder();
    }


    ~OrderClient()
    {
        delete of ;
    }
};