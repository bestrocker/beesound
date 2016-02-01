package Controller.Audio;
/*
 * MpegInfo.
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

import org.tritonus.share.sampled.file.TAudioFileFormat;
import Controller.Files.Log;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * This class gives information (audio format and comments) about MPEG file or URL.
 */
public final class MpegInfo implements TagInfo {
    
    private static final MpegInfo SINGLEINSTANCE = new MpegInfo();
    
    private int channels = -1;
    private String channelsMode = null;
    private String version = null;
    private int rate = 0;
    private String layer = null;
    private String emphasis = null;
    private int nominalbitrate = 0;
    private long total = 0;
    private String location = null;
    private long size = 0;
    private boolean copyright = false;
    private boolean crc = false;
    private boolean original = false;
    private boolean vbr = false;
    private int track = -1;
    private String year = null;
    private String genre = null;
    private String title = null;
    private String artist = null;
    private String album = null;
    private Duration durationInMinutes;
    
    /**
     * Return an instance of the class.
     * @return {@link MpegInfo}
     */
    public static MpegInfo getInstance() {
        return MpegInfo.SINGLEINSTANCE;
    }
    
    /**
     * Constructor.
     */
    private MpegInfo() {
        super();
    }

    /**
     * Load and parse MPEG info from File.
     *
     * @param input File
     * @throws IOException
     * @return boolean b
     */
    public boolean load(final File input) {
        this.size = input.length();
        this.location = input.getPath();
        try {
            loadInfo(input);
        } catch (IOException | UnsupportedAudioFileException e) {
            Log.ERROR("Can't load MPEG TAG of " + input.getPath());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Load and parse MPEG info from URL.
     *
     * @param input URL
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public void load(final URL input) throws IOException, UnsupportedAudioFileException {
        this.location = input.toString();
        loadInfo(input);
    }

    /**
     * Load and parse MPEG info from InputStream.
     *
     * @param input {@link INPUT_STREAM}
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public void load(final InputStream input) throws IOException, UnsupportedAudioFileException {
        loadInfo(input);
    }

    /**
     * Load info from input stream.
     *
     * @param input
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    private void loadInfo(final InputStream input) throws IOException, UnsupportedAudioFileException {
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(input);
        loadInfo(aff);
    }

    /**
     * Load MP3 info from file.
     *
     * @param file
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    private void loadInfo(final File file) throws IOException, UnsupportedAudioFileException {
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
        loadInfo(aff);
    }

    /**
     * Load info from AudioFileFormat.
     *
     * @param aff
     */
    @SuppressWarnings("unchecked")
    private void loadInfo(final AudioFileFormat aff) throws UnsupportedAudioFileException {
        final String type = aff.getType().toString();
        if (!type.equalsIgnoreCase("mp3")) {
            throw new UnsupportedAudioFileException("Not MP3 audio format");
        }
        if (aff instanceof TAudioFileFormat) {
            final Map<String, Object> props = ((TAudioFileFormat) aff).properties();
            if (props.containsKey("mp3.channels")){
                channels = ((Integer) props.get("mp3.channels")).intValue();
            }
            if (props.containsKey("mp3.frequency.hz")){
                rate = ((Integer) props.get("mp3.frequency.hz")).intValue();
            }
            if (props.containsKey("mp3.bitrate.nominal.bps")){
                nominalbitrate = ((Integer) props.get("mp3.bitrate.nominal.bps")).intValue();
            }
            if (props.containsKey("mp3.version.layer")){
                layer = "Layer " + props.get("mp3.version.layer");
            }
            if (props.containsKey("mp3.version.mpeg")) {
                version = (String) props.get("mp3.version.mpeg");
                if (version.equals("1")){
                    version = "MPEG1";
                }
                else if (version.equals("2")){
                    version = "MPEG2-LSF";
                }
                else if (version.equals("2.5")){
                    version = "MPEG2.5-LSF";
                }
            }
            if (props.containsKey("mp3.mode")) {
                final int mode = ((Integer) props.get("mp3.mode")).intValue();
                if (mode == 0){
                    channelsMode = "Stereo";
                }
                else if (mode == 1){
                    channelsMode = "Joint Stereo";
                }
                else if (mode == 2){
                    channelsMode = "Dual Channel";
                }
                else if (mode == 3){
                    channelsMode = "Single Channel";
                }
            }
            if (props.containsKey("mp3.crc")){
                crc = ((Boolean) props.get("mp3.crc")).booleanValue();
            }
            if (props.containsKey("mp3.vbr")){
                vbr = ((Boolean) props.get("mp3.vbr")).booleanValue();
            }
            if (props.containsKey("mp3.copyright")){
                copyright = ((Boolean) props.get("mp3.copyright")).booleanValue();
            }
            if (props.containsKey("mp3.original")){
                original = ((Boolean) props.get("mp3.original")).booleanValue();
            }
            emphasis = "none";
            if (props.containsKey("title")){
                title = (String) props.get("title");
            }
            if (props.containsKey("author")){
                artist = (String) props.get("author");
            }
            if (props.containsKey("album")){
                album = (String) props.get("album");
            }
            if (props.containsKey("date")){
                year = (String) props.get("date");
            }
            if (props.containsKey("duration")){
                total = (long) Math.round((((Long) props.get("duration")).longValue()) / 1000000);
            }
            this.durationInMinutes = new Duration((int)(this.total/60), (int)(this.total % 60));
            if (props.containsKey("mp3.id3tag.genre")){
                genre = (String) props.get("mp3.id3tag.genre");
            }
            if (props.containsKey("mp3.id3tag.track")) {
                try {
                    track = Integer.parseInt((String) props.get("mp3.id3tag.track"));
                }
                catch (NumberFormatException e1) {
                    // Not a number
                }
            }
        }
    }

    /**
     * Load MP3 info from URL.
     *
     * @param input
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    private void loadInfo(final URL input) throws IOException, UnsupportedAudioFileException {
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(input);
        loadInfo(aff);
        loadShoutastInfo(aff);
    }

    /**
     * Load Shoutcast info from AudioFileFormat.
     *
     * @param aff
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    @SuppressWarnings("unchecked") // cause it depends on the library (map not parametrized)
    private void loadShoutastInfo(final AudioFileFormat aff) throws IOException, UnsupportedAudioFileException {
        final String type = aff.getType().toString();
        if (!type.equalsIgnoreCase("mp3")){
            throw new UnsupportedAudioFileException("Not MP3 audio format");
        }
        if (aff instanceof TAudioFileFormat) {
            final Map<String,Object> props = ((TAudioFileFormat) aff).properties();
            // Try shoutcast meta data (if any).
            final Iterator<String> it = props.keySet().iterator();
            String key;
            while (it.hasNext()) {
                key = it.next();
                if (key.startsWith("mp3.shoutcast.metadata.")) {
                    String value =  (String) props.get(key);
                    key = key.substring(23, key.length());
                    if (key.equalsIgnoreCase("icy-name")) {
                        title = value;
                    } else if (key.equalsIgnoreCase("icy-genre")) {
                        genre = value;
                    } 
                }
            }
        }
    }

    public boolean getVBR() {
        return vbr;
    }

    public int getChannels() {
        return channels;
    }

    public String getVersion() {
        return version;
    }

    public String getEmphasis() {
        return emphasis;
    }

    public boolean getCopyright() {
        return copyright;
    }

    public boolean getCRC() {
        return crc;
    }

    public boolean getOriginal() {
        return original;
    }

    public String getLayer() {
        return layer;
    }

    public long getSize() {
        return size;
    }

    public String getLocation() {
        return location;
    }

    /*-- TagInfo Implementation --*/
    /**
     * {@inheritDoc}
     */
    @Override
    public int getSamplingRate() {
        return rate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getBitRate() {
        return nominalbitrate;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getPlayTime() {
        return total;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }
    /**
     * Set Title of the song.
     * @param title String
     */
    public void setTitle(final String title) {
        this.title = title;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getArtist() {
        return Optional.ofNullable(artist);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getAlbum() {
        return Optional.ofNullable(album);
    }
    
    public int getTrack() {
        return track;
    }
    
    /**
     * {@inheritDoc}
     */
    public Optional<String> getGenre() {
        return Optional.ofNullable(genre);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getYear() {
        if(year == null) return Optional.empty();
        return Optional.ofNullable(year);
    }

    /**
     * Get channels mode.
     *
     * @return channels mode
     */
    public String getChannelsMode() {
        return channelsMode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Duration getDurationInMinutes() {
        return durationInMinutes;
        
    }
    
    /**
     * Class for song's duration.
     * @author bestrocker221
     *
     */
    public static class Duration{
        private final int min;
        private final int sec;
        public Duration(final int min, final int sec) {
            this.min = min;
            this.sec = sec;
        }
        public int getMin(){
            return this.min;
        }
        public int getSec(){
            return this.sec;
        }
    }
}