package Controller;

import java.util.List;

import model.Playlist;
import model.Song;

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
    List<Song> showAllSong();
    
    /**
     * Return a list of all playlist.
     * @return playlist list
     */
    List<Playlist> showAllPlaylist();
    
    /**
     * Add the selected song to the given playlist
     * @param song
     * @param playlist
     */
    void addSongInPlaylist(final Song song, final Playlist playlist);
    
    /**
     * Play.
     */
    void playButton();
    
    /**
     * Play selected song.
     * @param song
     */
    void playButton(final Song song);
    
    /**
     * Pause.
     */
    void pauseButton();
    
    /**
     * Stop player.
     */
    void stopButton();
    
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
    
    void addSongInReproductionPlaylist(final Song song);
//    /**
//     * Increment by one the counter of song's reproductions.
//     * @param songPath
//     */
//    void incrementSongCounter(final String songPath);
}
