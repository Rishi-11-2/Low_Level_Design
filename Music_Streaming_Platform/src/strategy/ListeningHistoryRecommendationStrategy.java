package strategy;

import model.Song;
import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListeningHistoryRecommendationStrategy implements RecommendationStrategy {

    @Override
    public List<Song> recommend(User user, List<Song> allSongs) {
        List<Song> recommendations = new ArrayList<>();
        Map<String, Integer> counts = user.getPlayCounts();
        if (counts.isEmpty()) {
            return recommendations;
        }

        List<Song> playedSongs = new ArrayList<>();
        for (Song song : allSongs) {
            if (counts.containsKey(song.getId()) && counts.get(song.getId()) > 0) {
                playedSongs.add(song);
            }
        }

        playedSongs.sort((s1, s2) -> counts.get(s2.getId()).compareTo(counts.get(s1.getId())));
        return playedSongs;
    }
}
