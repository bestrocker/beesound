package Main;

import Controller.Controller;
import model.LibraryManager;
import model.Manager;
import view.GUI;
import view.ViewInterface;

public class BeeSoundLauncher {

    public static void main(String[] args) {
       
        /*
         * Initialize the player.
         */
        final ViewInterface view = new GUI();
        final Manager model = LibraryManager.getInstance();
        new Controller(view,model);
        
    }

}
