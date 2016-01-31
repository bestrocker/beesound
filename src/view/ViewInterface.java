package view;

import view.GUI.PROGRESS_BAR;

/**
 * Interface for Controller-View Communication.
 *
 */
public interface ViewInterface {

    /**
     * Sets observer for the GUI. 
     * @param observer 
     */
    void setObserver(final ViewObserver observer);

    /**
     * Method called by Controller to notify the view to be refreshed.
     */
    void refreshView();

    /**
     * Set the visibility of the view. 
     * @param visible 
     */
    void setVisible(final boolean visible);

    /**
     * Tell the View to refresh and reinitialize the progress bar. 
     * @param val 
     */
    void updateProgressBar(PROGRESS_BAR val);
}
