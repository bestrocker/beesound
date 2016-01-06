package Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import Controller.Files.FileController;
import Controller.Files.Log;

public class test {

    public static void main(String[] args) throws IOException {
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
        
        FileController fc = new FileController();
        try {
            fc.importToLibrary("/home/bestrocker221/Salmo-1984.mp3", "/home/bestrocker221/Arduino/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        System.out.println(System.getProperty("user.home"));
        FileController fc = new FileController();
        Log.INFO("program started");
        Log.INFO("program closed");
        */
        
            System.out.println(
                    Arrays.asList(new File("/home/bestrocker221/Dropbox/Musica/").listFiles())
                    .stream()
                    .filter(i->i.getAbsolutePath().endsWith("mp3"))
                    .map(i->i.getAbsolutePath())
                    .collect(Collectors.toList()));
        
//        Log c = new Log("/home/bestrocker221/tette.txt");
        /*
        Log.INFO("ciao");
        Log.INFO("ciao");
        Log.PROGRAM("avviato");
        Log.INFO("ciao");
        Log.ERROR("ERRORE");
        Log.INFO("ciao");
        Log.PROGRAM("chiuso");
        Log.getLogLines().get().stream().forEach(System.out::println);
        */
    }

}
