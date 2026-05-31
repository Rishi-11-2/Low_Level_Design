package strategy;

import model.Song;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class GenreRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Song> recommend(User user, List<Song> allSongs) {
        List<Song> recommendations = new ArrayList<>();
        String favGenre = user.getFavoriteGenre();
        if (favGenre == null) {
            return recommendations;
        }

        for (Song song : allSongs) {
            if (favGenre.equalsIgnoreCase(song.getGenre())) {
                recommendations.add(song);
            }
        }
        return recommendations;
    }
}
