package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an artist in the music library.
 * @author tiziano
 *
 */
public class Artist {
       
    private final String name;
    private final List<Album> albumList;
    
    /**
     * Constructs an artist with the specified name. 
     * @param name - the name of the artist to create.
     */
    public Artist(final String name) {
        this.name = name;
        this.albumList = new ArrayList<>();
    }

    /**
     * Returns the name of the artist.
     * @return the name of the artist.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the list of the albums of this artist.
     * @return the list of the albums if this artist.
     */
    public List<Album> getAlbumList() {
        return this.albumList;
    }
    
    /**
     * Adds an album to the album list of this artist.
     * @param album - the album to add to the list.
     */
    public void addAlbum(final Album album) {
        this.albumList.add(album);
    }
    
    /**
     * Removes an album from the album list of this artist.
     * @param album - the album to remove from the list.
     */
    public void removeAlbum(final Album album) {
        this.albumList.remove(album);
    }
}
