#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

// ==============================
// Observer Interface
// ==============================
class Subscriber {
public:
    virtual void update(const string& videoTitle) = 0;
    virtual ~Subscriber() {}
};

// ==============================
// Concrete Observer: Email
// ==============================
class EmailSubscriber : public Subscriber {
private:
    string email;

public:
    EmailSubscriber(const string& email) {
        this->email = email;
    }

    void update(const string& videoTitle) override {
        cout << "Email sent to " << email << ": New video uploaded - " << videoTitle << endl;
    }
};

// ==============================
// Concrete Observer: Mobile App
// ==============================
class MobileAppSubscriber : public Subscriber {
private:
    string username;

public:
    MobileAppSubscriber(const string& username) {
        this->username = username;
    }

    void update(const string& videoTitle) override {
        cout << "In-app notification for " << username << ": New video - " << videoTitle << endl;
    }
};

// ==============================
// Subject Interface
// ==============================
class Channel {
public:
    virtual void subscribe(Subscriber* subscriber) = 0;
    virtual void unsubscribe(Subscriber* subscriber) = 0;
    virtual void notifySubscribers(const string& videoTitle) = 0;
    virtual ~Channel() {}
};

// ==============================
// Concrete Subject: YouTubeChannel
// ==============================
class YouTubeChannel : public Channel {
private:
    vector<Subscriber*> subscribers;
    string channelName;

public:
    YouTubeChannel(const string& channelName) {
        this->channelName = channelName;
    }

    void subscribe(Subscriber* subscriber) override {
        subscribers.push_back(subscriber);
    }

    void unsubscribe(Subscriber* subscriber) override {
        subscribers.erase(std::remove(subscribers.begin(), subscribers.end(), subscriber), subscribers.end());
    }

    void notifySubscribers(const string& videoTitle) override {
        for (Subscriber* subscriber : subscribers) {
            subscriber->update(videoTitle);
        }
    }

    // Simulates video upload and triggers notifications
    void uploadVideo(const string& videoTitle) {
        cout << channelName << " uploaded: " << videoTitle << "\n" << endl;
        notifySubscribers(videoTitle);
    }
};

// ==============================
// Client Code
// ==============================
int main() {
    YouTubeChannel tuf("takeUforward");

    // Add subscribers
    MobileAppSubscriber* raj = new MobileAppSubscriber("raj");
    EmailSubscriber* rahul = new EmailSubscriber("rahul@example.com");

    tuf.subscribe(raj);
    tuf.subscribe(rahul);

    // Upload video and notify all observers
    tuf.uploadVideo("observer-pattern");

    // Cleanup
    delete raj;
    delete rahul;

    return 0;
}
