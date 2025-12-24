#include<iostream>
using namespace std;








class EmployeeDAO{
    
    public:
    void insert()
    {
        
    }
    
    void updateEmployee(){
        
    }
    
    
    Employee* getEmployeeDetails(string emailId)
    {
        
    }
    
    
    Employee* getEmployeeDetails(int employeeId)
    {
        
    }
};
class EmployeeFacade{ // has-a relationship with EmployeeDAO

    public:
    EmployeeDAO* edao;
    EmployeeFacade() 
    {
        edao = new EmployeeDAO();
    }


    void insert()
    {
        edao->insert();
    }

    Employee* getEmployeeDetails(int employeeId)
    {
        return edao->getEmployeeDetails();
    }

    ~EmployeeFacade()
    {
        delete edao;
    }
}



class EmployeeClient{

    public:
    EmployeeFacade* ef;

    EmployeeClient()
    {
        ef = new EmployeeFacade();
    }



    ~EmployeeClient()
    {
        delete ef;
    }
};