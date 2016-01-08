package model;

import Controller.Audio.MpegInfo.Duration;

public class Song {
       
    private final String title;
    private final String album;
    private final String artist;
    private final String genre;
    private final Duration duration;    
    private final int bitRate;
    private int reproductionsCounter;
    private final long size;
    private final String path;
    
    /**
     * Constructor for class Song. 
     * @param title
     * @param album
     * @param artist
     * @param genre
     * @param duration
     * @param bitRate 
     * @param size 
     * @param path 
     */
    public Song(final String title, final String album, final String artist, final String genre, 
            final Duration duration, final int bitRate, final long size, final String path) {
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
        private Duration duration;    
        private int bitRate;
        private long size;
        private String path;     
        
        public Builder title(final String title) {
            this.title = title;
            return this;
        }
        
        public Builder album(final String album) {
            this.album = album;
            return this;
        }
        
        public Builder artist(final String artist) {
            this.artist = artist;
            return this;
        }
        
        public Builder genre(final String genre) {
            this.genre = genre;
            return this;            
        }
        
        public Builder duration(final Duration duration) {
            this.duration = duration;
            return this;
        }
        
        public Builder bitRate(final int bitRate) {
            this.bitRate = bitRate;
            return this;            
        }
        
        public Builder size(final long size) {
            this.size = size;
            return this;
        }
        
        public Builder path(final String path) {
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

    public Duration getDuration() {
        return this.duration;
    }

    public String getPath() {
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