package model;

import java.util.ArrayList;
import java.util.List;

public class Album {
     
    private final String title;
    private final List<Song> trackList;
    
    public Album(final String title) {        // this constructor is called in LibraryManager
        this.title = title;
        this.trackList = new ArrayList<>();
    }
    
    public Album(final String title, final List<Song> trackList) {
        this.title = title;
        this.trackList = trackList;
    }

    public String getTitle() {
        return this.title;
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
