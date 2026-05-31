# Music Streaming Platform LLD Java Implementation Walkthrough

We have successfully implemented and verified the Low-Level Design (LLD) for the Music Streaming Platform in Java under `/Users/rishi/Projects/LLD/Music_Streaming_Platform/src/`. The design strictly follows the tiered enterprise architecture (Client -> Controller -> Service -> Repository -> Domain) and incorporates the State, Observer, and Strategy design patterns as specified in `Music_Streaming_Platform_System_Design.pdf`.

---

## 1. Package Structure Created
All source files are organized under `/Users/rishi/Projects/LLD/Music_Streaming_Platform/src/`:

```
src/
├── Main.java                        (System bootstrap & end-to-end simulation runner)
├── model/
│   ├── PlaybackStatus.java          (Enum: PLAYING, PAUSED, STOPPED)
│   ├── Song.java                    (Domain model)
│   ├── Album.java                   (Domain model representing artist collections)
│   ├── Artist.java                  (Domain model acting as publisher/subject)
│   ├── User.java                    (Domain model representing users and listeners)
│   ├── Playlist.java                (Domain model for user-curated song groups)
│   ├── Player.java                  (State Pattern: context class managing active playback)
│   ├── PlayerState.java             (State Pattern: state interface)
│   ├── StoppedState.java            (State Pattern: concrete state)
│   ├── PlayingState.java            (State Pattern: concrete state)
│   ├── PausedState.java             (State Pattern: concrete state)
│   ├── SongSubscriber.java          (Observer Pattern: observer interface)
│   ├── EmailNotificationSubscriber.java (Observer Pattern: concrete email broadcaster)
│   └── PushNotificationSubscriber.java  (Observer Pattern: concrete push broadcaster)
├── strategy/
│   ├── RecommendationStrategy.java  (Strategy Pattern: interface)
│   ├── GenreRecommendationStrategy.java (Strategy Pattern: recommend matching genre)
│   └── ListeningHistoryRecommendationStrategy.java (Strategy Pattern: recommend matching history)
├── repository/
│   ├── SongRepository.java          (In-memory storage)
│   ├── ArtistRepository.java        (In-memory storage)
│   ├── AlbumRepository.java         (In-memory storage)
│   ├── UserRepository.java          (In-memory storage)
│   └── PlaylistRepository.java      (In-memory storage)
├── service/
│   ├── CatalogService.java          (Enforces song search, registration, and retrievals)
│   ├── PlaylistService.java         (Manages user playlists and editing)
│   ├── PlaybackService.java         (Controls player clicks, states, and history logs)
│   ├── ArtistNotificationService.java (Coordinates artist alerts to subscribers)
│   └── RecommendationService.java   (Enforces pluggable recommendations)
└── controller/
    ├── CatalogController.java
    ├── PlaylistController.java
    ├── PlaybackController.java
    ├── ArtistNotificationController.java
    └── RecommendationController.java
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **State Design Pattern (Music Player)**:
   - Implements player state management using the `PlayerState` interface and three concrete states: `StoppedState`, `PlayingState`, and `PausedState`.
   - Transitions correctly control status updates and track changes, safely alerting the user if actions (like pausing while stopped) are invalid.
2. **Observer Design Pattern (Artist Releases)**:
   - Observers (registered listeners) subscribe to an `Artist` (the subject).
   - When an artist uploads or releases a single/album, notifications are automatically broadcasted across multiple channels (unconditional `EmailNotificationSubscriber` and screen `PushNotificationSubscriber`).
3. **Strategy Design Pattern (Recommendations)**:
   - Recommendation strategies are pluggable at runtime via `RecommendationStrategy`.
   - Swap easily between genre-based matches (`GenreRecommendationStrategy`) and listening history frequency filters (`ListeningHistoryRecommendationStrategy`).

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/strategy/*.java src/repository/*.java src/service/*.java src/controller/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
==============================================================
       MUSIC STREAMING PLATFORM LOW LEVEL SYSTEM BOOT         
==============================================================

--- Setup: Registering Artists & Users ---
[CatalogService] Registered Artist: Ed Sheeran (ID: ART-ED)
[CatalogService] Registered Album: 'Divide' for Artist: Ed Sheeran
[CatalogService] Added Song: 'Shape of You' (Pop) to Album 'Divide'
[CatalogService] Added Song: 'Perfect' (Pop) to Album 'Divide'
[CatalogService] Added Song: 'Castle on the Hill' (Rock) to Album 'Divide'

--- Flow 1: Registering Observers for Artist Releases ---
[ArtistNotificationService] User 'alice_listener' subscribed to Artist: 'Ed Sheeran'
[ArtistNotificationService] User 'alice_listener' subscribed to Artist: 'Ed Sheeran'

--- Flow 2: Music Playback Operations (State Pattern) ---
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Shape of You'
[PlayerState: Stopped] Starting playback of song: 'Shape of You'
[PlayerState: Playing] Suspending playback of song: 'Shape of You'
[MusicPlayer: DEV-IPHONE-ALICE] Volume adjusted to: 75%
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Shape of You'
[PlayerState: Paused] Resuming playback of song: 'Shape of You'
[PlayerState: Playing] Halting playback of song: 'Shape of You'
[PlayerState: Stopped] Player is already stopped. Cannot pause.

--- Flow 3: Curating Playlists ---
[PlaylistService] Created playlist 'Alice's Pop Faves' (ID: PL-ALICE-POP) for user: alice_listener
[PlaylistService] Added Song ID SNG-SHAPE to Playlist 'Alice's Pop Faves'
[PlaylistService] Added Song ID SNG-PERFECT to Playlist 'Alice's Pop Faves'

--- Flow 4: Artist Releases Single (Observer Notification) ---
[Artist: Ed Sheeran] Releasing new song 'Bad Habits'. Notifying 2 subscribers.
[Email Alert] Sending message to alice@example.com: Ed Sheeran just released a new song: 'Bad Habits' (Pop). Listen now!
[Push Notification] Sending mobile alert to screen of user 'alice_listener': Ed Sheeran dropped a new single: 'Bad Habits'!

--- Flow 5: Pluggable Recommendations (Strategy Pattern) ---
[Genre Recommendation] Fetching Pop recommendations for Alice:
  - Song: 'Shape of You' Genre: Pop
  - Song: 'Bad Habits' Genre: Pop
  - Song: 'Perfect' Genre: Pop

[Playback History] Simulating user play frequencies...
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Castle on the Hill'
[PlayerState: Stopped] Starting playback of song: 'Castle on the Hill'
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Castle on the Hill'
[PlayerState: Playing] Already playing active stream. Switching to new track: 'Castle on the Hill'
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Castle on the Hill'
[PlayerState: Playing] Already playing active stream. Switching to new track: 'Castle on the Hill'
[PlaybackService] User 'alice_listener' listening history incremented for song: 'Shape of You'
[PlayerState: Playing] Already playing active stream. Switching to new track: 'Shape of You'
[RecommendationService] Strategy updated to: ListeningHistoryRecommendationStrategy

[History Recommendation] Fetching history-based recommendations for Alice (Frequency Ranked):
  - Song: 'Shape of You' Plays: 3 Genre: Pop
  - Song: 'Castle on the Hill' Plays: 3 Genre: Rock

==============================================================
       MUSIC STREAMING SYSTEM SIMULATION COMPLETE             
==============================================================
```
