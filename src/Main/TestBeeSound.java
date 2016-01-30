package Main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Controller.Audio.MpegInfo.Duration;
import model.LibraryManager;
import model.Manager;

public class TestBeeSound {
    
    Manager model = LibraryManager.getInstance();
    
    @Before    
    public void setUp() {
        Duration duration = new Duration(5, 10);
        Duration duration2 = new Duration(6, 30);
        Duration duration3 = new Duration(4, 6);
        
        // adding four songs to the music library
        model.addSongToLibrary("title", "album", "artist", "genre", duration, 192, 16000, System.getProperty("user.home"), 
                false, 2, "version", 192, "channelsMode");
        model.addSongToLibrary("title2", "album2", "artist2", "genre2", duration2, 192, 16000, System.getProperty("user.home"), 
                false, 2, "version", 192, "channelsMode");
        model.addSongToLibrary("title3", "album3", "artist3", "genre3", duration3, 192, 16000, System.getProperty("user.home"), 
                false, 2, "version", 192, "channelsMode");        
        model.addSongToLibrary("title4", "album", "artist", "genre", duration3, 192, 16000, System.getProperty("user.home"), 
                false, 2, "version", 192, "channelsMode");
    }
    
    @Test
    public void testNumberOfElements() {
        assertEquals(4, model.getSongTitles().size()); 
        assertEquals(3, model.getAlbumTitles().size());
        assertEquals(3, model.getArtistNames().size());        
    }
    
    @Test
    public void testSongTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("title");
        titles.add("title2");
        titles.add("title3");
        titles.add("title4");
        assertTrue(model.getSongTitles().containsAll(titles));
        titles.add("newTitle");
        assertFalse(model.getSongTitles().containsAll(titles));
    }
    
    @Test
    public void testAlbumTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("album");
        titles.add("album2");
        titles.add("album3");
        assertTrue(model.getAlbumTitles().containsAll(titles));
        titles.add("newTitle");
        assertFalse(model.getAlbumTitles().containsAll(titles));
    }
    
    @Test
    public void testReproductionQueue() {
        model.addSongInPlaylist("title", true);
        model.addSongInPlaylist("title2", false);
        model.addSongInPlaylist("title3", true);
        List<String> list = new ArrayList<>();
        list.add("title3");
        list.add("title");
        list.add("title2");
        assertEquals(model.getInReproductionTitles(), list);
    }
    
    @Test
    public void testPlaylists() {        
        model.newPlaylist("testPlaylist", System.getProperty("user.home"));
        model.newPlaylist("otherPlaylist", System.getProperty("user.home"));
        assertEquals(2, model.getPlaylistNames().size());
        model.addSongInPlaylist("title", "testPlaylist");
        model.addSongInPlaylist("title3", "testPlaylist");
        model.addSongInPlaylist("title2", "otherPlaylist");
        model.addSongInPlaylist("title4", "otherPlaylist");
        List<String> list = new ArrayList<>();
        list.add("title");
        list.add("title3");
        assertTrue(model.showPlaylistSong("testPlaylist").containsAll(list));
        List<String> list2 = new ArrayList<>();
        list2.add("title2");
        list2.add("title4");
        assertTrue(model.showPlaylistSong("otherPlaylist").containsAll(list2));       
    }
    
    @Test
    public void testSearch() {        
        assertTrue(model.fetchSongs("title").containsAll(model.getSongTitles()));
        Duration duration = new Duration(5, 10);
        model.addSongToLibrary("newSong", "album", "artist", "genre", duration, 192, 16000, System.getProperty("user.home"), 
                false, 2, "version", 192, "channelsMode");
        assertFalse(model.fetchSongs("title").containsAll(model.getSongTitles()));
        assertTrue(model.fetchSongs("new").contains("newSong"));        
    }    
}
