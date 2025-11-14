#include<iostream>
using namespace std;


class Shape{
    public:
    virtual void draw()=0;
};

class Circle : public Shape{
    public:
    void draw() override{
        cout<<"Drawing Circle"<<endl;
    }
};
class Square : public Shape{
    public:
    void draw() override{
        cout<<"Drawing Square"<<endl;
    }
};

class Rectangle : public Shape{
    public:
    void draw() override{
        cout<<"Drawing Rectangle"<<endl;
    }
};

class Shape_Factory{
    public:

    Shape* getShape(string s)
    {  
      string key = s;
      for (auto &c : key) c = toupper(c);

      if (key == "CIRCLE")
      return new Circle();
      else if (key == "RECTANGLE")
      return new Rectangle();
      else if (key == "SQUARE")
      return new Square(); 
      return nullptr;
    }
};

int main()
{
    Shape_Factory* sf = new Shape_Factory();
    
    Shape* s = sf->getShape("CIRCLE");

    s->draw();
}