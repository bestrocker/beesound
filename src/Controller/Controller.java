package Controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import Controller.Audio.AudioController;
import Controller.Audio.MpegInfo;
import Controller.Audio.AudioController.REPRODUCTION_STRATEGY;
import Controller.Files.FileController;
import Controller.Files.Log;
import model.LibraryManager;
import model.Song;

public class Controller implements ViewObserver{

    private final LibraryManager model;
  //private final View view;
    private final AudioController audiocontroller;
    private final FileController filecontroller;
    
    public Object j = new Object();
    
    public Controller() {
        this.model = LibraryManager.getInstance();        
        this.filecontroller = new FileController();
        this.audiocontroller = new AudioController();
        
        loadInfoToLibrary();
       
    }
    
    void loadInfoToLibrary() {
        final String unk = "";
        final MpegInfo info = MpegInfo.getInstance();
        this.filecontroller.listAllSongPath().stream().parallel()
                           .forEach(i->{
                               synchronized(j){
                                   try {
                                    info.load(new File(i));
                                } catch (Exception e) {
                                    Log.ERROR("Can't Load song info from file");
                                    e.printStackTrace();
                                }
                                this.model.addSongToLibrary(new Song.Builder()
                                .title(info.getTitle().orElse(i.substring(i.lastIndexOf("/")+1,i.length()-4)))
                                .album(info.getAlbum().orElse(unk))
                                .artist(info.getArtist().orElse(unk))
                                .bitRate(info.getBitRate())
                                .year(info.getYear().orElse(unk))
                                .genre(info.getGenre().orElse(unk))
                                .size(info.getSize())
                                .path(Paths.get(i))
                                .build()
                                );
                               }});
                            
    }
    public static void main(String[] args) throws InterruptedException{
        Controller c = new Controller();
        List<String> l = new ArrayList<>();
        for(Song s : c.model.getSongList()){
            l.add(s.getPath().toString());
        }
        c.audiocontroller.setReproductionStrategy(REPRODUCTION_STRATEGY.SHUFFLE);
        c.audiocontroller.setPlaylist(l);
        c.audiocontroller.playPlayer();
        Thread.sleep(10000);
        c.audiocontroller.nextPlayer();
        Thread.sleep(10000);
        c.audiocontroller.nextPlayer();
        Thread.sleep(10000);
        c.audiocontroller.prevPlayer();
        System.out.println("import fatto");
        Thread.sleep(200);
        for(Song i : c.model.getSongList()){
            System.out.println("INIZIO");
            System.out.println(i.getPath());
            System.out.println("title "+i.getTitle());
            System.out.println("album" + i.getAlbum());
            System.out.println("artist "+i.getArtist());
            System.out.println(i.getBitRate());
            System.out.println("genre "+i.getGenre());
            System.out.println("size "+i.getSize());
            System.out.println("year " +i.getYear());
            System.out.println("FINE");
        }
    }
}
