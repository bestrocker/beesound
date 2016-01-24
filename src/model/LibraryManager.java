package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import Controller.Audio.MpegInfo.Duration;

/**
 * Representation of the music library. 
 * @author tiziano
 */
public final class LibraryManager implements Manager{   // this class is implemented using the singleton pattern
    
    private static final LibraryManager INSTANCE = new LibraryManager();
    private static final int MAX_FAVOURITE = 9;
    
    private final List<Song> songList;
    private final List<Album> albumList;
    private final List<Artist> artistList;
    private final List<Genre> genreList;
    private final List<Playlist> playlistList;        // try to find a better name for this field
    private Playlist.Playing playlistInReproduction;
    
    private LibraryManager(/*String queueName, String queuePath*/) {
        this.songList = new LinkedList<>();
        this.albumList = new LinkedList<>();
        this.artistList = new LinkedList<>();
        this.genreList = new LinkedList<>();
        this.playlistList = new LinkedList<>();
        this.playlistInReproduction = new Playlist.Playing(); 
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
        if (isSongPresent(title)) {            
            return;
        }
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
        if (!this.getArtistNames().contains(song.getArtist())) {      
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
    
    public List<String> getAlbumTitles() {     // try to generalize these methods with interfaces and abstract classes
        final List<String> list = new ArrayList<>();
        this.albumList.forEach(x -> list.add(x.getTitle()));
        return list;       
    }
    
    public List<String> getArtistNames() {
        final List<String> list = new ArrayList<>();
        this.artistList.forEach(x -> list.add(x.getName()));
        return list;       
    }
    
    public List<String> getGenreNames() {
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

    @Override
    public List<String> getSongTitles() {
        List<String> list = new ArrayList<>();
        this.songList.forEach(x -> list.add(x.getTitle()));
        return list;
    }

    @Override
    public List<String> getPlaylistNames() {
        List<String> list = new ArrayList<>();
        this.playlistList.forEach(x -> list.add(x.getName()));
        return list;
    }

    @Override
    public String getCurrentSong(int index) {
        return this.playlistInReproduction.getTrackList().get(index).getPath();
    }

    @Override
    public void addSongInPlaylist(String songTitle, String playlistName) { // probably this method needs to throw exception
        Song song = getSong(songTitle);       
        getPlaylist(playlistName).addSong(song);       
    }
    
    @Override
    public void addSongInPlaylist(String songTitle, boolean now) {
        Song song = getSong(songTitle);
        if(now) {
            this.playlistInReproduction.getTrackList().add(0, song);
            this.playlistInReproduction.setSongInReproduction(song);
        }
        else {
            this.playlistInReproduction.addSong(song);
        }             
    }
    
    private Song getSongByPath(String songPath) {
        for (Song s : this.songList) {
            if (s.getPath().equals(songPath)) {
                return s;
            }            
        }
        throw new NoSuchElementException();
    }
    
    private Song getSong(String songTitle) throws NoSuchElementException {
        for (Song s : this.songList) {
            if (s.getTitle().equals(songTitle)) {
                return s;
            }            
        }
        throw new NoSuchElementException();
    }
    
    private Playlist getPlaylist(String playlistName) throws NoSuchElementException {
        for (Playlist p : this.playlistList) {
            if (p.getName().equals(playlistName)) {
                return p;
            }           
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public void addSongToQueue(int index) {
        this.playlistInReproduction.addSong(this.songList.get(index));        
    }

    @Override
    public String getSongPath(String songTitle) {
        return getSong(songTitle).getPath();
    }

    @Override
    public String getPlaylistPath(String playlistName) {
        return getPlaylist(playlistName).getPath();
    }

    @Override
    public int getQueueSize() {
        return this.playlistInReproduction.getTrackList().size();
    }

    @Override
    public void setReproductionPlaylist(String playlistName) {
        this.playlistInReproduction.setTracklist(getPlaylist(playlistName).getTrackList());
    }

    @Override
    public void setInReproduction(String songPath) {
        Song song = getSongByPath(songPath);         // with this we need to find the song only one time 
        this.playlistInReproduction.setSongInReproduction(song);
        song.incrementCounter();
    }

    @Override
    public void setSongPaused(boolean pause) {
        this.playlistInReproduction.getSongInReproduction().setSongPaused(pause);        
    }
    
    public List<String> getInReproductionTitles() {
        List<String> list = new ArrayList<>();
        this.playlistInReproduction.getTrackList().forEach(x -> list.add(x.getTitle()));
        return list;
    }
    
    private boolean isSongPresent(String songTitle) {
        boolean present = false;
        for (Song s : this.songList) {
            if (s.getTitle().equals(songTitle)) {
                present = true;
            }
        }
        return present;
    }
    
    @Override
    public void removeSong(String songTitle) {
        Song song = this.getSong(songTitle);
        this.playlistInReproduction.removeSong(song);
        this.getAlbumFromList(song.getAlbum()).removeSong(song);
        this.playlistList.forEach(x -> x.removeSong(song));
        this.songList.remove(song);
    }
    
    @Override
    public void removeSongFromQueue(String songTitle) {
        this.playlistInReproduction.removeSong(getSong(songTitle));
    }
    
    @Override
    public Map<String, Object> getCurrentSongInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("Title", this.playlistInReproduction.getSongInReproduction().getTitle());
        map.put("Size", this.playlistInReproduction.getSongInReproduction().getSize());
        map.put("Duration", this.playlistInReproduction.getSongInReproduction().getDuration());
        return map;       
    }    
    
    @Override
    public List<String> showPlaylistSong(String playlistName) {
        List<String> list = new ArrayList<>();
        this.getPlaylist(playlistName).getTrackList().forEach(x -> list.add(x.getTitle()));
        return list;
    }
    
    public Map<String, Integer> getLibraryInfo() {
        Map<String, Integer> map = new HashMap<>();        
        int min = 0;
        int sec = 0;
        for (Song s : this.songList) {
            min += s.getDuration().getMin();
            sec += s.getDuration().getSec();            
        }
        map.put("nSongs", this.songList.size());
        map.put("min", min);
        map.put("sec", sec);
        return map;
    }
    
    public List<String> getMostListened() {
        List<Song> list = this.songList;
        list.sort(new Comparator<Song>() {

            @Override
            public int compare(Song o1, Song o2) {
                return o1.getReproductionsCounter() - o2.getReproductionsCounter();
            }
        });
        List<String> titles = new ArrayList<>();
        list.forEach(x -> titles.add(x.getTitle()));
        return  titles.subList(0, this.MAX_FAVOURITE);       
    }
    
    @Override
    public void removePlaylist(String playlistName) {
        this.playlistList.remove(getPlaylist(playlistName));       
    }   
}