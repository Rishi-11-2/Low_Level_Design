#include <iostream>
#include <vector>
#include <string>

using namespace std;

// Forward declaration of User class
class User;

// Mediator Interface
class DocumentSessionMediator {
public:
    virtual void broadcastChange(string change, User* sender) = 0;
    virtual void join(User* user) = 0;
    virtual ~DocumentSessionMediator() {}
};

// User Class
class User {
protected:
    string name;
    DocumentSessionMediator* mediator;

public:
    User(string name, DocumentSessionMediator* mediator) : name(name), mediator(mediator) {}

    // Method for users to make a change
    void makeChange(string change) {
        cout << name + " edited the document: " + change << endl;
        mediator->broadcastChange(change, this);
    }

    // Method to receive a change from another user
    void receiveChange(string change, User* sender) {
        cout << name + " saw change from " + sender->name + ": \"" + change + "\"" << endl;
    }
};

// Concrete Mediator Class
class CollaborativeDocument : public DocumentSessionMediator {
private:
    vector<User*> users;

public:
    void join(User* user) override {
        users.push_back(user);
    }

    void broadcastChange(string change, User* sender) override {
        for (User* user : users) {
            if (user != sender) {
                user->receiveChange(change, sender);
            }
        }
    }
};

// Client Code
int main() {
    CollaborativeDocument doc;

    // Creating users
    User alice("Alice", &doc);
    User bob("Bob", &doc);
    User charlie("Charlie", &doc);

    // Joining the collaborative document
    doc.join(&alice);
    doc.join(&bob);
    doc.join(&charlie);

    // Users making changes
    alice.makeChange("Added project title");
    bob.makeChange("Corrected grammar in paragraph 2");

    return 0;
}
