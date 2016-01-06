package Controller.Files;

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
import java.util.stream.Collectors;

public  class FileController implements SystemManager{

    public static final String libraryPath = System.getProperty("user.home")+"/beesound/";
    public static final  String logPath = libraryPath+"LOG.txt";
    private  final String propPath = libraryPath+"properties.txt";
    private  final String musicDirPath = libraryPath + "Music/";
    private  final String playlistDirPath = libraryPath + "Playlist/";
    
    private  PrintWriter propertiesWrite;
    private  final File properties;
   // private  final File musicDir;
 //   private  final File playlistDir;
    
    /**
     * Initialize FileController, Libraries folder, Logger, Properties.
     */
    public FileController() {
        
        this.properties = new File(propPath);
       // this.musicDir = new File(musicDirPath);
       // this.playlistDir = new File(playlistDirPath);
        initialize();
        
        
    }

    private void initialize() {
        if (Files.notExists(Paths.get(libraryPath), LinkOption.NOFOLLOW_LINKS)){
            new File(libraryPath).mkdirs();
        }
        Log.initializeLogger();
        Log.PROGRAM("Root Library folder loaded. STARTING PROGRAM");
        if (Files.notExists(Paths.get(this.musicDirPath), LinkOption.NOFOLLOW_LINKS)){
            new File(musicDirPath).mkdirs();
            Log.PROGRAM("Music Directory created");
        }
        if (Files.notExists(Paths.get(this.playlistDirPath), LinkOption.NOFOLLOW_LINKS)){
            new File(this.playlistDirPath).mkdirs();
            Log.PROGRAM("Playlist Directory created");
        }
        if (Files.notExists(Paths.get(this.propPath), LinkOption.NOFOLLOW_LINKS)){
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<File> listAllFilesInDir(final File directory) {
        return Arrays.asList(directory.listFiles());
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listAllSongPath(final File directory) {        
        return Arrays.asList(directory.listFiles()).stream()
                .filter(i->i.getAbsolutePath().endsWith("mp3"))
                .map(i->i.getAbsolutePath())
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listAllSongPath(){
        return listAllSongPath(new File(this.musicDirPath));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPlaylistSongs(final File playlist) throws IOException{
        return Files.readAllLines(playlist.toPath());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public  void importToLibrary(final String pathSource) throws IOException {
       final String d = this.musicDirPath + pathSource.substring(pathSource.lastIndexOf("/"));
       if(Files.notExists(Paths.get(d), LinkOption.NOFOLLOW_LINKS)){
           Files.copy(Paths.get(pathSource),
                   Paths.get(d),
                   StandardCopyOption.COPY_ATTRIBUTES);
           Log.INFO(pathSource +" added to library");
       }
    }
    
}
