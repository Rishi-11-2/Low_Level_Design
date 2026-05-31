package repository;

import model.Album;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumRepository {
    private final Map<String, Album> albums = new HashMap<>();

    public void save(Album album) {
        albums.put(album.getId(), album);
    }

    public Album findById(String id) {
        return albums.get(id);
    }

    public List<Album> findAll() {
        return new ArrayList<>(albums.values());
    }
}
