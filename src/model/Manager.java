package model;

import java.nio.file.Path;
import java.util.List;

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
    
    public List<Song> getSongList();
    
    public List<Album> getAlbumList();
    
    public List<Artist> getArtistList();
    
    public List<Genre> getGenreList();
    
    public List<Playlist> getPlaylistList();
    
    
    

}