package Controller;

import java.util.List;
import Controller.Controller.REPRODUCE;

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
     * Play selected song.
     * @param song
     *//*
    void playButton(final String songName);
    */
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
    
}
