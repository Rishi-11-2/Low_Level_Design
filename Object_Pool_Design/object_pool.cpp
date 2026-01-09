#include <iostream>
#include <vector>
#include <unordered_set>


using namespace std;
class DBConnection {
public:
    DBConnection() { 

     }
    ~DBConnection() { 

     }
};

class DBConnectionPool {

private:

    // by making the constructor private we are following the singleton design pattern
    // this ensures that only one object of the class is created
    // this singleton design pattern is thread-safe 
    // however you can create multiple instances using move or copy 
    // for that you will need some additional lines of code 
    std::vector<DBConnection*> freeConnectionsPool;
    std::unordered_set<DBConnection*> connectionsCurrentlyInUse;
    int INITIAL_POOL_SIZE;
    int MAX_POOL_SIZE;
    DBConnectionPool(int INITIAL_POOL_SIZE, int MAX_POOL_SIZE)
        : INITIAL_POOL_SIZE(INITIAL_POOL_SIZE), MAX_POOL_SIZE(MAX_POOL_SIZE)
    {
        for (int i = 0; i < INITIAL_POOL_SIZE; ++i) {
            cout<<"Warming up the INITIAL POOL CONNECTION"<<endl;
            freeConnectionsPool.push_back(new DBConnection());
        }
    }

    ~DBConnectionPool() {
        // delete any remaining connections
        for (auto p : freeConnectionsPool) delete p;
        for (auto p : connectionsCurrentlyInUse) delete p;
    }


    public:
    static DBConnectionPool& getInstance(int INITIAL_POOL_SIZE = 4, int MAX_POOL_SIZE  = 6){
        static DBConnectionPool instance(INITIAL_POOL_SIZE,MAX_POOL_SIZE);
        return instance;
    }

    // returns nullptr if pool exhausted
    DBConnection* getConnection()
    {
        int sz1 = (int)(freeConnectionsPool.size());
        int sz2 = (int)(connectionsCurrentlyInUse.size());
        if (sz1 == 0 && sz2 < MAX_POOL_SIZE) {
            cout<<"Creating new DB conneciton"<<endl;
            freeConnectionsPool.push_back(new DBConnection());
        } else if (sz1 == 0 && sz2 == MAX_POOL_SIZE) {
            cout<<"Cannot create new db connection, MAX LIMIT exceeded"<<endl;
            return nullptr;
        }

        DBConnection* db = freeConnectionsPool.back();
        freeConnectionsPool.pop_back();
        connectionsCurrentlyInUse.insert(db);
        return db;
    }

    void restoreConnection(DBConnection* db)
    {
        if (!db) return;
        auto it = connectionsCurrentlyInUse.find(db);
        if (it == connectionsCurrentlyInUse.end()) return; // not from this pool
        connectionsCurrentlyInUse.erase(it);
        freeConnectionsPool.push_back(db);
    }
};


int main()
{
    DBConnectionPool& dbpool = DBConnectionPool::getInstance(4,6);

    vector<DBConnection*>v;
    for(int i =1 ; i<=7;i++)
    {
        v.push_back(dbpool.getConnection());
    }

    

}