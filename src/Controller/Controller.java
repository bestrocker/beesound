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
import model.Playlist;
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
        this.audiocontrol = new AudioController(model);
        loadInfoToLibrary();
       
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void newPlaylistFile(final String name){
        if(this.filecontrol.notExist(FileController.playlistDirPath+name+".txt")){
            this.filecontrol.createNewFile(name, FileController.playlistDirPath);
        }
        this.model.newPlaylist(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSongInPlaylist(final Song song, final Playlist playlist){
        playlist.addSong(song);
        try {
            if(!this.filecontrol.getPlaylistSongs(
                    new File(playlist.getPath()))
                    .contains(song.getPath())){
                System.out.println("MERD");
                this.filecontrol.appendToFile(song.getPath(),playlist.getPath());
            }
        } catch (IOException e) {
            Log.ERROR("Can't addSongInPlaylist");
            e.printStackTrace();
        }
    }
    
    
    
    private void loadInfoToLibrary() {
        final String unk = "";
        final MpegInfo info = MpegInfo.getInstance();
        this.filecontrol.listAllSongPath().stream()
                           .forEach(i->{
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
                                .path(i)
                                .build()
                                );
                               });
        Log.INFO("Mpeg tag loaded into the library.");
    }
    
    public static void main(String[] args) throws InterruptedException{
        Controller c = new Controller();
        
        List<String> l = c.filecontrol.listAllSongPath();
        
        List<Song> lsong = c.model.getSongList();
        c.audiocontrol.setPlaylist(new Playlist("ciao", lsong));
       // c.audiocontrol.addSongInPlaylist("/home/bestrocker221/Dropbox/Musica/holdbacktheriver.mp3");
        c.audiocontrol.setReproductionStrategy(REPRODUCTION_STRATEGY.SHUFFLE);
        
       /* 
        c.newPlaylistFile("bella");
        System.out.println("CREATED : " +c.model.getPlaylistList().get(0).getPath());
        
       c.addSongInPlaylist(c.model.getSongList().get(0), c.model.getPlaylistList().get(0));
       
        System.out.println("TRYING TO SET PLAYLIST "+c.model.getPlaylistList().get(0).getPath());*/
        //c.audiocontrol.setPlaylist(c.model.getPlaylistList().get(0));
       
        c.audiocontrol.playPlayer();
      //  System.out.println(c.model.getSongList().get(0).getTitle() + " added to "+ c.model.getPlaylistList().get(0).getName() );
        System.out.println("import fatto");
       
        /*
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
        }*/
    }

    @Override
    public void addSong(String songPath) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Song> showAllSong() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Playlist> showAllPlaylist() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void playButton() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void playButton(Song song) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pauseButton() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stopButton() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setVolumeButton(double volume) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void skipTo(long toBytes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addSongInReproductionPlaylist(Song song) {
        this.audiocontrol.addSongInPlaylist(song);
        
    }
}
