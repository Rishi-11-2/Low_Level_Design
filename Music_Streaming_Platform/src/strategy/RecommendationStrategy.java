package strategy;

import model.Song;
import model.User;
import java.util.List;

public interface RecommendationStrategy {
    List<Song> recommend(User user, List<Song> allSongs);
}
