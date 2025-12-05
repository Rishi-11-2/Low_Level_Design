#include<iostream>
#include<string>
using namespace std;



class LogProcressor{

    protected : 
    LogProcressor* nextLoggerProcessor;
    public:
    enum Level{
        INFO = 1,
        DEBUG = 2 ,
        ERROR = 3
    };

   explicit LogProcressor(LogProcressor* loggerProcessor): nextLoggerProcessor(loggerProcessor){}


    virtual void log(int loglevel,string message)
    {
        if(nextLoggerProcessor!=nullptr)
        {
            nextLoggerProcessor->log(loglevel,message);
        }
    }

    virtual ~LogProcressor()
    {
        delete nextLoggerProcessor;
    }
};

class InfoLogProcessor:public LogProcressor{

    public:
    explicit InfoLogProcessor(LogProcressor* nextLoggerProcessor):LogProcressor(nextLoggerProcessor){}

    void log(int loglevel,string message)
    {
        if(loglevel==INFO)
        {
            cout<<"INFO::"<<message<<endl;
        }
        else
        {
            LogProcressor::log(loglevel,message);
        }
    }
};

class DebugLogProcessor:public LogProcressor{

    public:
   explicit DebugLogProcessor(LogProcressor* nextLoggerProcessor):LogProcressor(nextLoggerProcessor){}

    void log(int loglevel,string message)
    {
        if(loglevel==DEBUG)
        {
            cout<<"DEBUG::"<<message<<endl;
        }
        else
        {
            LogProcressor::log(loglevel,message);
        }
    }
};

class ErrorLogProcessor:public LogProcressor{

    public:
    explicit ErrorLogProcessor(LogProcressor* nextLoggerProcessor):LogProcressor(nextLoggerProcessor){}

    void log(int loglevel,string message)
    {
        if(loglevel==ERROR)
        {
            cout<<"ERROR::"<<message<<endl;
        }
        else
        {
            LogProcressor::log(loglevel,message);
        }
    }
};

int main()
{
    LogProcressor* log = new InfoLogProcessor(new DebugLogProcessor(new ErrorLogProcessor(nullptr)));
    

    // Chain : Info --> Debug --> Error 
    log->log(LogProcressor::ERROR,"Exception happens");
    log->log(LogProcressor::INFO,"just for info");
    log->log(LogProcressor::DEBUG,"need to debug this");

    delete log ;

}