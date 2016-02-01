package Controller.Audio;

import Controller.Audio.AudioController.REPRODUCTION_STRATEGY;
import model.Manager;

public interface AudioControllerInterface {

    /**
     * Set pause if in reproduction.
     * Set in reproduction if Paused.
     */
    void togglePause();
    
    /**
     * Skip the song to the given number of bytes.
     * @param nbytes long
     */
    void seekPlayer(final long nbytes);
    
    /**
     * Play the next Track.
     */
    void nextPlayer();
    
    /**
     * Play the previous Track.
     */
    void prevPlayer();
    
    /**
     * Stop the Player.
     */
    void stopPlayer();
    
    /**
     * Play the current song in the player.
     */
    void playPlayer();
    
    /**
     * Set the strategy for reproducing the next tracks.
     * @param strategy {@link REPRODUCTION_STRATEGY}
     */
    void setReproductionStrategy(final REPRODUCTION_STRATEGY strategy);
    
    /**
     * Set the volume to the given 0.1-1 value.
     * @param volume double
     */
    void setVolume(final double volume);
    
    /**
     * Set the reproduction to play the first song.
     * @param b boolean
     */
    void setReproduceNowBoolean(final boolean b);
    
    /**
     * Return the current position of the song.
     * @return int 
     */
    int getPos();
    
    /**
     * Set the current position.
     * @param pos
     * @return
     */
    void setPos(final int pos);
    
    /**
     * Return if the player is paused.
     * @return
     */
    boolean isPaused();
    
    /**
     * Set the model.
     */
    void setModel(final Manager model);
}
