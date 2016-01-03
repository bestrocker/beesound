package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) throws InterruptedException {
        
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
//        Thread.sleep(500);
//        System.out.println("seeking");
        
        //audio.seek(1000);
    }

}
