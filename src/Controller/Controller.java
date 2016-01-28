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
import model.Manager;
import view.GUI.PROGRESS_BAR;
import view.ViewInterface;
import view.ViewObserver;

public class Controller implements ViewObserver {

    private final Manager model;
    private final ViewInterface view;
    private final AudioControllerInterface audiocontrol;
    private SystemManager filecontrol;
    
    public Controller(final ViewInterface view, final Manager model) {
        this.model=model;
        this.view=view;
        this.filecontrol = new FileController();
        this.audiocontrol = new AudioController(model);
        this.view.setObserver(this);
        loadInfoToLibrary();
        this.view.refreshView();        
        this.view.setVisible(true);
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
        this.view.refreshView();
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
            Log.ERROR("Can't addSongInPlaylist"+e);
            e.printStackTrace();
        }
    }
    
    /*
     * Load all info into the library.
     */
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
                                this.filecontrol.getPlaylistSongs(new File(i))
                                    .stream()
                                    .forEach(j->this.model.addSongInPlaylist(j.substring(j.lastIndexOf(sep)+1,j.length()-4), plname));
                            } catch (Exception e) {
                                Log.ERROR("Can't load PLAYLIST to library." +e);
                            }
                         });
        Log.INFO("Playlist succesfully loaded into the library.");
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
        this.view.refreshView();
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
      //  this.view.updateProgressBar(PROGRESS_BAR.ACTIVE);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseButton() {
        this.audiocontrol.togglePause();
        //this.view.updateProgressBar(PROGRESS_BAR.PAUSE);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stopButton() {
       this.audiocontrol.stopPlayer();
      // this.view.updateProgressBar(PROGRESS_BAR.PAUSE);
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
        //    this.view.updateProgressBar(PROGRESS_BAR.ACTIVE);
        }
    }
    
    /**
     * Enum for setting the priority of a song to be reproduced.
     * NOW-AFTER.
     * @author bestrocker221
     *
     */
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
     //   this.view.updateProgressBar(PROGRESS_BAR.ACTIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prevTrack() {
        this.audiocontrol.prevPlayer();
    //    this.view.updateProgressBar(PROGRESS_BAR.ACTIVE);
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
        this.view.refreshView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSongFromQueue(final String songTitle) {
        this.model.removeSongFromQueue(songTitle);
        Log.INFO(songTitle + " Removed from reproduction Queue");
        this.view.refreshView();
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
    public void removePlaylist(String namePlaylist) {
        this.model.removePlaylist(namePlaylist);
        this.filecontrol.delete(playlistDirPath+namePlaylist+".txt");
        this.view.refreshView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showFavorites() {
        return this.model.getMostListened();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newLibrary(String pathNewLibrary) {
        this.model.resetLibrary();
        this.filecontrol = new FileController(pathNewLibrary);
        loadInfoToLibrary();
        this.view.refreshView(); 
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void playPlaylist(String playlistName) {
        this.model.setReproductionPlaylist(playlistName);
        this.audiocontrol.playPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSongFromPlaylist(String songName, String playlistName) {
        this.model.removeSongFromPlaylist(songName, playlistName);
        this.view.refreshView();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> showLibraryInfo() {
        return this.model.getLibraryInfo();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> searchSong(String text) {
        return this.model.fetchSongs(text);
    }
   
    public int getPos(){
        return this.audiocontrol.getPos();
    }
}
