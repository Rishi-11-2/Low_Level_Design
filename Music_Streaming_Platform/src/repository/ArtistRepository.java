package repository;

import model.Artist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistRepository {
    private final Map<String, Artist> artists = new HashMap<>();

    public void save(Artist artist) {
        artists.put(artist.getId(), artist);
    }

    public Artist findById(String id) {
        return artists.get(id);
    }

    public List<Artist> findAll() {
        return new ArrayList<>(artists.values());
    }
}
