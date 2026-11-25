package gui.panels.subpanels;
import gui.panels.Panel;

/**
 * Add description here */
public abstract class DataPanel extends Panel{

    Panel selected;

    /**
     * builds a list deplay panel
     *
     * @param variable  the variable description
     */
    public DataPanel( ){
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
