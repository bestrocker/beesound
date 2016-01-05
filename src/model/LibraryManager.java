package model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager implements Manager{   // this class is implemented using the singleton pattern
    
    private static final LibraryManager INSTANCE = new LibraryManager();
    
    private List<Song> songList;
    private List<Album> albumList;
    private List<Artist> artistList;
    private List<Genre> genreList;
    private List<Playlist> playlistList;        // try to find a better name for this field
    
    private LibraryManager() {
        this.songList = new ArrayList<>();
        this.albumList = new ArrayList<>();
        this.artistList = new ArrayList<>();
        this.genreList = new ArrayList<>();
        this.playlistList = new ArrayList<>();
    }
    
    public static LibraryManager getInstance() {
        return INSTANCE;
    }

    public List<Song> getSongList() {
        return this.songList;
    }

    public List<Album> getAlbumList() {
        return this.albumList;
    }

    public List<Artist> getArtistList() {
        return this.artistList;
    }

    public List<Genre> getGenreList() {
        return this.genreList;
    }

    public List<Playlist> getPlaylistList() {
        return this.playlistList;
    }
    
    @Override
    public void addSongToLibrary(Song song) {
        this.songList.add(song);
        // add serialization
    }
    
    public void newAlbum(String title) {
        Album album = new Album(title);
        this.albumList.add(album);
        //add serialization
    }
    
    public void newArtist(String name) {
        Artist artist = new Artist(name);
        this.artistList.add(artist);
        //add serialization
    }
    
    public void newGenre(String name) {
        Genre genre = new Genre(name);
        this.genreList.add(genre);
        //add serialization
    }
    
    public void newPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        this.playlistList.add(playlist);
        //add serialization
    }
    
    @Override
    public void serializeData(Path path) {
        
    }
}
