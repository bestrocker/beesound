package model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
    public void addSongToLibrary(Song song) {   // raw version, try to improve
        boolean albumCheck = false;
        boolean artistCheck = false;
        this.songList.add(song);
        if (!this.getAlbumTitles().contains(song.getAlbum())) {
            this.newAlbum(song.getAlbum());
            albumCheck = true;;
        }
        this.getAlbumFromList(song.getAlbum()).addSong(song);
        if (!this.getArtistNames().contains(song.getArtist())) {      // this method seems not to use equals.
            this.newArtist(song.getArtist());
            artistCheck = true;
        }
        this.getArtistFromList(song.getArtist()).addAlbum(this.getAlbumFromList(song.getAlbum()));
        if (!this.getGenreNames().contains(song.getGenre())) {
            this.newGenre(song.getGenre());
        }
        if (albumCheck) {
            this.getGenreFromList(song.getGenre()).getAlbumList().add(this.getAlbumFromList(song.getAlbum()));
        }
        if (artistCheck) {
            this.getGenreFromList(song.getGenre()).getArtistList().add(this.getArtistFromList(song.getArtist()));
        }       
    }
    
    private List<String> getAlbumTitles() {     // try to generalize these methods with interfaces and abstract classes
        List<String> list = new ArrayList<>();
        this.albumList.forEach(x -> list.add(x.getTitle()));
        return list;       
    }
    
    private List<String> getArtistNames() {
        List<String> list = new ArrayList<>();
        this.artistList.forEach(x -> list.add(x.getName()));
        return list;       
    }
    
    private List<String> getGenreNames() {
        List<String> list = new ArrayList<>();
        this.genreList.forEach(x -> list.add(x.getName()));
        return list;
    }
    
    // try to reduce the private methods below
    private Album getAlbumFromList(String title) throws NoSuchElementException {
        for (Album a : this.albumList) {
            if (a.getTitle().equals(title)) {
                return a;
            }
        }
        throw new NoSuchElementException("No such album in the library");
    }
    
    private Artist getArtistFromList(String name) throws NoSuchElementException {
        for (Artist a : this.artistList) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new NoSuchElementException("No such artist in the library");
    }
    
    private Genre getGenreFromList(String name) throws NoSuchElementException {
        for (Genre g : this.genreList) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        throw new NoSuchElementException("No such genre in the library");
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
    
    public void newPlaylist(String name, String path) {
        Playlist playlist = new Playlist(name, path);
        this.playlistList.add(playlist);
        //add serialization
    }
    
    @Override
    public void serializeData(Path path) {
        
    }
    
    public Song getSongFromPath(Path path) throws NoSuchElementException {
        for (Song s : this.songList) {
            if (s.getPath().equals(path)) {
                return s;
            } 
        }
        throw new NoSuchElementException("This song is not present in the list.");
    }
    
    public List<String> getPathsFromPlaylist(Playlist playlist) {
        List<String> list = new ArrayList<>();
        playlist.getTrackList().forEach(x -> list.add(x.getPath().toString()));
        return list;
    }    
}