package Controller.Audio;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.Random;
import Controller.Audio.AudioControllerInterface;
import Controller.Audio.MpegInfo;
import Controller.Audio.TagInfo;
import Controller.Files.Log;
import javazoom.jlgui.basicplayer.*;
import model.Manager;

/**
 * Audio Controller class.
 * @author bestrocker221
 *
 */
public class AudioController implements BasicPlayerListener, AudioControllerInterface {
    
    private Manager lm;
    private int counter = 0;
    private boolean reproduceNow;
    private boolean paused;
    private boolean strategy = true; /* if true linear, else shuffle */
    private final BasicPlayer player;
    private final BasicController control;
    private final Random rnd = new Random();
    private final  PrintStream out;
    private final TagInfo mp3Info;
    
    
    /**
     * This enum contain the reproduction strategy for obtaining the next track to play.
     * @author bestrocker221
     *
     */
    public static enum REPRODUCTION_STRATEGY{
        /**
         * Reproduce songs one after the other.
         */
        LINEAR(true),
        /**
         * Reproduce songs casually.
         */
        SHUFFLE(false);
        
        private final boolean val;
        REPRODUCTION_STRATEGY(final boolean b) {
           this.val = b;
        }
        /**
         * Return strategy value.
         * @return boolean
         */
        public boolean getVal() {
            return this.val;
        }
    }
    /**
     * Audio Controller for mp3 songs.
     * @param Manager c
     * @author bestrocker221
     */
    public AudioController(final Manager c) {
        this.lm = c;
        this.player = new BasicPlayer();
        this.control = this.player;
        this.player.addBasicPlayerListener(this);
        this.mp3Info = MpegInfo.getInstance();
        this.out = System.out;
        try {
            this.control.setGain(0.50);
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Obtain the MpegInfo instance.
     * @return tagInfo
     */
    public TagInfo getMpegInfo() {
        return this.mp3Info;
    }
    
    /**
     *Set the class model.
     *@param model model
     */
    public void setModel(final Manager model) {
        this.lm = model;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void togglePause() {
        try {
            if (!this.paused) {
                this.control.pause();
                Log.INFO("Song paused.");
                this.paused = true;
                
            } else {
                this.control.seek(this.getPos());
                Thread.sleep(5); // seek at same position and sleep(1) needed because of audio noise.
                this.control.resume();
                Log.INFO("Song resumed.");
                this.paused = false;
            }
        } catch (BasicPlayerException | InterruptedException e) {
            e.printStackTrace();
            Log.ERROR("impossible toggle pause" + e);
        }
        this.lm.setSongPaused(this.paused);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void seekPlayer(final long nbytes) {
        try {
            this.control.seek(nbytes);
        } catch (Exception e) {
            Log.ERROR("Error seeking song" + e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPlayer() {
        try {
            control.stop();
            nextSongPlayer();
        } catch (BasicPlayerException e) {
            Log.ERROR("cannot stop, next() failed" + e);
            e.printStackTrace();
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void prevPlayer() {
        try {
            control.stop();
            if (this.counter == 0) {
                return;
            }
            this.counter--;
            this.playPlayer();
        } catch (BasicPlayerException e) {
            Log.ERROR("prevPlayer FAILED." + e);
            e.printStackTrace();
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stopPlayer() {
        try {
            this.control.stop();
            Log.PROGRAM("Player stopped.");
        } catch (BasicPlayerException e) {
            Log.ERROR("Cannot stop player, error stopPlayer()"  + e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void playPlayer() {
        try {
            String s = "empty";
            if (reproduceNow) {
                s = this.lm.getCurrentSong(0);
                reproduceNow = false;
            } else {
                s = this.lm.getCurrentSong(this.counter);
            }
            this.lm.setInReproduction(s);
            Log.INFO("Set as Song in reproduction: " + s);
            this.paused = false;
            this.control.open(new File(s));
            Thread.sleep(100);
            this.control.play();
            this.control.setPan(0.0);
        } catch (BasicPlayerException | InterruptedException e) {
            Log.ERROR("can't perform action: playPlayer()"  + e);
            e.printStackTrace();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setReproductionStrategy(Controller.Audio.AudioController.REPRODUCTION_STRATEGY strategy) {
        this.strategy = strategy.getVal();
        if (strategy.getVal()) {
            Log.INFO("Reproduction set to Linear.");
        } else {
            Log.INFO("Reproduction set to Shuffle.");
        }
    }
    
    /**
     * Set the next song to reproduce.
     */
    private void nextSongPlayer() {
        if (strategy) {
            if (this.counter + 1 > this.lm.getQueueSize() - 1) {
                this.stopPlayer();
                Log.INFO("playlist finished");
                return;
            }
            ++this.counter;
        } else {
            int c;
            while ((c = rnd.nextInt(this.lm.getQueueSize())) == this.counter){}
            this.counter = c;
        }
        this.playPlayer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setVolume(final double volume)  {
        try {
            this.control.setGain(volume);
        } catch (BasicPlayerException e) {
            Log.ERROR("impossible to change volume, error in setVolume " + e);
            e.printStackTrace();
        }
    }
    
    private void display(final String msg) {
        if (out != null) {
            out.println(msg);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setReproduceNowBoolean(final boolean b) {
        this.reproduceNow = b;
    }
    
    /*
     * @SuppressWarnig due to the native Library implementation (non-Javadoc)
     * @see javazoom.jlgui.basicplayer.BasicPlayerListener#opened(java.lang.Object, java.util.Map)
     */
    @SuppressWarnings("rawtypes") 
    @Override
    public void opened(final Object stream, final Map properties) {
            
            display("opened : " + properties.toString());             
    }
    
    private int position;
    private Object obj = new Object();
    /*
     * @SuppressWarnig due to the native Library implementation(non-Javadoc)
     * @see javazoom.jlgui.basicplayer.BasicPlayerListener#progress(int, long, byte[], java.util.Map)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void progress(final int bytesread, final long microseconds,
            final byte[] pcmdata, final Map properties) {
       synchronized (obj) {
        position = Long.valueOf((long) (properties.get("mp3.position.byte"))).intValue();
       }
           // display("progress : "+properties.toString());
    }
    
    @Override
    public void stateUpdated(final BasicPlayerEvent event) {
            // Notification of BasicPlayer states (opened, playing, end of media, ...)
            display("stateUpdated : " + event.toString());
            if (event.getCode() == BasicPlayerEvent.EOM) {
                    nextSongPlayer();
            }
    }
    
    @Override
    public void setController(final BasicController controller) {
        display("setController : " + controller);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPaused(){
        return this.paused;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getPos() {
        return this.position;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setPos(final int pos) {
        this.position = pos;
    }
    
}
