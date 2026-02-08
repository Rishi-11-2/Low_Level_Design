#include <bits/stdc++.h>
using namespace std;

// ========== Video class representing a single video ==========
class Video {
    string title;

public:
    Video(const string& t) : title(t) {}

    string getTitle() const {
        return title;
    }
};

// ========== YouTubePlaylist class (Aggregate) ==========
class YouTubePlaylist {
    vector<Video> videos;

public:
    // Method to add video to playlist
    void addVideo(const Video& video) {
        videos.push_back(video);
    }

    // Method to expose internal video list 
    vector<Video>& getVideos() {
        return videos;
    }
};

// ========== Iterator interface ==========
class PlaylistIterator {
public:
    virtual bool hasNext() = 0;
    virtual Video* next() = 0;
    virtual ~PlaylistIterator() = default;
};

// ========== Concrete Iterator class ==========
class YouTubePlaylistIterator : public PlaylistIterator {
    vector<Video>* videos;
    size_t position;

public:
    // Constructor takes the list to iterate on
    YouTubePlaylistIterator(vector<Video>* vids) {
        videos = vids;
        position = 0;
    }

    // Check if more videos are left to iterate
    bool hasNext() override {
        return position < videos->size();
    }

    // Return the next video in sequence
    Video* next() override {
        return hasNext() ? &(*videos)[position++] : nullptr;
    }
};

// ========== Main method (Client code) ==========
int main() {
    // Create a playlist and add videos
    YouTubePlaylist playlist;
    playlist.addVideo(Video("LLD Tutorial"));
    playlist.addVideo(Video("System Design Basics"));

    // Client directly creates the iterator using internal list (not ideal)
    PlaylistIterator* iterator = new YouTubePlaylistIterator(&playlist.getVideos());

    // Use the iterator to loop through the playlist
    while (iterator->hasNext()) {
        cout << iterator->next()->getTitle() << endl;
    }

    delete iterator;
    return 0;
}
