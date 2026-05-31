package repository;

import model.Song;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongRepository {
    private final Map<String, Song> songs = new HashMap<>();

    public void save(Song song) {
        songs.put(song.getId(), song);
    }

    public Song findById(String id) {
        return songs.get(id);
    }

    public List<Song> findAll() {
        return new ArrayList<>(songs.values());
    }
}
