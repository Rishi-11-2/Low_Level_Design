#include<iostream>
using namespace std; 



class Prototype{

    public:

    virtual Prototype* clone() = 0;
};


class Student:public Prototype{


    private:
    int age;
    string name;

    public:
    int rollnumber;

    Student()
    {

    }
    Student(int age,int rollnumber,string name):age(age),name(name),rollnumber(rollnumber){

    }


    Prototype* clone()
    {
        return new Student(age,rollnumber,name);
    }
};


int main()
{
    Student* s = new Student(10,220,"Rishi");
    Student* ss =(Student*) s->clone();
    cout<<ss->rollnumber<<endl;
}