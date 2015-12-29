package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    
    private static String DEFAULT = "Unknown";
    
    private String name;
    private List<Album> albumList;
    
    public Artist() {
        this.name = DEFAULT;
        this.albumList = new ArrayList<>();
    }

    public Artist(String name, List<Album> albumList) {
        this.name = name;
        this.albumList = albumList;
    }

    public String getName() {
        return this.name;
    }

    public List<Album> getAlbumList() {
        return this.albumList;
    }
    
    public void addAlbum(Album album) {
        this.albumList.add(album);
    }
    
    public void removeAlbum(Album album) {
        this.albumList.remove(album);
    }
}
