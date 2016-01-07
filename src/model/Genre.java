package model;

import java.util.ArrayList;
import java.util.List;

public class Genre {
    
    private final String name;
    private final List<Album> albumList;
    private final List<Artist> artistList;
    
    public Genre(final String name) {
        this.name = name;
        this.albumList = new ArrayList<>();
        this.artistList = new ArrayList<>();
    }
    
    public Genre(final String name, final List<Album> albumList, final List<Artist> artistList) {
        this.name = name;
        this.albumList = albumList;
        this.artistList = artistList;
    }

    public String getName() {
        return this.name;
    }

    public List<Album> getAlbumList() {
        return this.albumList;
    }

    public List<Artist> getArtistList() {
        return this.artistList;
    }
    
    public void addAlbum(final Album album) {
        this.albumList.add(album);
    }
    
    public void removeAlbum(final Album album) {
        this.albumList.remove(album);
    }
    
    public void addArtist(final Artist artist) {
        this.artistList.add(artist);
    }
    
    public void removeArtist(final Artist artist) {
        this.artistList.remove(artist);
    }
}