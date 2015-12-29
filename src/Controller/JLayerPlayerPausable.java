package Controller;
/* *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

import java.net.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

public class JLayerPlayerPausable {
    // This class is loosely based on javazoom.jl.player.AdvancedPlayer.

    private java.net.URL urlToStreamFrom;
    private Bitstream bitstream;
    private Decoder decoder;
    private AudioDevice audioDevice;
    private boolean isClosed = false;
    private boolean isComplete = false;
    private PlaybackListener listener;
    private int frameIndexCurrent;
    public boolean isPaused;
    
    public boolean stopped = false;

    public JLayerPlayerPausable(URL urlToStreamFrom,PlaybackListener listener) 
            throws JavaLayerException{
        this.urlToStreamFrom = urlToStreamFrom;
        this.listener = listener;
    }
    
    
    public void pause(){
        this.isPaused = true;
        this.close();
    }

    public boolean play() throws JavaLayerException{
        return this.play(0);
    }

    public boolean play(int frameIndexStart) throws JavaLayerException {
        return this.play(frameIndexStart, -1, 10);
    }

    /**
     * Plays a range of MPEG audio frames
     * @param start     The first frame to play
     * @param end               The last frame to play
     * @return true if the last frame was played, or false if there are more frames.
     */
    public boolean play(int frameIndexStart, int frameIndexFinal, 
            int correctionFactorInFrames) throws JavaLayerException{
        
        boolean shouldContinueReadingFrames = true;
        
        try {
            this.bitstream = new Bitstream(this.urlToStreamFrom.openStream());
        }
        catch (java.io.IOException ex){
            
        }
        this.audioDevice = FactoryRegistry.systemRegistry().createAudioDevice();
        this.decoder = new Decoder();
        this.audioDevice.open(decoder);
        
        this.isPaused = false;
        this.frameIndexCurrent = 0;

        while (shouldContinueReadingFrames &&
                this.frameIndexCurrent < frameIndexStart - correctionFactorInFrames ) {
            shouldContinueReadingFrames = this.skipFrame();
            this.frameIndexCurrent++;
        }

        if (this.listener != null) {
            this.listener.playbackStarted(new PlaybackEvent(this,
                    PlaybackEvent.EventType.Instances.Started,
                    this.audioDevice.getPosition()
            ));}

        if (frameIndexFinal < 0) {
            frameIndexFinal = frameIndexStart + 2000; //Integer.MAX_VALUE 
        }

        while (shouldContinueReadingFrames && this.frameIndexCurrent < frameIndexFinal){
            if (this.isPaused){
                shouldContinueReadingFrames = false;    
                try { 
                    Thread.sleep(1); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                shouldContinueReadingFrames = this.decodeFrame();
                if (shouldContinueReadingFrames){  // cambiato  Cosi lo faccio avanzare di 1000 frames alla volta, cosi quando finisce la canzone 
                    frameIndexFinal += 1000;       // ho un range di massimo 1000 frames
                }
                this.frameIndexCurrent++;    
            }
        }
        
        // last frame, ensure all data flushed to the audio device.
        if (this.audioDevice != null) {
            this.audioDevice.flush();     //  decommenta

            synchronized (this){
                this.isComplete = (!this.isClosed);
               // this.close();               decommenta se vuoi chiudere il player
            }

            // report to listener
            if (this.listener != null) {
                this.listener.playbackSongFinished(new PlaybackEvent(this,
                        PlaybackEvent.EventType.Instances.Finished,   //"Stopped" before
                        this.audioDevice.getPosition()
                ));
                
            }
        }

        return shouldContinueReadingFrames;
    }

    public boolean resume() throws JavaLayerException{
        return this.play(this.frameIndexCurrent);
    }
    //migliorabile in una sola
    public boolean resume(final int frame) throws JavaLayerException{
        return this.play(frame);
    }
    
/**
 * Cloases this player. Any audio currently playing is stopped
 * immediately.
 */
    public synchronized void close(){ 
        if (this.audioDevice != null){
            this.isClosed = true;

            this.audioDevice.close();

            this.audioDevice = null;

            try {
                this.bitstream.close();
            } catch (BitstreamException ex) {
                
            }
        }
    }
    
    /**
     * Decodes a single frame.
     *
     * @return true if there are no more frames to decode, false otherwise.
     */
    protected boolean decodeFrame() throws JavaLayerException {
        //boolean returnValue = false;

        try {
            if (this.audioDevice != null) {                
                final Header header = this.bitstream.readFrame();
                if (header == null) {
                    return false;
                } else {
                    // sample buffer set when decoder constructed
                    final SampleBuffer output = (SampleBuffer) decoder.decodeFrame(header, bitstream);

                    synchronized (this) {
                        if (this.audioDevice != null) {
                            this.audioDevice.write(output.getBuffer(), 0, output.getBufferLength());
                        }
                    }
                    this.bitstream.closeFrame();
                }
            }
        }
        catch (RuntimeException ex){
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }

    /**
     * skips over a single frame
     * @return false    if there are no more frames to decode, true otherwise.
     */
    protected boolean skipFrame() throws JavaLayerException {
        
        final Header header = bitstream.readFrame();

        if (header == null) {
            return false;
        } else {
            bitstream.closeFrame();
            return true;
        }
    }

    public void stop() {
        this.stopped = true;
        this.listener.playbackFinished(new PlaybackEvent(this,
                PlaybackEvent.EventType.Instances.Stopped,
                this.audioDevice.getPosition()
        ));

        this.close();
    }

    // inner classes

    public static class PlaybackEvent{    
        public JLayerPlayerPausable source;
        public EventType eventType;
        public int frameIndex;

        public PlaybackEvent(final JLayerPlayerPausable source, final EventType eventType, int frameIndex){
            this.source = source;
            this.eventType = eventType;
            this.frameIndex = frameIndex;
        }

        public static class EventType{
            public String name;

            public EventType(String name){
                this.name = name;
            }

            public static class Instances{
                public static EventType Started = new EventType("Started");
                public static EventType Stopped = new EventType("Stopped");
                public static EventType Finished = new EventType("SongFinished");
            }
        }
    }

    public static abstract class PlaybackListener {
        public void playbackStarted(PlaybackEvent event){}
        public void playbackFinished(PlaybackEvent event){}
        public void playbackSongFinished(PlaybackEvent event){}
    }
}
