package Controller.Audio;
/*
 * TagInfo.
 * 
 * JavaZOOM : jlgui@javazoom.net
 *            http://www.javazoom.net
 * 
 *-----------------------------------------------------------------------
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import javax.sound.sampled.UnsupportedAudioFileException;
import Controller.Audio.MpegInfo.Duration;

/**
 * This interface define needed features for song information.
 * Adapted from Scott Pennell interface.
 */
public interface TagInfo {
    
    void load(InputStream input) throws IOException, UnsupportedAudioFileException;

    void load(URL input) throws IOException, UnsupportedAudioFileException;

    boolean load(File input) throws IOException, UnsupportedAudioFileException;

    /**
     * Get Sampling Rate.
     *
     * @return sampling rate
     */
    int getSamplingRate();

    /**
     * Get Nominal Bitrate
     *
     * @return bitrate in bps
     */
    int getBitRate();


    /**
     * Get play time in seconds.
     *
     * @return play time in seconds
     */
    long getPlayTime();

    /**
     * Get the title of the song.
     *
     * @return the title of the song
     */
    Optional<String> getTitle();

    /**
     * Get the artist that performed the song.
     *
     * @return the artist that performed the song
     */
    Optional<String> getArtist();

    /**
     * Get the name of the album upon which the song resides.
     *
     * @return the album name
     */
     Optional<String> getAlbum();

    /**
     * Get the genre string of the music.
     *
     * @return the genre string
     */
    Optional<String> getGenre();

    /**
     * Get the year the track was released.
     *
     * @return the year the track was released
     */
    Optional<String> getYear();
    
    /**
     * Get the song's duration.
     * 
     * @return a Dimension including minutes and seconds. Width = minutes, Height = seconds;
     */
     Duration getDurationInMinutes();
}