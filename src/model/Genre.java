package model;

import java.util.ArrayList;
import java.util.List;

public class Genre {
    
    private static String DEFAULT = "Unknown";
    
    private String name;
    private List<Album> albumList;
    private List<Artist> artistList;
    
    public Genre() {
        this.name = DEFAULT;
        this.albumList = new ArrayList<>();
        this.artistList = new ArrayList<>();
    }
    
    public Genre(String name, List<Album> albumList, List<Artist> artistList) {
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
    
    public void addAlbum(Album album) {
        this.albumList.add(album);
    }
    
    public void removeAlbum(Album album) {
        this.albumList.remove(album);
    }
    
    public void addArtist(Artist artist) {
        this.artistList.add(artist);
    }
    
    public void removeArtist(Artist artist) {
        this.artistList.remove(artist);
    }
}