


class Student{


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
};

int main()
{
    Student* s = new Student(10,202,"Rishi");


    Student* ss = new Student();
    ss->age = s->age;
    ss->name  = s->name;
    ss->rollnumber = s->rollnumber;
}