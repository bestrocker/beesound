package Controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

import Controller.Audio.AudioController;
import Controller.Files.FileController;
import Controller.Files.Log;
import model.LibraryManager;
import model.Song;

public class Controller implements ViewObserver{

    private final LibraryManager model;
  //private final View view;
    private final AudioController audiocontroller;
    private final FileController filecontroller;
    
    public Controller() {
        this.model = LibraryManager.getInstance();        
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
        String unk = "Unknown";
        
        this.filecontroller.listAllSongPath().stream()
                           .forEach(i->{
                                try {
                                    this.audiocontroller.getMpegInfo().load(new URL(i));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                this.model.addSongToLibrary(new Song.Builder()
                                .title(this.audiocontroller.getMpegInfo().getTitle())
                                .album(this.audiocontroller.getMpegInfo().getAlbum())
                                .artist(this.audiocontroller.getMpegInfo().getArtist())
                                .bitRate(this.audiocontroller.getMpegInfo().getBitRate())
                                .year(this.audiocontroller.getMpegInfo().getYear())
                                .genre(this.audiocontroller.getMpegInfo().getGenre())
                                .size(this.audiocontroller.getMpegInfo().getSize())
                                .path(Paths.get(i))
                                .build()
                                );
                            });
    }
}
