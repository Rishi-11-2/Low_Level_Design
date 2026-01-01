#include "Lazy_DBConnection.h"


int main()
{
    Lazy_DBConnection::instance().execute("Select * from A");
}