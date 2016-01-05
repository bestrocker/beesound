package model;

import java.nio.file.Path;
import java.util.Optional;

public class Song {
       
    private String title;
    private Optional<String> album;
    private Optional<String> artist;
    private Optional<String> genre;
    private Optional<String> year;    
    private double duration;    
    private int bitRate;
    private int reproductionsCounter;
    private long size;
    private Path path;

    /*public Song() {
        this.title = DEFAULT;
        this.album = DEFAULT;
        this.artist = DEFAULT;
        this.genre = DEFAULT;
    }*/    
    
    public Song(String title, Optional<String> album, Optional<String> artist, Optional<String> genre, 
            Optional<String> year, double duration, int bitRate, long size, Path path) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.bitRate = bitRate;
        this.reproductionsCounter = 0;
        this.size = size;
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<String> getAlbum() {
        return this.album;
    }

    public void setAlbum(Optional<String> album) {
        this.album = album;
    }

    public Optional<String> getArtist() {
        return this.artist;
    }

    public void setArtist(Optional<String> artist) {
        this.artist = artist;
    }

    public Optional<String> getGenre() {
        return this.genre;
    }

    public void setGenre(Optional<String> genre) {
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

    public Optional<String> getYear() {
        return this.year;
    }
    
    public void setYear(Optional<String> year) {
        this.year = year;
    }
    
    public int getBitRate() {
        return this.bitRate;
    }
    
    public void setBitRate(int bitRate) {
        this.bitRate = bitRate; 
    }
    
    public int getReproductionsCounter() {
        return this.reproductionsCounter;
    }
    
    public void incrementCounter() {
        this.reproductionsCounter++;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
}
