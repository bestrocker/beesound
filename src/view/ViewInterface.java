package view;

public interface ViewInterface {
	
	/**
	 * Sets observer for the GUI
	 * @param observer
	 */
    void setObserver(final ViewObserver observer);
    
    /**
     * Returns the index of the currently selected item
     * @return
     */
    int getSelectedIndex();
    
    /**
     * Method called by Controller to notify the view to be refreshed.
     */
    void refreshView();
    
    /**
     * Set the visibility of the view.
     */
    void setVisible(final boolean visible);
}
