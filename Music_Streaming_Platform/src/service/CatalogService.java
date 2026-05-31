package service;

import model.Album;
import model.Artist;
import model.Song;
import repository.AlbumRepository;
import repository.ArtistRepository;
import repository.SongRepository;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public CatalogService(SongRepository songRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public Artist registerArtist(String id, String name) {
        Artist artist = new Artist(id, name);
        artistRepository.save(artist);
        System.out.println("[CatalogService] Registered Artist: " + name + " (ID: " + id + ")");
        return artist;
    }

    public Album registerAlbum(String id, String title, String artistId) {
        Artist artist = artistRepository.findById(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist ID " + artistId + " does not exist.");
        }
        Album album = new Album(id, title, artistId);
        albumRepository.save(album);
        artist.addAlbum(id);
        System.out.println("[CatalogService] Registered Album: '" + title + "' for Artist: " + artist.getName());
        return album;
    }

    public Song addSongToAlbum(String songId, String title, String artistId, String albumId, String genre, int durationSeconds) {
        Artist artist = artistRepository.findById(artistId);
        Album album = albumRepository.findById(albumId);
        if (artist == null || album == null) {
            throw new IllegalArgumentException("Invalid artist or album ID.");
        }

        Song song = new Song(songId, title, artistId, albumId, genre, durationSeconds);
        songRepository.save(song);
        album.addSong(songId);
        artist.addSong(songId);
        System.out.println("[CatalogService] Added Song: '" + title + "' (" + genre + ") to Album '" + album.getTitle() + "'");
        return song;
    }

    public List<Song> searchSongs(String query) {
        List<Song> results = new ArrayList<>();
        for (Song song : songRepository.findAll()) {
            if (song.getTitle().toLowerCase().contains(query.toLowerCase()) || song.getGenre().toLowerCase().contains(query.toLowerCase())) {
                results.add(song);
            }
        }
        return results;
    }

    public Song findSongById(String id) {
        return songRepository.findById(id);
    }
}
