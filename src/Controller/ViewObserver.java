package Controller;

public interface ViewObserver {
    
    /**
     * Create a new empty Playlist file into the library playlist folder and load it into the library.
     * @param name
     */
    public void newPlaylistFile(final String name);
    
    /**
     * Increment by one the counter of song's reproductions.
     * @param songPath
     */
    void incrementSongCounter(final String songPath);
}
