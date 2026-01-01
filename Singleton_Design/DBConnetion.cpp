#include "DBConnection.h"

int main() {
    DBConnection::instance().execute("SELECT * FROM users");
}