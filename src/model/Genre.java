package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Representation of a genre in the music library.
 */
public class Genre {

    private final String name;
    private final List<Album> albumList;
    private final List<Artist> artistList;

    /**
     * Constructs a genre with the specified name and two empty lists for albums and artists.
     * @param name - the name of the genre to create.
     */
    public Genre(final String name) {
        this.name = name;
        this.albumList = new LinkedList<>();
        this.artistList = new LinkedList<>();
    }

    /**
     * Returns the name of this genre.
     * @return the name of this genre.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the list of the album in this genre.
     * @return the list of the albums in this genre.
     */
    public List<Album> getAlbumList() {
        return this.albumList;
    }

    /**
     * Returns the list of the artists in this genre.
     * @return the list of the artists in this genre.
     */
    public List<Artist> getArtistList() {
        return this.artistList;
    }

    /**
     * Adds an album to this genre.
     * @param album - the album to add to the list.
     */
    public void addAlbum(final Album album) {
        this.albumList.add(album);
    }

    /**
     * Removes an album from this genre.
     * @param album - the album to remove from the list.
     */
    public void removeAlbum(final Album album) {
        this.albumList.remove(album);
    }

    /**
     * Adds an artist to this genre.
     * @param artist - the artist to add to the list.
     */
    public void addArtist(final Artist artist) {
        this.artistList.add(artist);
    }

    /**
     * Removes an artist from this genre.
     * @param artist - the artist to remove from the list.
     */
    public void removeArtist(final Artist artist) {
        this.artistList.remove(artist);
    }
}