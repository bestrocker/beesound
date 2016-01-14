package model;

import java.nio.file.Path;
import java.util.List;
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
    
    public void newPlaylist(final String name, final String path);
    
    public Song getSongFromPath(final Path path) throws NoSuchElementException;
    
    public List<String> getPathsFromPlaylist(final Playlist playlist);
    
    public List<String> getSongTitles();
    
    public List<String> getPlaylistNames();
    
    public String getCurrentSong(String songTitle);
    
    public void addSongInPlaylist(String songTitle, String playlistName);
    
    public void addSongInPlaylist(String songTitle);
    
    public void addSongToQueue(int index);
    
    public String getSongPath(String songTitle);
    
    public String getPlaylistPath(String playlistName);
    
    public String setReproductionPlaylist(String playlistName);
    
    public void setInReproduction(String songTitle);
    
}
