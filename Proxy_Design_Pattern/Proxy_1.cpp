#include <bits/stdc++.h>    
using namespace std;

// Interface (in Java it's an interface, in C++ we use an abstract base class)
class VideoDownloader {
public:
    virtual string downloadVideo(string videoUrl) = 0;
    virtual ~VideoDownloader() = default;
};

// ========== RealVideoDownloader Class ==========
class RealVideoDownloader : public VideoDownloader {
public:
    string downloadVideo(string videoUrl) override {
        cout << "Downloading video from URL: " << videoUrl << endl;
        return "Video content from " + videoUrl;
    }
};

// =============== Proxy With Cache ====================
class CachedVideoDownloader : public VideoDownloader {
private:
    RealVideoDownloader realDownloader;
    unordered_map<string, string> cache;

public:
    string downloadVideo(string videoUrl) override {
        if (cache.find(videoUrl) != cache.end()) {
            cout << "Returning cached video for: " << videoUrl << endl;
            return cache[videoUrl];
        }

        cout << "Cache miss. Downloading..." << endl;
        string video = realDownloader.downloadVideo(videoUrl);
        cache[videoUrl] = video;
        return video;
    }
};

// ================ Main Function ===================
int main() {
    VideoDownloader* cacheVideoDownloader = new CachedVideoDownloader();

    cout << "User 1 tries to download the video." << endl;
    cacheVideoDownloader->downloadVideo("https://video.com/proxy-pattern");

    cout << endl;

    cout << "User 2 tries to download the same video again." << endl;
    cacheVideoDownloader->downloadVideo("https://video.com/proxy-pattern");

    delete cacheVideoDownloader;
    return 0;
}
