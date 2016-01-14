package model;

import Controller.Audio.MpegInfo.Duration;

/**
 * Represents a song in the music library.
 * @author tiziano
 *
 */
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
    private final boolean copyright;
    private final int channel;
    private final String version;
    private final int rate;
    private final String channelsMode;
    private boolean isPaused;
    
    /**
     * Constructs a new song with the specified parameters. The reproductionsCounter field is initialized to 0. 
     * @param title - the title of the song to create.
     * @param album - the album in which the song is included.
     * @param artist - the artist who created the song.
     * @param genre - the genre which the song belongs to. 
     * @param duration - the duration of the song.
     * @param bitRate - bit-rate of the song.
     * @param size - the size of the song expressed in byte.
     * @param path - the path of the file system in which the song is stored.
     */
    public Song(final String title, final String album, final String artist, final String genre, 
            final Duration duration, final int bitRate, final long size, final String path, boolean copyright,
            int channel, String version, int rate, String channelsMode) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.bitRate = bitRate;
        this.reproductionsCounter = 0;
        this.size = size;
        this.path = path;
        this.copyright = copyright;
        this.channel = channel;
        this.version = version;
        this.rate = rate;
        this.channelsMode = channelsMode;
        this.isPaused = false;
    }
    
    /**
     * An inner class which models the Builder pattern.
     * @author tiziano
     */
    public static class Builder {       // Builder pattern is implemented here 
        
        private String title;
        private String album;
        private String artist;
        private String genre;
        private Duration duration;    
        private int bitRate;
        private long size;
        private String path;  
        private boolean copyright;
        private int channel;
        private String version;
        private int rate;
        private String channelsMode;
        
        /**
         * Sets the specified title and returns this builder. 
         * @param title - the title to set in this builder.
         * @return this builder.
         */
        public Builder title(final String title) {
            this.title = title;
            return this;
        }
        
        /**
         * Sets the specified album title and returns this builder.
         * @param album - the title of the album to set in this builder.
         * @return this builder.
         */
        public Builder album(final String album) {
            this.album = album;
            return this;
        }
        
        /**
         * Sets the specified artist and returns this builder.
         * @param artist - the artist to set in this builder.
         * @return this builder.
         */
        public Builder artist(final String artist) {
            this.artist = artist;
            return this;
        }
        
        /**
         * Sets the specified genre and returns this builder.
         * @param genre - the genre to set in this builder.
         * @return this builder.
         */
        public Builder genre(final String genre) {
            this.genre = genre;
            return this;            
        }
        
        /**
         * Sets the specified duration and returns this builder.
         * @param duration - the duration of the song to set in this builder.
         * @return this builder.
         */
        public Builder duration(final Duration duration) {
            this.duration = duration;
            return this;
        }
        
        /**
         * Sets the specified bit-rate and returns this builder
         * @param bitRate - the bit-rate of the song to set in this builder.
         * @return this builder.
         */
        public Builder bitRate(final int bitRate) {
            this.bitRate = bitRate;
            return this;            
        }
        
        /**
         * Sets the specified size and returns this builder,
         * @param size - the size of the song to set in this builder.
         * @return this builder.
         */
        public Builder size(final long size) {
            this.size = size;
            return this;
        }
        
        /**
         * Sets the specified path and returns this builder.
         * @param path - the path of the song to set in this builder.
         * @return this builder.
         */
        public Builder path(final String path) {
            this.path = path;
            return this;
        }
        
        public Builder copyright(final boolean copyright) {
            this.copyright = copyright;
            return this;
        }
        
        public Builder channel(final int channel) {
            this.channel = channel;
            return this;
        }
        
        public Builder version(final String version) {
            this.version = version;
            return this;
        }
        
        public Builder rate(final int rate) {
            this.rate = rate;
            return this;
        }
        
        public Builder channelsMode(final String channelsMode) {
            this.channelsMode = channelsMode;
            return this;
        }
        
        /**
         * Builds a new Song using the fields of this builder.
         * @return a new Song initialized with the values contained in the fields of this builder.
         */
        public Song build() {
            return new Song(this.title, this.album, this.artist, this.genre, this.duration,this.bitRate, 
                    this.size, this.path, this.copyright, this.channel, this.version, this.rate, this.channelsMode);
        }
    }
    
    /**
     * Returns the tile of this song.
     * @return the tile of this song.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the title of the album which this song belongs to.
     * @return the title of the album which this song belongs to.
     */
    public String getAlbum() {
        return this.album;
    }

    /**
     * Returns the name of the artist who created this song.
     * @return the name of the artist who created this song.
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * Returns the genre which this song belongs to.
     * @return the genre which this song belongs to.
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * Returns the duration of this song.
     * @return the duration of this song.
     */
    public Duration getDuration() {
        return this.duration;
    }

    /**
     * Returns the string representation of the path in which this song is stored.
     * @return the string representation of the path in which this song is stored.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Returns the bit-rate of this song.
     * @return the bit-rate of this song.
     */
    public int getBitRate() {
        return this.bitRate;
    }
    
    /**
     * Returns the amount of reproductions for this song.
     * @return the amount of reproductions for this song.
     */
    public int getReproductionsCounter() {
        return this.reproductionsCounter;
    }
    
    /**
     * Returns the size of this song expressed in bytes.
     * @return the size of this song expressed in bytes.
     */
    public long getSize() {
        return this.size;
    }
    
    public boolean getCopyright() {
        return this.copyright;
    }
    
    public int getChannel() {
        return this.channel;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public int getRate() {
        return this.rate;
    }
    
    public String getChannelsMode() {
        return this.channelsMode;
    }
    
    public boolean getIsPaused() {
        return this.isPaused;
    }
    
    public void setSongPaused(boolean pause) {
        this.isPaused = pause;
    }
    
    /**
     * Increments the counter of the reprodutions for this song by one unit.
     */
    public void incrementCounter() {
        this.reproductionsCounter++;
    }
}