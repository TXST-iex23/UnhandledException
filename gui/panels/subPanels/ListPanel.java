package gui.panels.subpanels;
import gui.panels.Panel;

/**
 * Add description here */
public abstract class ListPanel extends Panel{

    Panel selected;

    /**
     * builds a list deplay panel
     *
     * @param variable  the variable description
     */
    public ListPanel( ){
        selected = new Panel();
    }

    /**
     * builds a list deplay panel
     *
     * @param variable  the variable description
     */
    public void selectPanel( Panel panel ){
        selected = panel;
    }

}