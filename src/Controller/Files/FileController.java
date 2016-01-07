package Controller.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public  class FileController implements SystemManager{
    public static final String sep = System.getProperty("file.separator");
    public static final String libraryPath = System.getProperty("user.home")+sep+"beesound"+sep;
    public static final  String logPath = libraryPath+"LOG.txt";
    public static final String propPath = libraryPath+"properties.txt";
    public static final String musicDirPath = libraryPath + "Music"+sep;
    public static final String playlistDirPath = libraryPath + "Playlist"+sep;
    
    private  PrintWriter writer;
    
    /**
     * Initialize FileController, Libraries folder, Logger, Properties.
     */
    public FileController() {
        
    //    this.properties = new File(propPath);
       // this.musicDir = new File(musicDirPath);
       // this.playlistDir = new File(playlistDirPath);
        initialize();
        
        
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean notExist(final String srcPath){
        return (Files.notExists(Paths.get(srcPath), LinkOption.NOFOLLOW_LINKS));
    }
    
    private void initialize() {
        if (notExist(libraryPath)){
            new File(libraryPath).mkdirs();
        }
        Log.initializeLogger();
        Log.PROGRAM("Root Library folder loaded. STARTING PROGRAM");
        if (notExist(musicDirPath)){
            new File(musicDirPath).mkdirs();
            Log.PROGRAM("Music Directory created");
        }
        if (notExist(playlistDirPath)){
            new File(playlistDirPath).mkdirs();
            Log.PROGRAM("Playlist Directory created");
        }
        if (notExist(propPath)){
            createNewFile("" ,propPath);
        }
        /*
        try {
            //this.propertiesRead = new BufferedReader(new FileReader(this.properties));
            
        } catch (IOException e) {
            Log.ERROR("Buffer and logger not set, error in FileController constructor");
            e.printStackTrace();
        }*/
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
        return listAllSongPath(new File(musicDirPath));
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
       final String d = musicDirPath + pathSource.substring(pathSource.lastIndexOf(sep));
       if(Files.notExists(Paths.get(d), LinkOption.NOFOLLOW_LINKS)){
           Files.copy(Paths.get(pathSource),
                   Paths.get(d),
                   StandardCopyOption.COPY_ATTRIBUTES);
           Log.INFO(pathSource +" added to library");
       }
    }
    
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    public void createNewFile(final String name,final String dstPath) {
        try {
            new File(dstPath+name+".txt").createNewFile();
        } catch (IOException e) {
            Log.ERROR("Can't createNewFile()");
            e.printStackTrace();
            return;
        }
        Log.PROGRAM("New file "+name+" created : "+name+dstPath);
    }
    
    public void appendToFile(final String msg, final String path){
        try {
            this.writer = new PrintWriter(new FileWriter(new File(path),true),true);
            this.writer.println(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
         
    }
    
}
