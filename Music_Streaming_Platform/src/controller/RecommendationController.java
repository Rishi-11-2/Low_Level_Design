package controller;

import model.Song;
import service.RecommendationService;
import strategy.RecommendationStrategy;
import java.util.List;

public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    public void updateStrategy(RecommendationStrategy strategy) {
        recommendationService.setStrategy(strategy);
    }

    public List<Song> getRecommendations(String userId) {
        return recommendationService.getRecommendations(userId);
    }
}
