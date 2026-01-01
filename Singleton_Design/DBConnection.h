#pragma once
#include <iostream>


// Eager Initialization
using namespace std;
class DBConnection {

private:
    DBConnection() = default;
    ~DBConnection() = default;
    DBConnection(const DBConnection&) = delete;
    DBConnection& operator=(const DBConnection&) = delete;
    DBConnection(DBConnection&&) = delete;
    DBConnection& operator=(DBConnection&&) = delete;

    static DBConnection instance_; // declaration only
public:
    static DBConnection& instance() noexcept { return instance_; }
    void execute(const char* sql)
    {
        cout<<"Executing SQL query :"<<sql<<endl;
    }
};

DBConnection DBConnection::instance_;