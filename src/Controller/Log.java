package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Date;

public class Log {

    private static PrintWriter logWriter;
    
    public Log(String logfile) {
        try {
            System.out.println(logfile);
            if (Files.notExists(Paths.get(logfile), LinkOption.NOFOLLOW_LINKS)){
                new File(logfile).createNewFile();
                Log.PROGRAM("log file creato");
            }
            Log.logWriter = new PrintWriter(new FileOutputStream(new File(logfile), true),true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static void INFO(final String msg) {
            Log.logWriter.println( "INFO: "+new Date() + " " + msg);
    }
    public static void PROGRAM(final String msg) {
        Log.logWriter.println("PROGRAM: "+ new Date() + " " + msg);
    }
    public static void ERROR(final String msg) {
        Log.logWriter.println("ERROR: "+ new Date() + " " + msg);
    }

}
