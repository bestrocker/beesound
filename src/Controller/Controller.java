package Controller;

import Controller.Audio.AudioController;
import Controller.Files.FileController;
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
        loadInfoToLibrary();
    }
    
    void loadInfoToLibrary(){
        
    }
}
