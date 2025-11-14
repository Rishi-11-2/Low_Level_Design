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
    WiredKeyboard* keyboard;
    WiredMouse* mouse ;
    Macbook(){
        keyboard = new WiredKeyboard();
        mouse = new WiredMouse();
    }
    ~Macbook()
    {
        delete keyboard;
        delete mouse;
    }
};




