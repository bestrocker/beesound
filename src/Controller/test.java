package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {
        /*
        String dir = "/home/bestrocker221/Dropbox/Musica/";
        File mydir = new File(dir);
        
        List<String> list = new ArrayList<>();
        Arrays.asList(mydir.listFiles()).stream()
                                   .filter(i->i.getAbsolutePath().endsWith("mp3"))
                                   .forEach(i->list.add(i.getAbsolutePath()));
        list.forEach(System.out::println);
        AudioController audio = new AudioController();
        audio.setPlaylist(list);
        audio.play();
        */
        FileController fc = new FileController();
        try {
            fc.importToLibrary("/home/bestrocker221/Salmo-1984.mp3", "/home/bestrocker221/Arduino/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
