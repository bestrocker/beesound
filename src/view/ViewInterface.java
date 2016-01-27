package view;

public interface ViewInterface {
	
    /**
     * Sets observer for the GUI
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
     * Set the current song as highlighted in the reproduction list
     * @param index
     */
    public void setHighlighted(int index);
}
