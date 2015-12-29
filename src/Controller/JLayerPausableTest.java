package Controller;
import java.io.*;

import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;

public class JLayerPausableTest {
    
    public JLayerPausableTest(){
        new Ciao();
    }
    public class Ciao extends JLayerPlayerPausable.PlaybackListener{
        String filePath;
        JLayerPlayerPausable player2;
        public Ciao() {
            try {
                String urlAsString = "file:///" + "/home/bestrocker221/Salmo-1984.mp3";
                this.player2 = new JLayerPlayerPausable(new java.net.URL(urlAsString),this );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                this.player2.play(8200);
                System.out.println("preSleep\n");
                Thread.sleep(3000);
                System.out.println("sleep");
                this.player2.stop();
                this.player2.play(200);
                Thread.sleep(2500);
                this.player2.play(9200);
                
            } catch (Exception e) {
                System.out.println("error play on selected frame.");
                e.printStackTrace();
            }
        }
        
        
    }
        public static void main(String[] args) throws InterruptedException {
                
            //new JLayerPausableTest();    
                final String path = "/home/bestrocker221/Salmo-1984.mp3";
                SoundJLayer soundToPlay = new SoundJLayer(path);

                //BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("About to start playing sound.");
                System.out.println("Press enter to pause...");
                File f = new File(path);
                soundToPlay.play();
                Thread.sleep(2000);
                soundToPlay.play(2000);
                
                Thread.sleep(3000);
                System.out.println("doposleep£");
                soundToPlay.play(7000);
                Thread.sleep(3000);
                soundToPlay.play();
                MpegInfo infosong = new MpegInfo();
                try {
                    infosong.load(f);
                } catch (IOException | UnsupportedAudioFileException e) {
                    System.out.println("error reading metadata");
                    e.printStackTrace();
                }

//                while (true){
//                        Thread.sleep(4000);
//                        System.out.println("toggling pause");
//
//                        soundToPlay.pauseToggle();
//                }
                
        }
}

class SoundJLayer extends JLayerPlayerPausable.PlaybackListener implements Runnable {
        private String filePath;
        private JLayerPlayerPausable player;
        private Thread playerThread;    
        volatile boolean flag = false;
        private int frameSelected=0;
        public SoundJLayer(String filePath){
                this.filePath = filePath;       
        }

        public void pause() {
                this.player.pause();
                //this.playerThread.stop();
                this.playerThread = null; //metodo funzionante per "stoppare" un thread
        }

        public void pauseToggle() {
                if (this.player.isPaused) {
                        this.play();
                } else {
                        this.pause();
                }
        }

        public void play() {
            this.flag = false;
                if (this.player == null) {
                        this.playerInitialize();
                }
                pause();
                this.playerThread = new Thread(this, "AudioPlayerThread");

                this.playerThread.start();
        }
        public void play(final int frame){
            
            this.frameSelected=frame;
            this.flag = true;
            pause();
            if (this.player == null) {
                this.playerInitialize();
            }

            this.playerThread = new Thread(this, "AudioPlayerThread");
            System.out.println("play with frames left\n");
            this.playerThread.start();
        }

        private void playerInitialize() {
                try {
                        String urlAsString = "file:///" + this.filePath;
                        this.player = new JLayerPlayerPausable(new java.net.URL(urlAsString),this );
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
        }

        // PlaybackListener members

        public void playbackStarted(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
                System.out.println("playbackStarted()");
        }

        public void playbackFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
                System.out.println("playbackEnded()");
        }       

        // IRunnable members

        public void run() {
                try {
                        if (flag){
                            this.player.resume(this.frameSelected);
                            System.out.println("resume with frame called\n");
                        } else {
                            this.player.resume();
                        }
                } catch (javazoom.jl.decoder.JavaLayerException ex) {
                        ex.printStackTrace();
                }

        }
}