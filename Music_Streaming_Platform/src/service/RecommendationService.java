package service;

import model.Song;
import model.User;
import repository.SongRepository;
import repository.UserRepository;
import strategy.RecommendationStrategy;
import java.util.List;

public class RecommendationService {
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private RecommendationStrategy strategy;

    public RecommendationService(SongRepository songRepository, UserRepository userRepository, RecommendationStrategy strategy) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.strategy = strategy;
    }

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
        System.out.println("[RecommendationService] Strategy updated to: " + strategy.getClass().getSimpleName());
    }

    public List<Song> getRecommendations(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User ID " + userId + " does not exist.");
        }
        List<Song> allSongs = songRepository.findAll();
        return strategy.recommend(user, allSongs);
    }
}
