package Controller.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public  class FileController implements SystemManager{
    public static final String sep = System.getProperty("file.separator");
    private static String libraryPath;
    public static String logPath;
    public static String propPath;
    public static String musicDirPath;
    public static String playlistDirPath;
    private  PrintWriter writer;
    
    /**
     * Initialize FileController, Libraries folder, Logger, Properties.
     */
    public FileController() {
        libraryPath = System.getProperty("user.home") + sep + "beesound" + sep;
        initializePaths(libraryPath);
        initialize();
    }
    
    private void initializePaths(final String root) {
        logPath = root + "LOG.txt";
        propPath = root + "properties.txt";
        musicDirPath = root + "Music" + sep;
        playlistDirPath = root + "Playlist" + sep;
    }
    
    /**
     * Contructor with specified LibraryPath.
     * @param newLibraryPath {@link String}
     */
    public FileController(final String newLibraryPath) {
        libraryPath = newLibraryPath + sep + "beesound" + sep;
        initializePaths(libraryPath);
        initialize();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean notExist(final String srcPath) {
        return (Files.notExists(Paths.get(srcPath), LinkOption.NOFOLLOW_LINKS));
    }
    
    private void initialize() {
        
        if (notExist(libraryPath)) {
            new File(libraryPath).mkdirs();
            Log.initializeLogger();
            Log.PROGRAM(libraryPath + " created.");
        }
        Log.initializeLogger();
        Log.PROGRAM("Root Library folder loaded. STARTING PROGRAM");
        if (notExist(musicDirPath)) {
            new File(musicDirPath).mkdirs();
            Log.PROGRAM("Music Directory created");
        }
        if (notExist(playlistDirPath)) {
            new File(playlistDirPath).mkdirs();
            Log.PROGRAM("Playlist Directory created");
        }
        if (notExist(propPath)) {
            createNewFile("", propPath);
        }
    }

    private List<String> listAllSongPath(final File directory, final String filter) {        
        return Arrays.asList(directory.listFiles()).stream()
                .filter(i->i.getAbsolutePath().endsWith(filter))
                .map(i->i.getAbsolutePath())
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listAllPlaylist() {
        return listAllSongPath(new File(playlistDirPath), ".txt");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listAllSongPath() {
        return listAllSongPath(new File(musicDirPath), ".mp3");
    }
    
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    public List<String> getPlaylistSongs(final File playlist) throws IOException {
        return Files.readAllLines(playlist.toPath());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public  String importToLibrary(final String pathSource) {
       final String d = musicDirPath + pathSource.substring(pathSource.lastIndexOf(sep));
           copy(pathSource, d, null, false);
       return d;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String importToLibrary(final InputStream source, final String name) {
        final String d = musicDirPath + name;
            copy(name, d, source, true);
        return d;
      }
    
    private void copy(final String pathSource, final String finalPath, final InputStream stream, final boolean isStreamed) {
        if (Files.notExists(Paths.get(finalPath), LinkOption.NOFOLLOW_LINKS)) {
            try {
                if (!isStreamed) {
                    Files.copy(Paths.get(pathSource),
                            Paths.get(finalPath),
                            StandardCopyOption.REPLACE_EXISTING);
                    Log.INFO("Added to library " + pathSource);
                } else {
                    Files.copy(stream,
                            Paths.get(finalPath),
                            StandardCopyOption.REPLACE_EXISTING);
                    Log.INFO("Added to library " + pathSource);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.ERROR("Can't importToLibrary, error during copy " + e);
            }
        }
    }
    
    
    
    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    public void createNewFile(final String name, final String dstPath) {
        try {
            new File(dstPath + name + ".txt").createNewFile();
        } catch (IOException e) {
            Log.ERROR("Can't createNewFile()" + e);
            e.printStackTrace();
            return;
        }
        Log.PROGRAM("New file " + name + " created : " + dstPath + name + ".txt");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void appendToFile(final String msg, final String path) {
        try {
            this.writer = new PrintWriter(new FileWriter(new File(path), true), true);
            this.writer.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
            Log.ERROR("error in appendToFile " + e);
        }
    }

    /**
     * {@inheritDoc}
     * @param pathFile
     */
    @Override
    public void delete(final String pathFile) {
        try {
            Files.deleteIfExists(Paths.get(pathFile));
            Log.PROGRAM(pathFile + " Deleted.");
        } catch (IOException e) {
            Log.ERROR("Can't remove " + pathFile + e);
            e.printStackTrace();
        }
    }
}
