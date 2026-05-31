package repository;

import model.Playlist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistRepository {
    private final Map<String, Playlist> playlists = new HashMap<>();

    public void save(Playlist playlist) {
        playlists.put(playlist.getId(), playlist);
    }

    public Playlist findById(String id) {
        return playlists.get(id);
    }

    public List<Playlist> findAll() {
        return new ArrayList<>(playlists.values());
    }
}
