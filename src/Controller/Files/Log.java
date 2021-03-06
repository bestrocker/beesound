package Controller.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;  

/**
 * Log Class for logging information.
 * @author bestrocker221
 *
 */
public final class Log {

    private static PrintWriter logWriter;
        
    private Log(final String logfile) {
        
        try {
            Log.logWriter = new PrintWriter(new FileWriter(logfile, true), true);
            if (Files.notExists(Paths.get(logfile), LinkOption.NOFOLLOW_LINKS)) {
                new File(logfile).createNewFile();
                Log.PROGRAM("log file creato");
            }
            
        } catch (IOException e) {
            System.out.println("Impossibile creare log file");
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize the Logger static class if not initialized yet.
     */
    public static void initializeLogger() {
            new Log(FileController.logPath);
    }
    
    /**
     * Get the content of log file.
     * @return a List containing all lines written in Log file
     */
    public static List<String> getLogLines() {
        List<String> l = null;
        try {
            l =  Files.readAllLines(Paths.get(FileController.logPath));
        } catch (IOException e) {
            Log.ERROR("Can't parse log into List<String>. getLogLines Failed.");
            e.printStackTrace();
        }
        return (l == null) ? new ArrayList<String>() : l;
    }
    
    /**
     * Write msg in log file.
     * Priority "INFO"
     * @param msg
     */
    public static void INFO(final String msg) {
        Log.logWriter.println("INFO:\t" + new Date() + " " + msg);
    }
    
    /**
     * Write a msg in log file.
     * Priority "PROGRAM"
     * @param msg
     */
    public static void PROGRAM(final String msg) {
        Log.logWriter.println("PROGRAM:\t" + new Date() + " " + msg);
    }
    
    /**
     * Write a msg in log file.
     * Priority "ERROR"
     * @param msg
     */
    public static void ERROR(final String msg) {
        Log.logWriter.println("ERROR:\t" + new Date() + " " + msg.toUpperCase());
    }

}
