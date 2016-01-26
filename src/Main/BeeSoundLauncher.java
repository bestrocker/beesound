package Main;

import Controller.Controller;
import model.LibraryManager;
import view.GUI;

public class BeeSoundLauncher {

    public static void main(String[] args) {
       
        /*
         * Initialize the player.
         */
        new Controller(new GUI(),LibraryManager.getInstance());
        
    }

}
