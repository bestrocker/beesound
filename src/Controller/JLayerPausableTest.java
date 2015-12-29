package Controller;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
                soundToPlay.play(9400);
                
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
        private List<String> plist = new ArrayList<>();
        private int counter = 0;
        
//        public SoundJLayer(String filePath){
//                this.filePath = filePath;       
//        }
        /*
         * Aggiunto da me
         */
        public SoundJLayer(final List<String> pl){
            this.plist.addAll(pl);
            setSongInPlaylist();
            
            
        }
        
        public void setSongInPlaylist(){
            this.filePath = this.plist.get(this.counter++);
            this.playerInitialize();
        }
        
//        public void initPlaylist(final List<String> pl){
//            this.player.takePlaylist(pl);
//        }
        
        public void stop(){
            this.player.stop();
            this.playerThread = null;
        }
        public void pause() {
                if(this.player!= null) {
                    this.player.pause();
                }
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
          //  System.out.println("play with frames\n");
            
            pause();
            if (this.player == null) {
                this.playerInitialize();
            }

            this.playerThread = new Thread(this, "AudioPlayerThread");
           // System.out.println("play with frames left\n");
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
                System.out.println(new java.util.Date()+" playback: Started()  "+this.filePath);
        }

        public void playbackFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
                System.out.println("playback: Ended()");
        }   
        public void playbackSongFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent) {
            System.out.println("playback: SongEnded()");
            this.stop();
            this.setSongInPlaylist();
            this.play();
    }   

        // IRunnable members

        public void run() {
                try {
                        if (flag){
                            this.player.resume(this.frameSelected);
                        } else {
                            this.player.resume();
                        }
                } catch (javazoom.jl.decoder.JavaLayerException ex) {
                        ex.printStackTrace();
                }

        }
}