package Controller.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface SystemManager {
    
    /**
     * Return a list of string representing the absolute path of every playlist.
     * @return a List<String>
     */
    List<String> listAllPlaylist();
    
    /**
     * Return a list of string representing the absolute path of every mp3 song in the default Music directory.
     * @return a List<String>
     */
    List<String> listAllSongPath();
    
    /**
     * Import the given mp3 into the library.
     * @param mp3Source String 
     * @return final path of the copied file.
     */
    String importToLibrary(final String mp3Source);
    
    
    /**
     * Import a stream into library.
     * @param source String
     * @param name String
     * @return String
     */
    String importToLibrary(final InputStream source, final String name);
    
    /**
     * Return a List of every song's absolute path contained in the given playlist.
     * @param playlist File
     * @return List<String>
     * @throws IOException exception
     */
    List<String> getPlaylistSongs(final File playlist) throws IOException;
    
    /**
     * Return true if is already existing in the library.
     * @param srcPath String
     * @return boolean
     */
    boolean notExist(final String srcPath);
    
    /**
     * Create a new text file with the given name in the dstPath.
     * Note: dstPath must have final "/"
     * @param dstPath String
     * @param name String
     */
    void createNewFile(final String name, final String dstPath);
    
    /**
     * Delete the selected file.
     * @param pathFile String
     */
    void delete(final String pathFile);
    
    /**
     * Append a message to the file pointed.
     * @param msg String
     * @param path String
     */
    void appendToFile(final String msg, final String path);
}
