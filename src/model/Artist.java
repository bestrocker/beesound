package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
       
    private final String name;
    private final List<Album> albumList;
    
    public Artist(final String name) {
        this.name = name;
        this.albumList = new ArrayList<>();
    }

    public Artist(final String name, final List<Album> albumList) {
        this.name = name;
        this.albumList = albumList;
    }

    public String getName() {
        return this.name;
    }

    public List<Album> getAlbumList() {
        return this.albumList;
    }
    
    public void addAlbum(final Album album) {
        this.albumList.add(album);
    }
    
    public void removeAlbum(final Album album) {
        this.albumList.remove(album);
    }
}
