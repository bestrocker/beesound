package model;

import java.util.ArrayList;
import java.util.List;

import Controller.Files.FileController;

public class Playlist {
    
    private String name;
    private List<Song> trackList;
    private String path;
    
    public Playlist(String name) {
        this.name = name;
        this.trackList = new ArrayList<>();
        this.path = FileController.playlistDirPath+ name+".txt";
    }

    public Playlist(String name, List<Song> trackList) {
        this.name = name;
        this.trackList = trackList;
    }

    public String getPath(){
        return this.path;
    }
    public String getName() {
        return this.name;
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
