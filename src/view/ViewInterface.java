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
}
