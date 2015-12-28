package model;
import java.nio.file;

public interface Manager {
    
    public void addSongToLibrary(Song song);
    public void serializeData(Path path); 
}
