package gui.panels;

import common.BookingManager;
import javax.swing.*;


/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class TopPanel extends JPanel{
    /** The application inventory that this menu operates on. */
    // private BookingManager manager;
    private JPanel top;
    private JButton placeholder1 = new JButton("Button1");

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public TopPanel( ){
        top = new JPanel();
        top.add(placeholder1);
    }

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public TopPanel(BookingManager manager){
        // this.manager = manager;
        top = new JPanel();
        top.add(placeholder1);
    }

    /**
     * Plaseholder
     */
    public JPanel getTopPanel(){ return top; }

}
