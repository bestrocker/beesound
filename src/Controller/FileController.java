package Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class FileController implements SystemManager{

    public FileController() {
        
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
