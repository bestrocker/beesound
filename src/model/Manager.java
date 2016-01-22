package model;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import Controller.Audio.MpegInfo.Duration;

/** 
 * Interface for classes that have to manage and organize the music library.
 * @author tiziano
 *
 */
public interface Manager {
    
    /**
     * Adds a new song to the library list.
     * @param song - the song to add to the list.
     */
    void addSongToLibrary(String title, String album, String artist, String genre,
            Duration duration, int bitRate, long size, String path, boolean copyright, int channel,
            String version, int rate, String channelsMode);
    
    
    /**
     * Creates a new playlist with the specified name and path and adds it to the music library.
     * @param name - the name of the playlist to create
     * @param path - the path of the playlist to create
     */
    public void newPlaylist(final String name, final String path);
    
    /**
     * Returns the list of the paths of the songs in the specified playlist.
     * @param playlist - the name of the playlist from which the song paths are extracted.
     * @return the list of the paths of the songs in the specified playlist.
     */
    public List<String> getPathsFromPlaylist(final Playlist playlist);
    
    /**
     * Returns a list containing the titles of all the songs in the music library.
     * @return a list containing the titles of all the songs in the music library.
     */
    public List<String> getSongTitles();
    
    /**
     * Returns a list containing the names of all the playlists in the music library.
     * @return a list containing the names of all the playlists in the music library.
     */
    public List<String> getPlaylistNames();
    
    /**
     * Returns the path of the song stored in the specified index of the list.
     * @param index - the index of the song from which the path is extracted.
     * @return the path of the song stored in the specified index of the list.
     */
    public String getCurrentSong(int index);
    
    /**
     * Adds the specified song to the specified playlist.
     * @param songTitle - the title of the song to add.
     * @param playlistName - the name of the playlist in which the song is added.
     */
    public void addSongInPlaylist(String songTitle, String playlistName);
    
    /**
     * Adds the specified song to the reproduction playlist.
     * @param songTitle - the title of the song to add to the reproduction playlist.
     */
    public void addSongInPlaylist(String songTitle, boolean now);
    
    /**
     * Adds the song with the specified index to the reproduction playlist.
     * @param index - the index of the song to add to the reproduction playlist.
     */
    public void addSongToQueue(int index);
    
    /**
     * Returns the path of the song with the specified title.
     * @param songTitle - the title of the song whose path is returned. 
     * @return the path of the song with the specified title.
     */
    public String getSongPath(String songTitle);
    
    /**
     * Returns the path of the playlist with the specified name.
     * @param playlistName - the name of the playlist whose path is returned.
     * @return the path of the playlist with the specified name.
     */
    public String getPlaylistPath(String playlistName);
    
    /**
     * Returns the number of elements inside the reproduction queue.
     * @return the number of elements inside the reproduction queue.
     */
    public int getQueueSize();
    
    /**
     * Sets the playlist with the specified name as reproduction playlist.
     * @param playlistName - the name of the playlist to set as reproduction playlist.
     */
    public void setReproductionPlaylist(String playlistName);
    
    /**
     * Sets the song with the specified title as song in reproduction and increments its reproductions counter. 
     * @param songPath - the title of the song to set in reproduction.
     */
    public void setInReproduction(String songPath);
    
    /**
     * Sets the specified parameter as value of the fields isPaused in the song in reproduction.
     * @param pause - the value to set in the field isPaused in the song in reproduction.
     */
    public void setSongPaused(boolean pause);
    
    public List<String> getAlbumTitles();
    
    public List<String> getArtistNames();
    
    public List<String> getGenreNames(); 
    
    List<String> getInReproductionTitles();
    
    void removeSong(String songTitle);
    
    void removeSongFromQueue(String songTitle);
    
    /**
     * Returns a map containing info about the song in reproduction: title, size and duration.
     * @return a map containing info about the song in reproduction.
     */
    Map<String, Object> getCurrentSongInfo();
}