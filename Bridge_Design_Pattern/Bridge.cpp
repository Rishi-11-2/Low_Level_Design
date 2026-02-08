#include <bits/stdc++.h>
using namespace std;

// ======== Implementor Interface =========
class VideoQuality {
public:
    virtual void load(const string& title) = 0;
    virtual ~VideoQuality() {}
};

// ============ Concrete Implementors ==============
class SDQuality : public VideoQuality {
public:
    void load(const string& title) override {
        cout << "Streaming " << title << " in SD Quality" << endl;
    }
};

class HDQuality : public VideoQuality {
public:
    void load(const string& title) override {
        cout << "Streaming " << title << " in HD Quality" << endl;
    }
};

class UltraHDQuality : public VideoQuality {
public:
    void load(const string& title) override {
        cout << "Streaming " << title << " in 4K Ultra HD Quality" << endl;
    }
};

// ========== Abstraction ==========
class VideoPlayer {
protected:
    VideoQuality* quality;

public:
    VideoPlayer(VideoQuality* quality) : quality(quality) {}
    virtual void play(const string& title) = 0;
    virtual ~VideoPlayer() {
        delete quality;
    }
};

// =========== Refined Abstractions ==============
class WebPlayer : public VideoPlayer {
public:
    WebPlayer(VideoQuality* quality) : VideoPlayer(quality) {}

    void play(const string& title) override {
        cout << "Web Platform:" << endl;
        quality->load(title);
    }
};

class MobilePlayer : public VideoPlayer {
public:
    MobilePlayer(VideoQuality* quality) : VideoPlayer(quality) {}

    void play(const string& title) override {
        cout << "Mobile Platform:" << endl;
        quality->load(title);
    }
};

// Client Code
int main() {
    // Playing on Web with HD Quality
    VideoPlayer* player1 = new WebPlayer(new HDQuality());
    player1->play("Interstellar");

    // Playing on Mobile with Ultra HD Quality
    VideoPlayer* player2 = new MobilePlayer(new UltraHDQuality());
    player2->play("Inception");

    delete player1;
    delete player2;

    return 0;
}
