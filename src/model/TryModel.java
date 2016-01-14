package model;

import Controller.Audio.MpegInfo.Duration;
import javafx.scene.effect.Lighting;


public class TryModel {

    public static void main(String[] args) {
        
        Duration duration = new Duration(50, 30);
        Song song = new Song.Builder()  
                            .title("Trains")
                            .album("In Absentia")
                            .artist("Porcupine Tree")
                            .genre("Progressive Rock")
                            .duration(duration)
                            .bitRate(192)
                            .size(80000)
                            .path(System.getProperty("user.home"))
                            .copyright(true)
                            .channel(0)
                            .version(null)
                            .rate(0)
                            .channelsMode("Stereo")
                            .build();
        
        LibraryManager library = LibraryManager.getInstance();
        library.addSongToLibrary(song);
        library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));
                
        
        
        
        
       
    }
}