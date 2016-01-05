package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class FileController implements SystemManager{

    public static final String libraryPath = System.getProperty("user.dir")+"/beesound/";
    private BufferedReader propertiesRead;
    private BufferedWriter propertiesWrite;
    public File properties;
    
    public FileController() {
        
        if (Files.notExists(Paths.get(libraryPath), LinkOption.NOFOLLOW_LINKS)){
            new File(libraryPath).mkdirs();
            System.out.println("Library directory created");
        }
        this.properties = new File(libraryPath+"properties.txt");
        if (Files.notExists(this.properties.toPath(), LinkOption.NOFOLLOW_LINKS)){
            try {
                this.properties.createNewFile();
                System.out.println("properties file created");
            } catch (IOException e) {
                System.err.println("cannot create properties File\n");
                e.printStackTrace();
            }
        }
        try {
            this.propertiesRead = new BufferedReader(new FileReader(this.properties));
            this.propertiesWrite = new BufferedWriter(new FileWriter(this.properties));
        } catch (IOException e) {
            System.out.println("Buffer setted");
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
