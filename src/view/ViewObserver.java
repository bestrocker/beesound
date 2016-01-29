package view;

import java.util.List;
import java.util.Map;
import Controller.Controller.REPRODUCE;

/**
 * Observer Interface for Controller.
 * @author bestrocker221
 *
 */
public interface ViewObserver {
    
    /**
     * Create a new empty Playlist file into the library playlist folder and load it into the library.
     * @param name
     */
    void newPlaylistFile(final String name);
    
    /**
     * Add a song to library
     * @param songPath
     */
    void addSong(final String songPath);
    
    /**
     * Return a list of all song in library
     * @return song list
     */
    List<String> showAllSong();
    
    /**
     * Return a list of all playlist.
     * @return playlist list
     */
    List<String> showAllPlaylist();
    
    /**
     * List of all album.
     * @return List<String>
     */
    List<String> showAllAlbum();
    
    /**
     * List of all Genres.
     * @return List<String>
     */
    List<String> showAllGenre();
    
    /**
     * List of all Artist
     * @return List<String>
     */
    List<String> showAllArtist();
    
    /**
     * List of the song in Reproduction.
     * @return list<String>
     */
    List<String> showReproductionPlaylist();
    
    /**
     * Add the selected song to the given playlist.
     * @param song
     * @param playlist
     */
    void addSongInPlaylist(final String songName, final String playlistName);
    
    /**
     * Play Player.
     */
    void playButton();
    
    /**
     * Pause.
     */
    void pauseButton();
    
    /**
     * Stop player.
     */
    void stopButton();
    
    /**
     * Next Track in ReproductionPlaylist.
     */
    void nextTrack();
    
    /**
     * Previous Track in ReproductionPlaylist.
     */
    void prevTrack();
    
    /**
     * Set volume to the given one.
     * @param volume
     */
    void setVolumeButton(final double volume);
    
    /**
     * Skip song to given bytes.
     * @param toBytes
     */
    void skipTo(final long toBytes);
    
    /**
     * Set the reproduction mode to shuffle.
     */
    void setShuffleMode();
    
    /**
     * Set the reproduction mode to linear.
     */
    void linearMode();
    
    /**
     * Remove phisically the song from library.
     * @param songTitle
     */
    void removeSong(final String songTitle);
    
    /**
     * Remove the selected song from the reproduction queue.
     * @param songTitle
     */
    void removeSongFromQueue(final String songTitle);
    
    /**
     * Add the selected song in reproduction Playlist.
     * REPRODUCE will be NOW or AFTER
     * @param songName
     * @param when (NOW/AFTER)
     */
    void addSongInReproductionPlaylist(final String songName,final REPRODUCE when);
    
    /**
     * Show all song in the given Playlist.
     * @param playlistName
     * @return
     */
    List<String> showPlaylistSong(final String playlistName);
    
    /**
     * Returns a map with current song info.
     * @return
     */
    Map<String, Object> getCurrentSongInfo();
    
    /**
     * Remove Playlist from library.
     * @param namePlaylist
     */
    void removePlaylist(String namePlaylist);
    
    /**
     * Returns a List of favorites.
     * @return List<String>
     */
    Map<String, Integer>showFavorites();
    
    /**
     * Create a full new Library in the specified path. If already existing, it will reload all its containing.
     * @param pathNewLibrary
     */
    void newLibrary(final String pathNewLibrary);    
    
    /**
     * Set the selected playlist in reproduction
     * @param playlistName
     */
    void playPlaylist(String playlistName);
    
    /**
     * Remove this song from the current playlist
     * @param songName
     */
    void removeSongFromPlaylist(String songName, String playlistName);
    
    /**
     * Return a list containing informations amount of songs and minutes in library
     * @return
     */
    List<Integer> showLibraryInfo();
    
    /**
     * Returns a list for all the matched songs.
     * @param text
     * @return
     */
    List<String> searchSong(String text);
    
    /**
     * Returns the current byte of the seekbar
     * @return
     */
    int getPos();
    
    /**
     * Returns info for the current song
     * @param index
     * @return
     */
    Map<String, Object> showSongInfo(int index);

}






