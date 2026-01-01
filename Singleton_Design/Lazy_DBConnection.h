#pragma once
#include <iostream>
using namespace std;

// Lazy Initialization
class Lazy_DBConnection {
public:
    static Lazy_DBConnection& instance() { /// thread safe as defined initially
        static Lazy_DBConnection inst; // defined on first use — no out-of-class definition needed
        return inst;
    }

    void execute(const char* sql) {
        cout<<"Executing SQL query :"<<sql<<endl;
     }

private:
    Lazy_DBConnection() = default;
    ~Lazy_DBConnection() = default;
    Lazy_DBConnection(const Lazy_DBConnection&) = delete;
    Lazy_DBConnection& operator=(const Lazy_DBConnection&) = delete;
    Lazy_DBConnection(Lazy_DBConnection&&) = delete;
    Lazy_DBConnection& operator=(Lazy_DBConnection&&) = delete;
};
