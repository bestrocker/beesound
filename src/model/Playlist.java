package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable{
    
    private final String name;
    private final List<Song> trackList;
    private String path;
    
    /**
     * Constructor with two parameters: the name of the playlist to create and its path in the file system.
     * @param name 
     * @param path 
     */
    public Playlist(final String name, final String path) {
        this.name = name;
        this.trackList = new ArrayList<>();
        this.path = path;
    }
    
    public class Playing extends Playlist {
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        //private final String QUEUE_NAME = "Reproduction_Queue";
        private Song songInReproduction;
        
        public Playing(String name, String path) {
            super(name, path);
            this.songInReproduction = null;
        }
        
        /**
         * Returns the number of elements inside the reproduction queue.
         * @return the number of elements inside the reproduction queue.
         */
        public int getQueueLenght() {
            return super.trackList.size();
        }
        
        public Song getSongInReproduction() {
            return this.songInReproduction;
        }
    }

    /**
     * Returns the path in which the playlist is stored.
     * @return the path in which the playlist is stored.
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     * Returns the name of this playlist.
     * @return the name of this playlist.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the list of the songs in this playlist.
     * @return the list of the songs in this playlist.
     */
    public List<Song> getTrackList() {
        return this.trackList;
    }
    
    /**
     * Adds a song to this playlist.
     * @param song - the song to add to the playlist.
     */
    public void addSong(final Song song) {
        this.trackList.add(song);
    }
    
    /**
     * Removes a song from this playlist.
     * @param song - the song to remove from this playlist.
     */
    public void removeSong(final Song song) {
        this.trackList.remove(song);
    }    
}