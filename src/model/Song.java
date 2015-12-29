package model;

import java.nio.file.Path;

public class Song {
    
    private static String DEFAULT = "Unknown";
    
    private String title;
    private String album;
    private String artist;
    private String genre;
    private double duration;
    private Path path;

    public Song() {
        this.title = DEFAULT;
        this.album = DEFAULT;
        this.artist = DEFAULT;
        this.genre = DEFAULT;
    }    
    
    public Song(String title, String album, String artist, String genre, double duration, Path path) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Path getPath() {
        return this.path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
