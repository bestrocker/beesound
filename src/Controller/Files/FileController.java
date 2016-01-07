package Controller.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public  class FileController implements SystemManager{

    public static final String libraryPath = System.getProperty("user.home")+"/beesound/";
    public static final  String logPath = libraryPath+"LOG.txt";
    public static final String propPath = libraryPath+"properties.txt";
    public static final String musicDirPath = libraryPath + "Music/";
    public static final String playlistDirPath = libraryPath + "Playlist/";
    
  //  private  PrintWriter propertiesWrite;
    
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
        if (notExist(FileController.musicDirPath)){
            new File(musicDirPath).mkdirs();
            Log.PROGRAM("Music Directory created");
        }
        if (notExist(FileController.playlistDirPath)){
            new File(FileController.playlistDirPath).mkdirs();
            Log.PROGRAM("Playlist Directory created");
        }
        if (notExist(FileController.propPath)){
            createNewFile("" , FileController.propPath);
        }
        /*try {
            //this.propertiesRead = new BufferedReader(new FileReader(this.properties));
            this.propertiesWrite = new PrintWriter(new FileWriter(this.properties));
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
        return listAllSongPath(new File(FileController.musicDirPath));
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
       final String d = FileController.musicDirPath + pathSource.substring(pathSource.lastIndexOf("/"));
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
    
}
