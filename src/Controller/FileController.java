package Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class FileController implements SystemManager{

    public static final String libraryPath = System.getProperty("user.home")+"/beesound/";
    public static final  String logPath = libraryPath+"LOG.txt";
    private  final String propPath = libraryPath+"properties.txt";
    private  final String musicDirPath = libraryPath + "Music/";
    private  PrintWriter propertiesWrite;
    private  final File properties;
    private  final File musicDir;
    
    public FileController() {
        
        this.properties = new File(propPath);
        this.musicDir = new File(musicDirPath);
        initializeLibFolder();
        initialize();
        
        
    }
    
    private void initializeLibFolder(){
        if (Files.notExists(Paths.get(libraryPath), LinkOption.NOFOLLOW_LINKS)){
            new File(libraryPath).mkdirs();
        }
        Log.initializeLogger();
        Log.PROGRAM("root library folder loaded");
    }
    
    private void initialize() {
        
        if (Files.notExists(this.musicDir.toPath(), LinkOption.NOFOLLOW_LINKS)){
            new File(musicDirPath).mkdirs();
            Log.PROGRAM("Music Directory created");
        }
        if (Files.notExists(this.properties.toPath(), LinkOption.NOFOLLOW_LINKS)){
            try {
                this.properties.createNewFile();
                Log.PROGRAM("properties file created");
            } catch (IOException e) {
                Log.ERROR("Cannot create properties File\n");
                e.printStackTrace();
            }
        }
        try {
            //this.propertiesRead = new BufferedReader(new FileReader(this.properties));
            this.propertiesWrite = new PrintWriter(new FileWriter(this.properties));
        } catch (IOException e) {
            Log.ERROR("Buffer and logger not set, error in FileController constructor");
            e.printStackTrace();
        }
        
        
    }
    
    public List<File> listAllFilesInDir(final File directory) {
        return Arrays.asList(directory.listFiles());
        
    }

    @Override
    public List<String> listAllSongPath(final File directory) {
        List<String> list = new ArrayList<>();
        Arrays.asList(directory.listFiles()).stream()
                                   .filter(i->i.getAbsolutePath().endsWith("mp3"))
                                   .forEach(i->list.add(i.getAbsolutePath()));
        Paths.get("/home/");
        return list;
    }

    /*
     * NOTA BENE: libraryPath DEVE includere il "/" finale   (non-Javadoc)
     * @see Controller.SystemManager#importToLibrary(java.io.File, java.lang.String)
     */
    public  void importToLibrary(final String pathSource, final String libraryPath) throws IOException {
        Files.copy(Paths.get(pathSource),
                   Paths.get(libraryPath+(pathSource.substring((pathSource.lastIndexOf("/"))+1))),
                   StandardCopyOption.COPY_ATTRIBUTES);
        
    }
    
}
