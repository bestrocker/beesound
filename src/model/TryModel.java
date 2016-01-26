package model;

import Controller.Audio.MpegInfo.Duration;
import javafx.scene.effect.Lighting;


public class TryModel {

    public static void main(String[] args) {
        
        Duration duration = new Duration(50, 30);
        Song song = new Song.Builder()  
                            .title("Trains")
                            .album("In Absentia")
                            .artist("Porcupine Tree")
                            .genre("Progressive Rock")
                            .duration(duration)
                            .bitRate(192)
                            .size(80000)
                            .path(System.getProperty("user.home"))
                            .copyright(true)
                            .channel(0)
                            .version(null)
                            .rate(0)
                            .channelsMode("Stereo")
                            .build();
        
        LibraryManager library = LibraryManager.getInstance();
        library.addSongToLibrary("Trains", "In Absentia", "Porcupine Tree", "Progressive Rock", duration, 192, 80000,
                "ciao", true, 0, null, 0, "Stereo");
        library.addSongToLibrary("Money", "In Absentia @ # ", "Pink FLoyd", "Progressive Rock", duration, 192, 80000,
                "salve", true, 0, null, 0, "Stereo");
        /*library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));
        library.getSongList().forEach(x -> System.out.println(x.getTitle()));        
        library.removeSong("salve");
        library.getSongList().forEach(x -> System.out.println(x.getTitle()));
        library.getAlbumList().forEach(x -> x.getTrackList().forEach(y -> System.out.println(y.getTitle())));
        library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));*/
        
        /*library.addSongInPlaylist("Trains", true);
        library.addSongInPlaylist("Money", true);
        //library.removeSong("Money");
        library.removeSongFromQueue("Money");
        library.getInReproductionTitles().forEach(x -> System.out.println(x));*/
        
        /*System.out.println(library.getLibraryInfo().get("nSongs"));
        System.out.println(library.getLibraryInfo().get("min"));
        System.out.println(library.getLibraryInfo().get("sec"));*/
        
        /*library.newPlaylist("tiziano", "ciao");
        library.newPlaylist("salve", "ciao");
        
        library.removePlaylist("tiziano");
        
        library.getPlaylistList().forEach(x -> System.out.println(x.getName()));*/
        
        //library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));
        
        library.fetchSongs("Money").forEach(x -> System.out.println(x));
        
        
    }
}