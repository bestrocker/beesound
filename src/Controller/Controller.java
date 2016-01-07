package Controller;

import java.io.File;
import java.io.IOException;
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

public class Controller implements ViewObserver {

    private final LibraryManager model;
  //private final View view;
    private final AudioController audiocontrol;
    private final FileController filecontrol;
    
    private Object j = new Object();
    
    public Controller() {
        this.model = LibraryManager.getInstance();        
        this.filecontrol = new FileController();
        this.audiocontrol = new AudioController(this);
        loadInfoToLibrary();
       
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void newPlaylistFile(final String name){
        if(this.filecontrol.notExist(FileController.playlistDirPath+name)){
            this.filecontrol.createNewFile(name, FileController.playlistDirPath);
        }
        this.model.newPlaylist(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void incrementSongCounter(final String songPath){
        System.out.println(songPath+"  DA TROVARE");
        List<String> l = new ArrayList<>();
        //this.model.getSongFromPath(Paths.get(songPath)).incrementCounter();
        for(Song s : this.model.getSongList()){
            System.out.println(s.getPath().toString());
            l.add(s.getPath().toString());
            
        }
        for(String s:l){
            if (s==songPath){
                System.out.println("CIAOOO");
            }
        }
        
    }
    
    private void loadInfoToLibrary() {
        final String unk = "";
        final MpegInfo info = MpegInfo.getInstance();
        this.filecontrol.listAllSongPath().stream().parallel()
                           .forEach(i->{
                               synchronized(j){
                                   try {
                                    info.load(new File(i));
                                } catch (Exception e) {
                                    Log.ERROR("Can't Load song info from file");
                                    e.printStackTrace(); }
                                this.model.addSongToLibrary(new Song.Builder()
                                .title(info.getTitle().orElse(i.substring(i.lastIndexOf("/")+1,i.length()-4)))
                                .album(info.getAlbum().orElse(unk))
                                .artist(info.getArtist().orElse(unk))
                                .bitRate(info.getBitRate())
                                .genre(info.getGenre().orElse(unk))
                                .size(info.getSize())
                                .bitRate(info.getBitRate())
                                .duration(info.getDurationInMinutes())
                                .path(Paths.get(i))
                                .build()
                                );
                               }});
        Log.INFO("Mpeg tag loaded into the library.");
    }
    
    public static void main(String[] args) throws InterruptedException{
        Controller c = new Controller();
        
        List<String> l = c.filecontrol.listAllSongPath();
        
        
       // c.audiocontrol.addSongInPlaylist("/home/bestrocker221/Dropbox/Musica/holdbacktheriver.mp3");
        c.audiocontrol.setReproductionStrategy(REPRODUCTION_STRATEGY.SHUFFLE);
        c.audiocontrol.setPlaylist(l);
        c.audiocontrol.playPlayer();
        
        System.out.println("import fatto");
        Thread.sleep(2000);
        for(Song i : c.model.getSongList()){
            System.out.println("INIZIO");
            System.out.println(i.getPath());
            System.out.println("title "+i.getTitle());
            System.out.println("album" + i.getAlbum());
            System.out.println("artist "+i.getArtist());
            System.out.println("bitrate "+i.getBitRate());
            System.out.println("genre "+i.getGenre());
            System.out.println("size "+i.getSize());
            System.out.println("duration "+i.getDuration().getMin()+":"+ i.getDuration().getSec());
            System.out.println("counter " + i.getReproductionsCounter());
            System.out.println("FINE");
        }
    }
}
