package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    
    private static String DEFAULT = "Unknown";
    
    private String title;
    private List<Song> trackList;
    
    public Album() {
        this.title = DEFAULT;
        this.trackList = new ArrayList<>();
    }
    
    public Album(String title, List<Song> trackList) {
        this.title = title;
        this.trackList = trackList;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Song> getTrackList() {
        return this.trackList;
    }
    
    public void addSong(Song song) {
        this.trackList.add(song);
    }
    
    public void removeSong(Song song) {
        this.trackList.remove(song);
    }
}
