package model;

import java.nio.file.FileSystems;
import Controller.Audio.MpegInfo.Duration;
import java.nio.file.Path;

public class TryModel {

    public static void main(String[] args) {
        
        LibraryManager library = LibraryManager.getInstance();
        Duration duration = new Duration(6, 30);
        String path = System.getProperty("user.home");
        String album = "The Dark Side Of The Moon";
        String genre = "Prog Rock";
        String artist = "Pink Floyd";
        Song song = new Song("Money", "The Dark Side Of The Moon", artist, genre, duration, 192, 8000, path);
        Song song2 = new Song("Eclipse", "The Dark Side Of The Moon", artist, genre, duration, 320, 100000, path);
        library.addSongToLibrary(song);
        library.addSongToLibrary(song2);
        /*library.getSongList().forEach(x -> System.out.println(x.getTitle()));
        library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));
        library.getArtistList().forEach(x -> System.out.println(x.getName()));
        library.getGenreList().forEach(x -> System.out.println(x.getName()));*/
    
        /*System.out.println("Genres:");
        library.getGenreList().forEach(x -> x.getAlbumList().forEach(y -> System.out.println(y.getTitle())));
        library.getGenreList().forEach(x -> x.getArtistList().forEach(y -> System.out.println(y.getName())));*/
        Playlist playlist = new Playlist("prova");
        playlist.addSong(song);
        Song song3 = new Song("Trains", "In Absentia", "Porcupine Tree", "Prog Rock", duration, 200, 15000, path);
        library.addSongToLibrary(song3);
        playlist.addSong(song3);
        /*
        library.getSongList().forEach(x -> System.out.println(x.getTitle()));
        library.getAlbumList().forEach(x -> System.out.println(x.getTitle()));
        System.out.println("Genres:");
        library.getGenreList().forEach(x -> x.getArtistList().forEach(y -> System.out.println(y.getName())));
        library.getGenreList().forEach(x -> x.getAlbumList().forEach(y -> System.out.println(y.getTitle())));*/
        //playlist.getTrackList().forEach(x -> System.out.println(x.getTitle()));
        
    }
}