package model;

public class Song {
    private final String id;
    private final String title;
    private final String artistId;
    private final String albumId;
    private final String genre;
    private final int durationSeconds;

    public Song(String id, String title, String artistId, String albumId, String genre, int durationSeconds) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.albumId = albumId;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }
}
