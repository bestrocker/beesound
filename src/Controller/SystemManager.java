package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SystemManager {
    /**
     * Return a list of all files in the given directory.
     * @param directory
     * @return a list of Files in the given directory.
     */
    List<File> listAllFilesInDir(final File directory);
    
    /**
     * Return a list of string representing the absolute path of every mp3 song in the given directory.
     * @param directory
     * @return a List<String>
     */
    List<String> listAllSongPath(final File directory);
    
    /**
     * Import the given mp3 into the library.
     * @param pathSource
     */
    void importToLibrary(final String mp3Source) throws IOException;
    
    /**
     * Return a List of every song's absolute path contained in the given playlist 
     * @param playlist
     * @return
     * @throws IOException
     */
    List<String> getPlaylistSongs(final File playlist) throws IOException;
}
