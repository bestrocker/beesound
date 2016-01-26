package Controller;

import java.io.File;
import static Controller.Audio.AudioController.REPRODUCTION_STRATEGY.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import Controller.Audio.AudioController;
import Controller.Audio.AudioControllerInterface;
import Controller.Audio.MpegInfo;
import Controller.Files.FileController;
import static Controller.Files.FileController.*;
import Controller.Files.Log;
import Controller.Files.SystemManager;
import model.LibraryManager;
import model.Manager;
import view.GUI;
import view.ViewInterface;
import view.ViewObserver;

public class Controller implements ViewObserver {

    private final Manager model;
    private final ViewInterface view;
    private final AudioControllerInterface audiocontrol;
    private final SystemManager filecontrol;
    
    public Controller(final ViewInterface view, final Manager model) {
        /*this.model = LibraryManager.getInstance();        
        this.filecontrol = new FileController();
        this.audiocontrol = new AudioController(model);
        loadInfoToLibrary();
        this.view = new GUI();
        this.view.setObserver(this);
       */
        this.model=model;
        this.view=view;
        this.filecontrol = new FileController();
        this.audiocontrol = new AudioController(model);
        loadInfoToLibrary();
        this.view.setObserver(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void newPlaylistFile(final String name){
        final String temp = playlistDirPath+name+".txt";
        if(this.filecontrol.notExist(temp)){
            this.filecontrol.createNewFile(name,playlistDirPath);
            this.model.newPlaylist(name,temp);
        }
        //AGGIUNGERE CODICE "ESISTE GIà"
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSongInPlaylist(final String song, final String playlist){
       this.model.addSongInPlaylist(song,playlist);
       final String songPath= this.model.getSongPath(song);
       final String playlistPath = this.model.getPlaylistPath(playlist);
        try {
            if(!this.filecontrol.getPlaylistSongs(new File(playlistPath)).contains(songPath)){
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
        this.filecontrol.listAllPlaylist()
                        .stream()
                        .forEach(i->{
                            String plname = i.substring(i.lastIndexOf(sep)+1,i.length()-4);
                            this.model.newPlaylist(plname, i);
                            try {
                                this.filecontrol.getPlaylistSongs(new File(i)).stream()
                                    .forEach(j->this.model.addSongInPlaylist(j.substring(j.lastIndexOf(sep)+1,j.length()-4), plname));
                            } catch (Exception e) {
                                Log.ERROR("Can't load PLAYLIST to library.");
                            }
                         });
        Log.INFO("Playlist succesfully loaded into the library.");
    }
    
    public static void main(String[] args) throws InterruptedException{
        //new Controller();
        
       // List<String> l = c.filecontrol.listAllSongPath();
        
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
    
    private void addSong(final String songPath, final MpegInfo info){
        final String alternativeTitle = songPath.substring(songPath.lastIndexOf(sep)+1,songPath.length()-4);
        if(!this.model.getSongTitles().contains(alternativeTitle)){
            this.model.addSongToLibrary(
                     (info.getTitle().orElse(alternativeTitle)) ,(info.getAlbum().orElse("")) 
                     ,(info.getArtist().orElse("")) ,(info.getGenre().orElse("")) ,(info.getDurationInMinutes())
                    ,(info.getBitRate()) ,(info.getSize()) ,(songPath) ,(info.getCopyright()) ,(info.getChannels())
                    ,(info.getVersion()),(info.getSamplingRate()),(info.getChannelsMode())
                    );
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSong(final String songPath) {
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
    public void setVolumeButton(final double volume) {
        this.audiocontrol.setVolume(volume);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void skipTo(final long toBytes) {
        this.audiocontrol.seekPlayer(toBytes);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSongInReproductionPlaylist(final String song,final REPRODUCE when) {
        this.model.addSongInPlaylist(song, when.getVal());
        Log.INFO(song + " added in reproduction playlist.");
        if(when.getVal()){
            this.audiocontrol.setReproduceNowBoolean(true);
            this.audiocontrol.playPlayer();
        }
    }
    
    public enum REPRODUCE{
        NOW(true),AFTER(false);
        
        final private boolean val;
        private REPRODUCE(final boolean b) {
           this.val = b;
        }
        public boolean getVal(){
            return this.val;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setShuffleMode() {
        this.audiocontrol.setReproductionStrategy(SHUFFLE);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void linearMode() {
        this.audiocontrol.setReproductionStrategy(LINEAR);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showAllAlbum() {
        return this.model.getAlbumTitles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showAllGenre() {
       return this.model.getGenreNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showAllArtist() {
        return this.model.getArtistNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextTrack() {
        this.audiocontrol.nextPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prevTrack() {
        this.audiocontrol.prevPlayer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showReproductionPlaylist() {
        return this.model.getInReproductionTitles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSong(final String songTitle) {
        this.model.removeSong(songTitle);
        this.filecontrol.delete(musicDirPath+songTitle+".mp3");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSongFromQueue(final String songTitle) {
        this.model.removeSongFromQueue(songTitle);
        Log.INFO(songTitle + " Removed from reproduction Queue");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showPlaylistSong(String playlistName) {
        return this.model.showPlaylistSong(playlistName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getCurrentSongInfo() {
        return this.model.getCurrentSongInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlaylit(String namePlaylist) {
        this.model.removePlaylist(namePlaylist);
        this.filecontrol.delete(playlistDirPath+namePlaylist+".txt");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showFavorites() {
        return this.model.getMostListened();
    }
}
