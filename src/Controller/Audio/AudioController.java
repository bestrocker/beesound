package Controller.Audio;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.Random;
import Controller.Files.Log;
import javazoom.jlgui.basicplayer.*;
import model.Manager;

public class AudioController implements BasicPlayerListener{
    
    final private Manager lm;
    private int counter = 0;
    private boolean reproduceNow;
    private boolean paused;
    private boolean strategy; /* if true linear, else shuffle */
    final private BasicPlayer player;
    final private BasicController control;
    final private Random rnd = new Random();
    final private  PrintStream out ;
    final private MpegInfo mp3Info;
    
    
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
    
    public AudioController(final Manager c){
        
        this.lm = c;
        this.player = new BasicPlayer();
        this.control = this.player;
        this.player.addBasicPlayerListener(this);
        this.mp3Info = MpegInfo.getInstance();
        this.out = System.out;
    }
    
    /**
     * Obtain the MpegInfo instance
     * @return 
     */
    public MpegInfo getMpegInfo(){
        return this.mp3Info;
    }
    
    public void togglePause(){
        try{
            if(!this.paused){
                this.control.pause();
                this.paused = true;
                
            } else {
                this.control.resume();
                this.paused = false;
            }
        } catch (BasicPlayerException e){
            e.printStackTrace();
            Log.ERROR("impossible toggle pause");
        }
        this.lm.setSongPaused(this.paused);
    }
    
    /**
     * Skip the song to the given number of bytes.
     * @param nbytes
     */
    public void seekPlayer(final long nbytes){
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
            String s = "empty";
            //String s = this.playlist.get(this.counter);
            //this.incrementSongCounter(this.lsong.get(this.counter));
            //String s = this.lsong.get(this.counter).getPath();
            if(reproduceNow){
                s = this.lm.getCurrentSong(0);
                reproduceNow = false;
            } else {
                s = this.lm.getCurrentSong(this.counter);
            }
            this.lm.setInReproduction(s);
            Log.INFO(s + " set as Song in reproduction");
            this.paused = false;
            this.control.open(new File(s));
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
        if(strategy.getVal()){
            Log.INFO("Reproduction set to Linear.");
        } else {
            Log.INFO("Reproduction set to Shuffle.");
        }
    }
    
    /**
     * Set the next song to reproduce.
     */
    private void nextSongPlayer(){
        if(strategy) {
           // if(this.counter + 1 > this.playlist.size()-1){
            if(this.counter + 1 > this.lm.getQueueSize() -1 ){
                this.stopPlayer();
                Log.INFO("playlist finished");
                return;
            }
            ++this.counter;
        } else {
            int c;
            while ( (c=rnd.nextInt(this.lm.getQueueSize()) ) == this.counter ){}
            this.counter = c;
        }
        this.playPlayer();
    }
    
    public void setVolume(final double volume){
        try {
            this.control.setGain(volume);
        } catch (BasicPlayerException e) {
            Log.ERROR("impossible to change volume, error in setVolume");
            e.printStackTrace();
        }
    }
    
    private void display(final String msg) {
        if (out != null) out.println(msg);
    }
    
    public void setReproduceNowBoolean(final boolean b){
        this.reproduceNow = b;
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
