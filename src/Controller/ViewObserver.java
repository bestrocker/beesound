package Controller;

public interface ViewObserver {
    
    /**
     * Create a new empty Playlist file into the library playlist folder.
     * @param name
     */
    public void newPlaylistFile(final String name);
    
}
