package model;

import java.nio.file.Path;

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
    void addSongToLibrary(Song song);
    
    /**
     * Serializes info of a specified song in the file system.
     * @param path - the path in which data is serialized.
     */
    void serializeData(Path path);
}