package Controller;

import java.io.IOException;
import java.net.URL;
import Controller.Audio.AudioController;
import Controller.Files.FileController;
import Controller.Files.Log;
import model.LibraryManager;

public class Controller implements ViewObserver{

    private final LibraryManager model;
  //private final View view;
    private final AudioController audiocontroller;
    private final FileController filecontroller;
    
    public Controller(final LibraryManager model) {
        this.model = model;        
        this.filecontroller = new FileController();
        this.audiocontroller = new AudioController();
        try {
            loadInfoToLibrary();
        } catch (IOException e) {
            Log.ERROR("Cannot load info into Library");
            e.printStackTrace();
        }
    }
    
    void loadInfoToLibrary() throws IOException{
        this.filecontroller.listAllSongPath().stream()
                           .forEach(i->{
                                try {
                                    this.audiocontroller.getMpegInfo().load(new URL(i));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                            });
    }
}
