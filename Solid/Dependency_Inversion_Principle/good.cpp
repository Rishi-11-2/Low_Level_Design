#include<bits/stdc++.h>
using namespace std;

class IKeyboard{
    public:
    virtual void type(string & text) = 0;
    virtual ~IKeyboard() {}
};

class IMouse{
    public:
    virtual void click ()=0;
    virtual ~IMouse()  {}
};

class WiredKeyboard : public IKeyboard{

    public:
    void type(string &text)
    {
        cout<<"Typing:"<<text<<endl; 
    }
};
class WiredMouse : public IMouse{

    public:
    void click()
    {
        cout<<"Clicking"<<endl;
    }
};

class Macbook{
    public:

    // object slicing is avoided via pointer indirection
    IKeyboard* keyboard; 
    IMouse* mouse ;
    // injecting abstractions through constructor 
    Macbook(IKeyboard* keyboard, IMouse* mouse) : keyboard(keyboard) , mouse(mouse){}
    ~Macbook()
    {
        delete keyboard;
        delete mouse;
    }
};




