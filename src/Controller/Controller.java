package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import Controller.Audio.AudioController;
import Controller.Audio.MpegInfo;
import Controller.Audio.AudioController.REPRODUCTION_STRATEGY;
import Controller.Files.FileController;
import Controller.Files.Log;
import model.LibraryManager;
import view.GUI;

public class Controller implements ViewObserver {

    private final LibraryManager model;
    private final GUI view;
    private final AudioController audiocontrol;
    private final FileController filecontrol;
    
    public Controller() {
        this.model = LibraryManager.getInstance();        
        this.filecontrol = new FileController();
        this.audiocontrol = new AudioController(model);
        this.view = new GUI();
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
        this.model.newPlaylist(name,FileController.playlistDirPath + name + ".txt");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSongInPlaylist(final String song, final String playlist){
       this.model.addSongInPlaylist(song,playlist);
        /* playlist.addSong(song);*/
       final String songPath= this.model.getSongPath(song);
       final String playlistPath = this.model.getPlaylistPath(playlist);
        try {
            if(!this.filecontrol.getPlaylistSongs(new File(playlistPath))
                    .contains(songPath)){
                this.filecontrol.appendToFile(songPath,playlistPath);
                Log.INFO(songPath+ " Added to playlist "+playlist);
            }
        } catch (IOException e) {
            Log.ERROR("Can't addSongInPlaylist");
            e.printStackTrace();
        }
    }
    
    private void loadInfoToLibrary() {
        final MpegInfo info = MpegInfo.getInstance();
        this.filecontrol.listAllSongPath()
                        .stream()
                        .forEach(i->{
                             if(info.load(new File(i))){
                                 this.addSong(i, info);
                             }
                         });
        Log.INFO("Mpeg tag succesfully loaded into the library.");
    }
    
    public static void main(String[] args) throws InterruptedException{
        Controller c = new Controller();
        
        List<String> l = c.filecontrol.listAllSongPath();
        
    //    List<Song> lsong = c.model.getSongList();
        
       // c.audiocontrol.addSongInPlaylist("/home/bestrocker221/Dropbox/Musica/holdbacktheriver.mp3");
      //  c.audiocontrol.setReproductionStrategy(REPRODUCTION_STRATEGY.SHUFFLE);
    //    c.audiocontrol.setPlaylist(c.model.getPlaylistList().get(0));
       /* 
        c.newPlaylistFile("bella");
        System.out.println("CREATED : " +c.model.getPlaylistList().get(0).getPath());
        
       c.addSongInPlaylist(c.model.getSongList().get(0), c.model.getPlaylistList().get(0));
       
        System.out.println("TRYING TO SET PLAYLIST "+c.model.getPlaylistList().get(0).getPath());*/
        //c.audiocontrol.setPlaylist(c.model.getPlaylistList().get(0));
       
        //c.audiocontrol.playPlayer();
       // Thread.sleep(10000);
      //  c.playButton(c.model.getSongList().get(3));
      //  System.out.println(c.model.getSongList().get(0).getTitle() + " added to "+ c.model.getPlaylistList().get(0).getName() );
       // System.out.println("import fatto");
       /*
        while (true){
        Thread.sleep(2000);
        c.audiocontrol.nextPlayer();
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
        }*/
    }
    
    private void addSong(String songPath, MpegInfo info){
        final String unk = "";
        this.model.addSongToLibrary(
                (info.getTitle().orElse(songPath.substring(songPath.lastIndexOf(FileController.sep)+1,songPath.length()-4)))
               ,(info.getAlbum().orElse(unk))
               ,(info.getArtist().orElse(unk))
               ,(info.getGenre().orElse(unk))
               ,(info.getDurationInMinutes())
               ,(info.getBitRate())
               ,(info.getSize())
               ,(songPath)
               ,(info.getCopyright())
               ,(info.getChannels())
               ,(info.getVersion())
               ,(info.getSamplingRate())
               ,(info.getChannelsMode())
               );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSong(String songPath) {
      //  String path =this.filecontrol.importToLibrary(songPath);
        MpegInfo info = MpegInfo.getInstance();
        info.load(new File(this.filecontrol.importToLibrary(songPath)));
        this.addSong(songPath, info);
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showAllSong() {
        return this.model.getSongTitles();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showAllPlaylist() {
        return this.model.getPlaylistNames();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void playButton() {
        this.audiocontrol.playPlayer();
    }
    
    /**
     * {@inheritDoc}
     */
    /*
    public void playButton(Song song) {
        this.audiocontrol.playPlayer(song);
    }
    */
    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseButton() {
        this.audiocontrol.togglePause();
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stopButton() {
       this.audiocontrol.stopPlayer();
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setVolumeButton(double volume) {
        this.audiocontrol.setVolume(volume);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void skipTo(long toBytes) {
        this.audiocontrol.seekPlayer(toBytes);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSongInReproductionPlaylist(String song) {
        this.model.addSongInPlaylist(song);
    }
}
