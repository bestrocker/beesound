package Controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

import Controller.Audio.AudioController;
import Controller.Audio.MpegInfo;
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
        final Optional<String> unk = Optional.of("Unknown");
        
        final MpegInfo info = new MpegInfo();
        
        this.filecontroller.listAllSongPath().stream()
                           .forEach(i->{
                                
                                
                                
                                this.model.addSongToLibrary(new Song.Builder()
                                .title(info.getTitle())
                                .album(info.getAlbum().)
                                .artist(info.getArtist().orElseGet(unk))
                                .bitRate(info.getBitRate())
                                .year(info.getYear().orElseGet(unk))
                                .genre(info.getGenre().orElseGet(unk))
                                .size(info.getSize())
                                .path(Paths.get(i))
                                .build()
                                );
                               });
                            
    }
}
