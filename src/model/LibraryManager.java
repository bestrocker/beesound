package model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import Controller.Audio.MpegInfo.Duration;

/**
 * Representation of the music library. 
 * @author tiziano
 */
public final class LibraryManager implements Manager{   // this class is implemented using the singleton pattern
    
    private static final LibraryManager INSTANCE = new LibraryManager();
    
    private final List<Song> songList;
    private final List<Album> albumList;
    private final List<Artist> artistList;
    private final List<Genre> genreList;
    private final List<Playlist> playlistList;        // try to find a better name for this field
    //private final Playlist.Playing playlistInReproduction;
    
    private LibraryManager(/*String queueName, String queuePath*/) {
        this.songList = new ArrayList<>();
        this.albumList = new ArrayList<>();
        this.artistList = new ArrayList<>();
        this.genreList = new ArrayList<>();
        this.playlistList = new ArrayList<>();
        //this.playlistInReproduction = new Playlist.Playing(queueName, queuePath); 
    }
    
    /**
     * Returns the only instance of this class.
     * @return an instance of this class.
     */
    public static LibraryManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Returns the list of the songs in the music library.
     * @return the list of the songs in the music library.
     */
    public List<Song> getSongList() {
        return this.songList;
    }
    
    /**
     * Returns the list of the albums in the music library.
     * @return the list of the albums in the music library.
     */
    public List<Album> getAlbumList() {
        return this.albumList;
    }
    
    /**
     * Returns the list of the artists in the music library.
     * @return the list of the artists in the music library.
     */
    public List<Artist> getArtistList() {
        return this.artistList;
    }
    
    /**
     * Returns the list of the genres in the music library.
     * @return the list of the genres in the music library.
     */
    public List<Genre> getGenreList() {
        return this.genreList;
    }
    
    /**
     * Returns the list of the playlists in the music library.
     * @return the list of the playlists in the music library.
     */
    public List<Playlist> getPlaylistList() {
        return this.playlistList;
    }

    @Override
    public void addSongToLibrary(String title, String album, String artist, String genre,
            Duration duration, int bitRate, long size, String path, boolean copyright, int channel,
            String version, int rate, String channelsMode) {   // raw version, try to improve
        
        Song song = createSong(title, album, artist, genre, duration, bitRate, size, path, copyright, channel, 
                                version, rate, channelsMode);
        boolean albumCheck = false;
        boolean artistCheck = false;
        this.songList.add(song);
        if (!this.getAlbumTitles().contains(song.getAlbum())) {
            this.newAlbum(song.getAlbum());
            albumCheck = true;
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
    
    private Song createSong(String title, String album, String artist, String genre,
            Duration duration, int bitRate, long size, String path, boolean copyright, int channel,
            String version, int rate, String channelsMode) {
        return new Song.Builder()
                .title(title)
                .album(album)
                .artist(artist)
                .genre(genre)
                .duration(duration)
                .bitRate(bitRate)
                .size(size)
                .path(path)
                .copyright(copyright)
                .channel(channel)
                .version(version)
                .rate(rate)
                .channelsMode(channelsMode)
                .build();
    }
    
    private List<String> getAlbumTitles() {     // try to generalize these methods with interfaces and abstract classes
        final List<String> list = new ArrayList<>();
        this.albumList.forEach(x -> list.add(x.getTitle()));
        return list;       
    }
    
    private List<String> getArtistNames() {
        final List<String> list = new ArrayList<>();
        this.artistList.forEach(x -> list.add(x.getName()));
        return list;       
    }
    
    private List<String> getGenreNames() {
        final List<String> list = new ArrayList<>();
        this.genreList.forEach(x -> list.add(x.getName()));
        return list;
    }
    
    // try to reduce the private methods below
    private Album getAlbumFromList(final String title) throws NoSuchElementException {
        for (final Album a : this.albumList) {
            if (a.getTitle().equals(title)) {
                return a;
            }
        }
        throw new NoSuchElementException("No such album in the library");
    }
    
    private Artist getArtistFromList(final String name) throws NoSuchElementException {
        for (final Artist a : this.artistList) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new NoSuchElementException("No such artist in the library");
    }
    
    private Genre getGenreFromList(final String name) throws NoSuchElementException {
        for (final Genre g : this.genreList) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        throw new NoSuchElementException("No such genre in the library");
    }
    
    private void newAlbum(final String title) {
        final Album album = new Album(title);
        this.albumList.add(album);
        //add serialization
    }
    
    private void newArtist(final String name) {
        final Artist artist = new Artist(name);
        this.artistList.add(artist);
        //add serialization
    }
    
    private void newGenre(final String name) {
        final Genre genre = new Genre(name);
        this.genreList.add(genre);
        //add serialization
    }
    
    /**
     * Adds a new playlist to the music library.
     * @param name - the name of the playlist.
     * @param path - the path in which store the playlist.
     */
    public void newPlaylist(final String name, final String path) {
        final Playlist playlist = new Playlist(name, path);
        this.playlistList.add(playlist);
        //add serialization
    }
    
    private Song getSongFromPath(final Path path) throws NoSuchElementException {
        for (final Song s : this.songList) {
            if (s.getPath().equals(path.toString())) {
                return s;
            } 
        }
        throw new NoSuchElementException("This song is not present in the list.");
    }
    
    /**
     * Returns a list containing the paths of the songs in the specified playlist.
     * @param playlist - the playlist from which the song paths are extracted.
     * @return a list containing the paths of the songs in the specified playlist.
     */
    public List<String> getPathsFromPlaylist(final Playlist playlist) {
        final List<String> list = new ArrayList<>();
        playlist.getTrackList().forEach(x -> list.add(x.getPath().toString()));
        return list;
    }    
}