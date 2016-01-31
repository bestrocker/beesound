package model;

import java.util.List;
import java.util.Map;
import Controller.Audio.MpegInfo.Duration;

/** 
 * Interface for classes that have to manage and organize the music library.
 */
public interface Manager {

    /**
     * Adds a new song to the library list.
     * @param title - the title of the song to add to the list
     * @param album - the title of the album which the song belongs to
     * @param artist - the name of the artist who created the song
     * @param genre - the genre which the song belongs to 
     * @param duration - the duration of the song
     * @param bitRate - the bit rate of the song
     * @param size - the size of the song in the file system
     * @param path - the path in which the song is stored
     * @param copyright - tells if the song is registered with copyright
     * @param channel - channel.
     * @param version - the version of the song
     * @param rate - the rate of the song
     * @param channelsMode - the channel mode of the song 
     */
    void addSongToLibrary(final String title, final String album, final String artist, final String genre,
            final Duration duration, final int bitRate, final long size, final String path, final boolean copyright, 
            final int channel, final String version, final int rate, final String channelsMode);

    /**
     * Creates a new playlist with the specified name and path and adds it to the music library.
     * @param name - the name of the playlist to create
     * @param path - the path of the playlist to create
     */
    void newPlaylist(final String name, final String path);

    /**
     * Returns the list of the paths of the songs in the specified playlist.
     * @param playlist - the name of the playlist from which the song paths are extracted.
     * @return the list of the paths of the songs in the specified playlist.
     */
    List<String> getPathsFromPlaylist(final Playlist playlist);

    /**
     * Returns a list containing the titles of all the songs in the music library.
     * @return a list containing the titles of all the songs in the music library.
     */
    List<String> getSongTitles();

    /**
     * Returns a list containing the names of all the playlists in the music library.
     * @return a list containing the names of all the playlists in the music library.
     */
    List<String> getPlaylistNames();

    /**
     * Returns the path of the song stored in the specified index of the list.
     * @param index - the index of the song from which the path is extracted.
     * @return the path of the song stored in the specified index of the list.
     */
    String getCurrentSong(int index);

    /**
     * Adds the specified song to the specified playlist.
     * @param songTitle - the title of the song to add.
     * @param playlistName - the name of the playlist in which the song is added.
     */
    void addSongInPlaylist(String songTitle, String playlistName);

    /**
     * Adds the specified song to the reproduction playlist.
     * @param songTitle - the title of the song to add to the reproduction playlist.
     * @param now - tells if the song has to be added at the beginning or the end of the reproduction list.
     */
    void addSongInPlaylist(String songTitle, boolean now);

    /**
     * Adds the song with the specified index to the reproduction playlist.
     * @param index - the index of the song to add to the reproduction playlist.
     */
    void addSongToQueue(int index);

    /**
     * Returns the path of the song with the specified title.
     * @param songTitle - the title of the song whose path is returned. 
     * @return the path of the song with the specified title.
     */
    String getSongPath(String songTitle);

    /**
     * Returns the path of the playlist with the specified name.
     * @param playlistName - the name of the playlist whose path is returned.
     * @return the path of the playlist with the specified name.
     */
    String getPlaylistPath(String playlistName);

    /**
     * Returns the number of elements inside the reproduction queue.
     * @return the number of elements inside the reproduction queue.
     */
    int getQueueSize();

    /**
     * Sets the playlist with the specified name as reproduction playlist.
     * @param playlistName - the name of the playlist to set as reproduction playlist.
     */
    void setReproductionPlaylist(String playlistName);

    /**
     * Sets the song with the specified title as song in reproduction and increments its reproductions counter. 
     * @param songPath - the title of the song to set in reproduction.
     */
    void setInReproduction(String songPath);

    /**
     * Sets the specified parameter as value of the fields isPaused in the song in reproduction.
     * @param pause - the value to set in the field isPaused in the song in reproduction.
     */
    void setSongPaused(boolean pause);

    /**
     * Returns a list containing album titles.
     * @return a list containing album titles. 
     */
    List<String> getAlbumTitles();

    /**
     * Returns a list containing artist names.
     * @return a list containing artist names.
     */
    List<String> getArtistNames();

    /**
     * Returns a list containing genre names.
     * @return a list containing artist names.
     */
    List<String> getGenreNames(); 

    /**
     * Returns a list containing titles of the song in reproducion list.
     * @return a list containing titles of the song in reproducion list.
     */
    List<String> getInReproductionTitles();

    /**
     * Removes the song with the specified title from the library.
     * @param songTitle - the title of the song to remove. 
     */
    void removeSong(String songTitle);

    /**
     * Removes the song with the specified title from the reproduction list.
     * @param songTitle - the title of the song to remove. 
     */
    void removeSongFromQueue(String songTitle);

    /**
     * Returns a map containing info about the song in reproduction: title, size and duration.
     * @return a map containing info about the song in reproduction.
     */
    Map<String, Object> getCurrentSongInfo();

    /**
     * Returns a list containing the titles of the songs in the specified playlist.
     * @param playlistName - the specified name of the playlist.
     * @return a list containing the titles of the songs in the specified playlist
     */
    List<String> showPlaylistSong(String playlistName);

    /**
     * Returns a map containing info about total number of songs and total audio time in library.
     * @return a map containing info about total number of songs and total audio time in library.
     */
    List<Integer> getLibraryInfo();

    /**
     * Returns a list containing the most listened song titles.
     * @return a list containing the most listened song titles.
     */
    List<String> getMostListened();

    /**
     * Removes the playlist with the specified name from the library.
     * @param playlistName - the name of the playlist to remove.
     */
    void removePlaylist(String playlistName);

    /**
     * Removes all the information contained in the library.
     */
    void resetLibrary();

    /**
     * Returns a list containing the titles of the songs which match with the specified string.
     * @param songTitle - the string to which the song titles have to match.
     * @return a list containing the titles of the songs which match with the specified string
     */
    List<String> fetchSongs(String songTitle);

    /**
     * Removes the specified song from the specified playlist.
     * @param songTitle - the title of the song to remove.
     * @param playlistName - the name of the playlist from which the song is removed.
     */
    void removeSongFromPlaylist(String songTitle, String playlistName);

    /**
     * Returns a map containing all the information about a specified song. 
     * @param index - the index of the song.
     * @return a map containing all the information
     */
    Map<String, Object> getSongInfo(int index);
}