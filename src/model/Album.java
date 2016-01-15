package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Representation of an album inside the music library.
 * @author tiziano
 *
 */
public class Album {
     
    private final String title;
    private final List<Song> trackList;
    
    /**
     * Constructs an album with the specified title and an empty list of tracks.
     * @param title - the title of the album to create.
     */
    public Album(final String title) {        // this constructor is called in LibraryManager
        this.title = title;
        this.trackList = new LinkedList<>();
    }

    /**
     * Returns the title of the album.
     * @return the title of the album.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the list of the songs in the album. 
     * @return list of the songs in the field trackList.
     */
    public List<Song> getTrackList() {
        return this.trackList;
    }
    
    /**
     * Adds a new song to the album.
     * @param song - the song to insert in the album.
     */
    public void addSong(final Song song) {
        this.trackList.add(song);
    }
    
    /**
     * Removes a song from the album.
     * @param song - the song to remove from the album.
     */
    public void removeSong(final Song song) {
        this.trackList.remove(song);
    }
}