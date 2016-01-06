package Controller.Audio;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Controller.Files.Log;
import javazoom.jlgui.basicplayer.*;

public class AudioController implements BasicPlayerListener{
    
    private List<String> playlist;
    final private BasicPlayer player;
    final private BasicController control;
    final private Random rnd = new Random();
    private int counter = 0;
    final private  PrintStream out ;
    final private MpegInfo mp3Info;
    private boolean strategy; /* if true linear, else shuffle */
    
    /**
     * This enum contain the reproduction strategy for obtaining the next track to play
     * @author bestrocker221
     *
     */
    public static enum REPRODUCTION_STRATEGY{
        LINEAR(true), SHUFFLE(false);
        
        final private boolean val;
        private REPRODUCTION_STRATEGY(final boolean b) {
           this.val = b;
        }
        public boolean getVal(){
            return this.val;
        }
    }
    
    public AudioController(){
        
        this.player = new BasicPlayer();
        this.control = (BasicController) player;
        this.player.addBasicPlayerListener(this);
        this.out = System.out;
        this.mp3Info = MpegInfo.getInstance();
    }
    
    /**
     * Obtain the MpegInfo instance
     * @return 
     */
    public MpegInfo getMpegInfo(){
        return this.mp3Info;
    }
    
    /**
     * Set the current Playlist to play to the given one.
     * @param playlist
     */
    public void setPlaylist(final List<String> playlist){
        this.playlist = playlist;
        Log.INFO("New playlist set");
    }
    
    /**
     * Add a song manually to the current playing Playlist
     * @param song
     */
    public void addSongInPlaylist(final String song){
        this.playlist.add(song);
        Log.INFO(song +" added to current playlist");
        
    }
    
    /**
     * Obtain the current playlist.
     * @return a List<String> representing the current playlist
     */
    public List<String> getPlaylist(){
        return this.playlist;
    }
    
    /**
     * Skip the song to the given number of bytes.
     * @param nbytes
     */
    public void seekPlayer(final int nbytes){
        try {
            this.control.seek(nbytes);
        } catch (BasicPlayerException e) {
            Log.ERROR("Error seeking song");
        }
    }
    
    /**
     * Play the next Track
     */
    public void nextPlayer(){
        try {
            control.stop();;
        } catch (BasicPlayerException e) {
            Log.ERROR("cannot stop, next() failed");
            e.printStackTrace();
        }
        nextSongPlayer();
    }
    
    /**
     * Play the previous Track.
     */
    public void prevPlayer(){
        try {
            control.stop();
        } catch (BasicPlayerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.counter--;
        this.playPlayer();
    }
    
    /**
     * Stop the Player.
     */
    public void stopPlayer(){
        try {
            this.control.stop();
        } catch (BasicPlayerException e) {
            Log.ERROR("Cannot stop player, error stopPlayer()");
        }
    }
    
    /**
     * Play the current song in the player.
     */
    public void playPlayer(){
        try {
            //System.out.println("canzone: "+this.playlist.get(this.counter));
            this.control.open(new File(this.playlist.get(this.counter)));
            this.control.play();
            this.control.setGain(0.85);
            this.control.setPan(0.0);
            
        } catch ( Exception e){
            Log.ERROR("can't perform action: playPlayer()");
            e.printStackTrace();
        }
    }
    
    /**
     * Set the strategy for reproducing the next tracks.
     * @param strategy
     */
    public void setReproductionStrategy(final REPRODUCTION_STRATEGY strategy){
        this.strategy = strategy.getVal();
    }
    
    /**
     * Set the next song to reproduce.
     */
    private void nextSongPlayer(){
        
        if(strategy) {
            if(this.counter + 1 > this.playlist.size()-1){
                this.stopPlayer();
                Log.INFO("playlist finished");
                return;
            }
            ++this.counter;
        } else {
            int c;
            while ( (c=rnd.nextInt(this.playlist.size()) ) != this.counter ){
                this.counter = c;
            }
        }
            this.playPlayer();
    }
    /*
    private void shuffleReproducing(){
        this.counter = rnd.nextInt(this.playlist.size()-1);
        this.play();
    }*/
    
    
    private void display(final String msg) {
        if (out != null) out.println(msg);
    }
    /*
     * @SuppressWarnig due to the native Library implementation (non-Javadoc)
     * @see javazoom.jlgui.basicplayer.BasicPlayerListener#opened(java.lang.Object, java.util.Map)
     */
    @SuppressWarnings("rawtypes") 
    @Override
    public void opened(final Object stream,final Map properties) {
            
            display("opened : "+properties.toString());             
    }
    
    /*
     * @SuppressWarnig due to the native Library implementation(non-Javadoc)
     * @see javazoom.jlgui.basicplayer.BasicPlayerListener#progress(int, long, byte[], java.util.Map)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void progress(final int bytesread,final long microseconds,
            final byte[] pcmdata,final Map properties) {
            
     //       display("progress : "+properties.toString());
    }
    
    @Override
    public void stateUpdated(final BasicPlayerEvent event) {
            // Notification of BasicPlayer states (opened, playing, end of media, ...)
            display("stateUpdated : "+event.toString());
            if (event.getCode()==BasicPlayerEvent.EOM) {
                    nextSongPlayer();
            }
    }
    
    @Override
    public void setController(final BasicController controller) {
        display("setController : "+controller);
    }
}
