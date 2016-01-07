package model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    
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

    public Playlist(final String name, final List<Song> trackList) {
        this.name = name;
        this.trackList = trackList;
    }

    public String getPath() {
        return this.path;
    }
    
    public String getName() {
        return this.name;
    }

    public List<Song> getTrackList() {
        return this.trackList;
    }
    
    public void addSong(final Song song) {
        this.trackList.add(song);
    }
    
    public void removeSong(final Song song) {
        this.trackList.remove(song);
    }    
}