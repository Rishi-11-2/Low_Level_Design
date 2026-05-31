import controller.*;
import model.*;
import repository.*;
import service.*;
import strategy.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("       MUSIC STREAMING PLATFORM LOW LEVEL SYSTEM BOOT         ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        SongRepository songRepository = new SongRepository();
        ArtistRepository artistRepository = new ArtistRepository();
        AlbumRepository albumRepository = new AlbumRepository();
        UserRepository userRepository = new UserRepository();
        PlaylistRepository playlistRepository = new PlaylistRepository();

        // 2. Initialize Services
        CatalogService catalogService = new CatalogService(songRepository, artistRepository, albumRepository);
        PlaylistService playlistService = new PlaylistService(playlistRepository, userRepository);
        PlaybackService playbackService = new PlaybackService(userRepository);
        ArtistNotificationService notificationService = new ArtistNotificationService(artistRepository, userRepository);
        
        RecommendationStrategy genreStrategy = new GenreRecommendationStrategy();
        RecommendationService recommendationService = new RecommendationService(songRepository, userRepository, genreStrategy);

        // 3. Initialize Controllers
        CatalogController catalogController = new CatalogController(catalogService);
        PlaylistController playlistController = new PlaylistController(playlistService);
        PlaybackController playbackController = new PlaybackController(playbackService);
        ArtistNotificationController notificationController = new ArtistNotificationController(notificationService);
        RecommendationController recommendationController = new RecommendationController(recommendationService);

        // 4. Register Users and Artists
        System.out.println("\n--- Setup: Registering Artists & Users ---");
        User alice = new User("USR-ALICE", "alice_listener", "alice@example.com", "Pop");
        userRepository.save(alice);

        Artist ed = catalogController.registerArtist("ART-ED", "Ed Sheeran");
        Album divide = catalogController.registerAlbum("ALB-DIVIDE", "Divide", ed.getId());

        // 5. Add Songs
        Song shapeOfYou = catalogController.addSongToAlbum("SNG-SHAPE", "Shape of You", ed.getId(), divide.getId(), "Pop", 233);
        Song perfect = catalogController.addSongToAlbum("SNG-PERFECT", "Perfect", ed.getId(), divide.getId(), "Pop", 263);
        Song castle = catalogController.addSongToAlbum("SNG-CASTLE", "Castle on the Hill", ed.getId(), divide.getId(), "Rock", 261);

        // 6. User Subscription Flow (Observer Pattern)
        System.out.println("\n--- Flow 1: Registering Observers for Artist Releases ---");
        SongSubscriber emailSub = new EmailNotificationSubscriber(alice.getEmail());
        SongSubscriber pushSub = new PushNotificationSubscriber(alice.getUsername());

        notificationController.subscribeUser(alice.getId(), ed.getId(), emailSub);
        notificationController.subscribeUser(alice.getId(), ed.getId(), pushSub);

        // 7. Playback Simulation (State Pattern)
        System.out.println("\n--- Flow 2: Music Playback Operations (State Pattern) ---");
        Player player = new Player("DEV-IPHONE-ALICE");

        playbackController.playSong(player, shapeOfYou, alice.getId());
        playbackController.pauseSong(player);
        playbackController.adjustVolume(player, 75);
        playbackController.playSong(player, shapeOfYou, alice.getId());
        playbackController.stopSong(player);
        playbackController.pauseSong(player);

        // 8. Playlist Management
        System.out.println("\n--- Flow 3: Curating Playlists ---");
        Playlist playlist = playlistController.createPlaylist("PL-ALICE-POP", "Alice's Pop Faves", alice.getId());
        playlistController.addSongToPlaylist(playlist.getId(), shapeOfYou.getId());
        playlistController.addSongToPlaylist(playlist.getId(), perfect.getId());

        // 9. Artist releases a new single (Observer Notification Trigger)
        System.out.println("\n--- Flow 4: Artist Releases Single (Observer Notification) ---");
        Song badHabits = new Song("SNG-HABITS", "Bad Habits", ed.getId(), divide.getId(), "Pop", 231);
        ed.addSong(badHabits.getId());
        songRepository.save(badHabits);
        
        notificationController.releaseSong(ed.getId(), badHabits);

        // 10. Pluggable Recommendations (Strategy Pattern)
        System.out.println("\n--- Flow 5: Pluggable Recommendations (Strategy Pattern) ---");
        System.out.println("[Genre Recommendation] Fetching Pop recommendations for Alice:");
        List<Song> recsGenre = recommendationController.getRecommendations(alice.getId());
        for (Song s : recsGenre) {
            System.out.println("  - Song: '" + s.getTitle() + "' Genre: " + s.getGenre());
        }

        System.out.println("\n[Playback History] Simulating user play frequencies...");
        playbackController.playSong(player, castle, alice.getId());
        playbackController.playSong(player, castle, alice.getId());
        playbackController.playSong(player, castle, alice.getId());
        playbackController.playSong(player, shapeOfYou, alice.getId());

        RecommendationStrategy historyStrategy = new ListeningHistoryRecommendationStrategy();
        recommendationController.updateStrategy(historyStrategy);

        System.out.println("\n[History Recommendation] Fetching history-based recommendations for Alice (Frequency Ranked):");
        List<Song> recsHistory = recommendationController.getRecommendations(alice.getId());
        for (Song s : recsHistory) {
            int plays = alice.getPlayCounts().getOrDefault(s.getId(), 0);
            System.out.println("  - Song: '" + s.getTitle() + "' Plays: " + plays + " Genre: " + s.getGenre());
        }

        System.out.println("\n==============================================================");
        System.out.println("       MUSIC STREAMING SYSTEM SIMULATION COMPLETE             ");
        System.out.println("==============================================================");
    }
}
