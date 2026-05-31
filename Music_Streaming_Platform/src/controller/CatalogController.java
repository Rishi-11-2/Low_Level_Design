package controller;

import model.Album;
import model.Artist;
import model.Song;
import service.CatalogService;
import java.util.List;

public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public Artist registerArtist(String id, String name) {
        return catalogService.registerArtist(id, name);
    }

    public Album registerAlbum(String id, String title, String artistId) {
        return catalogService.registerAlbum(id, title, artistId);
    }

    public Song addSongToAlbum(String songId, String title, String artistId, String albumId, String genre, int durationSeconds) {
        return catalogService.addSongToAlbum(songId, title, artistId, albumId, genre, durationSeconds);
    }

    public List<Song> searchSongs(String query) {
        return catalogService.searchSongs(query);
    }
}
