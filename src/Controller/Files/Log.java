package Controller.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Log {

    private static PrintWriter logWriter;
    //private static Log SINGLELOG = null;
    
    private Log(final String logfile) {
        
        try {
            Log.logWriter = new PrintWriter(new FileWriter(logfile, true), true);
            System.out.println(logfile);
            if (Files.notExists(Paths.get(logfile), LinkOption.NOFOLLOW_LINKS)){
                new File(logfile).createNewFile();
                Log.PROGRAM("log file creato");
            }
            
        } catch (IOException e){
            System.out.println("Impossibile creare log file");
            e.printStackTrace();
        }
    }
    
    public static void initializeLogger(){
       new Log(FileController.logPath);
    }
    
    public static Optional<List<String>> getLogLines(){
        List<String> l = null;
        try {
            l =  Files.readAllLines(Paths.get(FileController.logPath));
        } catch (IOException e) {
            Log.ERROR("Can't parse log into List<String>. getLogLines Failed.");
            e.printStackTrace();
        }
        return Optional.ofNullable(l);
    }
    
    public static void INFO(final String msg) {
        Log.logWriter.println( "INFO: "+new Date() + " " + msg);
    }
    public static void PROGRAM(final String msg) {
        Log.logWriter.println("PROGRAM: "+ new Date() + " " + msg);
    }
    public static void ERROR(final String msg) {
        Log.logWriter.println("ERROR: "+ new Date() + " " + msg.toUpperCase());
    }

}
