package model;

import java.awt.Dimension;
import java.nio.file.Path;
import java.util.Optional;

public class Song {
       
    private String title;
    private String album;
    private String artist;
    private String genre;
    private Dimension duration;    
    private int bitRate;
    private int reproductionsCounter;
    private long size;
    private Path path;
   
    public Song(String title, String album, String artist, String genre, 
            Dimension duration, int bitRate, long size, Path path) {
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
    
    public static class Builder {       // Builder pattern is implemented here 
        
        private String title;
        private String album;
        private String artist;
        private String genre;
        private Dimension duration;    
        private int bitRate;
        //private int reproductionsCounter = 0;
        private long size;
        private Path path;     
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder album(String album) {
            this.album = album;
            return this;
        }
        
        public Builder artist(String artist) {
            this.artist = artist;
            return this;
        }
        
        public Builder genre(String genre) {
            this.genre = genre;
            return this;            
        }
        
        public Builder duration(Dimension duration) {
            this.duration = duration;
            return this;
        }
        
        public Builder bitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;            
        }
        
        public Builder size(long size) {
            this.size = size;
            return this;
        }
        
        public Builder path(Path path) {
            this.path = path;
            return this;
        }
        
        public Song build() {
            return new Song(this.title, this.album, this.artist, this.genre, this.duration, 
                    this.bitRate, this.size, this.path);
        }       
    }

    public String getTitle() {
        return this.title;
    }

    public String getAlbum() {
        return this.album;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getGenre() {
        return this.genre;
    }

    public Dimension getDuration() {
        return this.duration;
    }

    public Path getPath() {
        return this.path;
    }

    public int getBitRate() {
        return this.bitRate;
    }
    
    public int getReproductionsCounter() {
        return this.reproductionsCounter;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public void incrementCounter() {
        this.reproductionsCounter++;
    }
}
