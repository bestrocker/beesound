package model;

import java.nio.file.FileSystems;
import Controller.Audio.MpegInfo.Duration;
import java.nio.file.Path;

public class TryModel {

    public static void main(String[] args) {
        
        LibraryManager library = LibraryManager.getInstance();
        Duration duration = new Duration(6, 30);
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"));
        Song song = new Song("Money", "The Dark Side Of The Moon", "Pink Floyd", "Prog Rock", duration, 192, 8000, path);
        library.addSongToLibrary(song);
        System.out.println(library.getSongList());
        System.out.println(library.getAlbumList());
        System.out.println(library.getArtistList());
        System.out.println(library.getGenreList());        
    }
}