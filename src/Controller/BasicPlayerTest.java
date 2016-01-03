package Controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jlgui.basicplayer.*;

public class BasicPlayerTest implements BasicPlayerListener {
    private PrintStream out = null;
    
    /**
     * Entry point.
     * @param args filename to play.
     * @throws InterruptedException 
     * @throws UnsupportedAudioFileException 
     * @throws IOException 
     */
    public static void main(String[] args) throws InterruptedException, IOException, UnsupportedAudioFileException {
            BasicPlayerTest test = new BasicPlayerTest();
            test.play("/home/bestrocker221/Salmo-1984.mp3");
            MpegInfo infosong = new MpegInfo();
            infosong.load(new File("/home/bestrocker221/Salmo-1984.mp3"));
            System.out.println(infosong.getInfo());
    }
    
    /**
     * Contructor.
     */
    public BasicPlayerTest() {
            out = System.out;
    }

    public void play(String filename) throws InterruptedException {
            // Instantiate BasicPlayer.
            BasicPlayer player = new BasicPlayer();
            // BasicPlayer is a BasicController.
            BasicController control = (BasicController) player;
            // Register BasicPlayerTest to BasicPlayerListener events.
            // It means that this object will be notified on BasicPlayer
            // events such as : opened(...), progress(...), stateUpdated(...)
            player.addBasicPlayerListener(this);

            try {                   
                    // Open file, or URL or Stream (shoutcast) to play.
                    control.open(new File(filename));
                    // control.open(new URL("http://yourshoutcastserver.com:8000"));
                    
                    // Start playback in a thread.
                    control.play();
                    
                    // Set Volume (0 to 1.0).
                    // setGain should be called after control.play().
                    control.setGain(0.85);
                    
                    // Set Pan (-1.0 to 1.0).
                    // setPan should be called after control.play().
                    control.setPan(0.0);
                    
                    // If you want to pause/resume/pause the played file then
                    // write a Swing player and just call control.pause(),
                    // control.resume() or control.stop().                  
                    // Use control.seek(bytesToSkip) to seek file
                    // (i.e. fast forward and rewind). seek feature will
                    // work only if underlying JavaSound SPI implements
                    // skip(...). True for MP3SPI (JavaZOOM) and SUN SPI's
                    // (WAVE, AU, AIFF).
                    
            } catch (BasicPlayerException e) {
                    e.printStackTrace();
            }
    }
            
    /**
     * Open callback, stream is ready to play.
     *
     * properties map includes audio format dependant features such as
     * bitrate, duration, frequency, channels, number of frames, vbr flag,
     * id3v2/id3v1 (for MP3 only), comments (for Ogg Vorbis), ... 
     *
     * @param stream could be File, URL or InputStream
     * @param properties audio stream properties.
     */
    @SuppressWarnings("rawtypes")
    /*  Map instead of Map<String,Object>
     *  Because the method to override is in the library, and it is made as this (non-Javadoc)
     * @see javazoom.jlgui.basicplayer.BasicPlayerListener#opened(java.lang.Object, java.util.Map)
     */
    @Override
    public void opened(final Object stream,final Map properties) {
            // Pay attention to properties. It's useful to get duration, 
            // bitrate, channels, even tag such as ID3v2.
        //    display("opened : "+properties.toString());             
    }
            
    /**
     * Progress callback while playing.
     * 
     * This method is called severals time per seconds while playing.
     * properties map includes audio format features such as
     * instant bitrate, microseconds position, current frame number, ... 
     * 
     * @param bytesread from encoded stream.
     * @param microseconds elapsed (<b>reseted after a seek !</b>).
     * @param pcmdata PCM samples.
     * @param properties audio stream parameters.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void progress(final int bytesread,final long microseconds,
            final byte[] pcmdata,final Map properties) {
            // Pay attention to properties. It depends on underlying JavaSound SPI
            // MP3SPI provides mp3.equalizer.
     //       display("progress : "+properties.toString());
    }
    
    /**
     * Notification callback for basicplayer events such as opened, eom ...
     *  
     * @param event
     */
    @Override
    public void stateUpdated(final BasicPlayerEvent event) {
            // Notification of BasicPlayer states (opened, playing, end of media, ...)
            display("stateUpdated : "+event.toString());
            if (event.getCode()==BasicPlayerEvent.STOPPED) {
                    //System.exit(0);
            }
    }

    /**
     * A handle to the BasicPlayer, plugins may control the player through
     * the controller (play, stop, ...)
     * @param controller : a handle to the player
     */
    public void setController(final BasicController controller) {
            display("setController : "+controller);
    }
    
    public void display(final String msg) {
            if (out != null) out.println(msg);
    }

}
