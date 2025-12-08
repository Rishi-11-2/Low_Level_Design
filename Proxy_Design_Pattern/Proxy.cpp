#include<iostream>
#include<string>
using namespace std;

class EmployeeD{
    public:
    string name;
    int employeeId;
    EmployeeD(string name,int employeeId):name(name),employeeId(employeeId){

    }
};

class Employee{

    public:
    virtual void create(string client , EmployeeD* e) = 0;
    virtual void del(string client,EmployeeD* e) = 0;
    virtual EmployeeD* get(string client,int employeeId)  = 0;
};


class EmployeeImplement:public Employee{
    public:
    void create(string client,EmployeeD* e)
    {
        cout<<"Creating a row in the table"<<endl;
    }

    void del(string client,EmployeeD* e)
    {
        cout<<"Deleting a row in the table"<<endl;
    }

    EmployeeD* get(string client,int employeeId)
    {
        cout<<"Fetching details for :"<<employeeId<<endl;
    }
};

class EmployeeProxy : public Employee{
    public: // has-a relationship with EmployeeImplement
    EmployeeImplement* emp = new EmployeeImplement();
    void create(string client,EmployeeD* e)
    {
        if(client=="ADMIN")
        {
            emp->create(client,e);
        }
        else
        {
            cout<<"Error"<<endl;
        }
    }
    void del(string client,EmployeeD* e)
    {
        if(client=="ADMIN")
        {
            emp->del(client,e);
        }
        else
        {
            cout<<"Error"<<endl;
        }
    }

    EmployeeD* get(string client,int employeeId)
    {
        if(client=="ADMIN" || client == "USER")
        {
            emp->get(client,employeeId);
        }
        else{
            cout<<"Error"<<endl;
        }
    }
};



int main()
{
    EmployeeD* a = new EmployeeD("ADMIN",120);
    EmployeeProxy* ep = new EmployeeProxy();

    ep->create(a->name,a);
}