package model;

import java.nio.file.Path;

/** Interface for classes that have to manage and organize the music library.
 * 
 * @author tiziano
 *
 */
public interface Manager {
    
    /**
     * Method for adding a new song to the library list.
     * @param song
     */
    void addSongToLibrary(Song song);
    
    /**
     * Method for serializing info of a specified song in the file system.
     * @param path
     */
    void serializeData(Path path);
}