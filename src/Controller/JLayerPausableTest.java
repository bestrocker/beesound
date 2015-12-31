package Controller;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;

public class JLayerPausableTest {
    
        public static void main(String[] args) throws InterruptedException {
                
                final String path = "/home/bestrocker221/Salmo-1984.mp3";
                final String path2 = "/home/bestrocker221/Dropbox/Musica/James Bay - Hold Back The River.mp3";
                File f = new File(path);
                //SoundJLayer soundToPlay = new SoundJLayer(path);
                final List<String> playlist = new ArrayList<>();
                playlist.add(path);
                playlist.add("/home/bestrocker221/Dropbox/Musica/James Bay - Hold Back The River.mp3");
                System.out.println("Contenuti in playlist: ");
                playlist.forEach(System.out::println);
                System.out.println("About to start playing sound.");
                
                SoundJLayer soundToPlay = new SoundJLayer(playlist);
                
                MpegInfo infosong = new MpegInfo();
                try {
                    infosong.load(f);
                } catch (IOException | UnsupportedAudioFileException e) {
                    System.out.println("error reading metadata");
                    e.printStackTrace();
                }
                Random rnd = new Random();
                soundToPlay.play();
                while (true){
                    Thread.sleep(3000);
                    soundToPlay.play(rnd.nextInt(7000));
                    Thread.sleep(2000);
                    soundToPlay.pause();
                    Thread.sleep(2000);
                    soundToPlay.pauseToggle();
                }
                
                
        }
}

class SoundJLayer extends JLayerPlayerPausable.PlaybackListener implements Runnable {
        private String filePath;
        private JLayerPlayerPausable player;
        private Thread playerThread;    
        
        private int frameSelected=0;
        final private List<String> plist = new ArrayList<>();
        private int counter = 0;
        
        /**
         * SoundJLayer takes a List<String> and set it as current playlist to reproduce
         * 
         * @param playlist
         */
        public SoundJLayer(final List<String> playlist){
            this.plist.addAll(playlist);
            setSongInPlaylist();
        }
        
        /**
         * Private: Set the current song to reproduce taking it by the current playlist.
         * 
         * @return void
         */
        private void setSongInPlaylist(){
            this.filePath = this.plist.get(this.counter++);
            this.playerInitialize();
        }
        /**
         * Stop the player.
         * @return void
         */
        public void stop(){
            this.player.stop();
            this.playerThread = null;
        }
        
        /**
         * Pause the player. 
         * @return void
         */
        public void pause() {
            if(this.player!= null) {
                this.player.pause();
            }
            this.playerThread = null; 
        }
        
        /**
         * Pause Toggle button.
         * If isPaused then play() otherwise pause()
         */
        public void pauseToggle() {
                if (this.player.isPaused) {
                        this.play();
                } else {
                        this.pause();
                }
        }
        
        /**
         * Initialize the player if not initialized and start a new Thread AudioPlayerThread
         * @return void
         */
        public void play() {
            if (this.player == null) {
                    this.playerInitialize();
            }
            pause();
            this.playerThread = new Thread(this, "AudioPlayerThread");
            this.playerThread.start();
        }
        /**
         * Play the song from the frame in input
         * @param frame
         * @return void
         */
        public void play(final int frame){
            this.frameSelected=frame;
            this.play();
        }
        
        /**
         * Private: Player initializer
         * Create a new Pausable Player and set the path of the current song
         * @exception MalformedInputException
         */
        private void playerInitialize() {
                try {
                        String urlAsString = "file:///" + this.filePath;
                        this.player = new JLayerPlayerPausable(new java.net.URL(urlAsString),this );
                } catch (JavaLayerException | MalformedURLException ex) {
                        ex.printStackTrace();
                }
        }

        /*
         *  PlaybackListener members(non-Javadoc)
         * @see Controller.JLayerPlayerPausable.PlaybackListener#playbackStarted(Controller.JLayerPlayerPausable.PlaybackEvent)
         */

        public void playbackStarted(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
                System.out.println(new java.util.Date()+" playback: Started()  "+this.filePath);
        }

        public void playbackFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
                System.out.println("playback: Ended()");
        }   
        public void playbackSongFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
            System.out.println("playback: SongEnded()");
            /*
             * for reproducing audio one after another
             */
            this.stop();
            this.setSongInPlaylist();
            this.play();
    }   

        // IRunnable members

        public void run() {
                try {
                        if (this.frameSelected != 0){
                            this.player.resume(this.frameSelected);
                            frameSelected = 0;
                        } else {
                            this.player.resume();
                        }
                } catch (javazoom.jl.decoder.JavaLayerException ex) {
                        ex.printStackTrace();
                }

        }
}