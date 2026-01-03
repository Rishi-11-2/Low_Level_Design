#include<iostream>
using namespace std;


class AirConditioner{

    public:
    bool isOn;
    int temperature;

    AirConditioner()
    {
        isOn= false;
        temperature = 20;
    }


    void turnOnAC()
    {
        isOn = true;
    }
    void turnOffAC()
    {
        isOn = false;
    }

    void increaseTemperature()
    {
        temperature++;
    }
    void decreaseTemperature()
    {
        temperature--;
    }
};


class ICommand{

    public:
    virtual void execute ()= 0;
    virtual void undo () = 0;
};

class TurnOffAC:public ICommand{

    public:
    AirConditioner ac;

    TurnOffAC(AirConditioner ac):ac(ac){

    }

    void execute()
    {
        cout<<"Turning AC OFF"<<endl;
        ac.turnOffAC();
    }
    void undo()
    {
        cout<<" Undoing Turning AC OFF"<<endl;
        ac.turnOnAC();
    }
};
class TurnOnAC:public ICommand{

    public:
    AirConditioner ac;

    TurnOnAC(AirConditioner ac):ac(ac){

    }

    void execute()
    {
        cout<<"Turning AC ON"<<endl;
        ac.turnOnAC();
    }
    void undo()
    {
        cout<<" Undoing Turning AC ON"<<endl;
        ac.turnOffAC();
    }
};

class IncreaseTemperature: public ICommand{

    public:
    AirConditioner ac;

    IncreaseTemperature(AirConditioner ac):ac(ac){

    }

    void execute()
    {
        cout<<"Increasing Temperature"<<endl;
        ac.increaseTemperature();
    }
    void undo()
    {
        cout<<"Undoing Increasing Temperature"<<endl;
        ac.decreaseTemperature();
    }
};
class DecreaseTemperature: public ICommand{

    public:
    AirConditioner ac;

    DecreaseTemperature(AirConditioner ac):ac(ac){

    }

    void execute()
    {
        cout<<"Decreasing Temperature"<<endl;
        ac.decreaseTemperature();
    }
    void undo()
    {
        cout<<"Undoing decreasing temperature"<<endl;
        ac.increaseTemperature();
    }
};

class NoCommand : public ICommand{

    public:
    void execute()
    {
        cout<<"NULL , not executing anything "<<endl;
    }
    void undo()
    {
        cout<<"NULL , not executing anything "<<endl;
    }
};

class MyRemoteControl{

    private:
    static NoCommand noCommand;
    static stack<ICommand*>commandHistory;
    public:
    ICommand* command;
    MyRemoteControl():command(&noCommand)
    {

    }


    void setCommand(ICommand* com)
    {
        command = com;
    }
    
    void pressButton()
    {
        commandHistory.push(command);
        command->execute();
    }

    void undo()
    {
        if(!commandHistory.empty())
        {
            auto it = commandHistory.top();
            commandHistory.pop();
            it->undo();
        }
    }
};

NoCommand MyRemoteControl::noCommand;
stack<ICommand*>MyRemoteControl::commandHistory;

int main()
{
    AirConditioner ac;
    MyRemoteControl rc ;
    TurnOffAC com(ac);
    TurnOnAC com1(ac);
    IncreaseTemperature com2(ac);
    DecreaseTemperature com3(ac);
    rc.setCommand(&com);
    rc.pressButton();
    rc.setCommand(&com1);
    rc.pressButton();
    rc.setCommand(&com2);
    rc.pressButton();
    rc.undo();
    rc.setCommand(&com3);
    rc.undo();
    rc.pressButton();
}