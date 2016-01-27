package Controller.Audio;

import Controller.Audio.AudioController.REPRODUCTION_STRATEGY;

public interface AudioControllerInterface {

    /**
     * Set pause if in reproduction.
     * Set in reproduction if Paused.
     */
    public void togglePause();
    
    /**
     * Skip the song to the given number of bytes.
     * @param nbytes
     */
    public void seekPlayer(final long nbytes);
    
    /**
     * Play the next Track
     */
    public void nextPlayer();
    
    /**
     * Play the previous Track.
     */
    public void prevPlayer();
    
    /**
     * Stop the Player.
     */
    public void stopPlayer();
    
    /**
     * Play the current song in the player.
     */
    public void playPlayer();
    
    /**
     * Set the strategy for reproducing the next tracks.
     * @param strategy
     */
    public void setReproductionStrategy(final REPRODUCTION_STRATEGY strategy);
    
    /**
     * Set the volume to the given 0.1-1 value
     * @param volume
     */
    public void setVolume(final double volume);
    
    /**
     * Set the reproduction to play the first song.
     * @param b
     */
    public void setReproduceNowBoolean(final boolean b);
}
