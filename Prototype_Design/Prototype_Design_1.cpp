#include <bits/stdc++.h>
using namespace std;

// Defining the Prototype Interface
class EmailTemplate {
public:
    virtual EmailTemplate* clone() const = 0; // Recommended to perform deep copy
    virtual void setContent(const string& content) = 0;
    virtual void send(const string& to) const = 0;
    virtual ~EmailTemplate() {}
};

// Concrete Class implementing clone logic
class WelcomeEmail : public EmailTemplate {
private:
    string subject;
    string content;

public:
    WelcomeEmail() {
        subject = "Welcome to TUF+";
        content = "Hi there! Thanks for joining us.";
    }

    WelcomeEmail(const WelcomeEmail& other) {
        subject = other.subject;
        content = other.content;
    }

    EmailTemplate* clone() const override {
        return new WelcomeEmail(*this);
    }

    void setContent(const string& content) override {
        this->content = content;
    }

    void send(const string& to) const override {
        cout << "Sending to " << to << ": [" << subject << "] " << content << endl;
    }
};

// Template Registry to store and provide clones
class EmailTemplateRegistry {
private:
    static unordered_map<string, EmailTemplate*> templates;

public:
    static void init() {
        templates["welcome"] = new WelcomeEmail();
        // templates["discount"] = new DiscountEmail();
        // templates["feature-update"] = new FeatureUpdateEmail();
    }

    static EmailTemplate* getTemplate(const string& type) {
        return templates[type]->clone(); // clone to avoid modifying original
    }

    static void cleanup() {
        for (auto& pair : templates) {
            delete pair.second;
        }
    }
};

unordered_map<string, EmailTemplate*> EmailTemplateRegistry::templates;

int main() {
    EmailTemplateRegistry::init();

    EmailTemplate* welcomeEmail1 = EmailTemplateRegistry::getTemplate("welcome");
    welcomeEmail1->setContent("Hi Alice, welcome to TUF Premium!");
    welcomeEmail1->send("alice@example.com");

    EmailTemplate* welcomeEmail2 = EmailTemplateRegistry::getTemplate("welcome");
    welcomeEmail2->setContent("Hi Bob, thanks for joining!");
    welcomeEmail2->send("bob@example.com");

    // Reuse the base WelcomeEmail structure, just changing dynamic content

    delete welcomeEmail1;
    delete welcomeEmail2;
    EmailTemplateRegistry::cleanup();

    return 0;
}
