package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class test {

    public static void main(String[] args) {
        
        String dir = "/home/bestrocker221/Dropbox/Musica/";
        File mydir = new File(dir);
        System.out.println(mydir.isDirectory());
        System.out.println("list()"+ Arrays.deepToString(mydir.list()) + 
                "\nlistFiles"+Arrays.deepToString(mydir.listFiles()));
        File[] myfiles = new File[(int) mydir.length()];
        String song = "James Bay - Hold Back The River.mp3";
        
        
    }

}
