package gui.panels;

// import common.BookingManager;
import gui.panels.subpanels.dataPanels.*;
import gui.panels.subpanels.*;
import javax.swing.*;
import java.awt.*;


/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class Panel{
    /** The application inventory that this menu operates on. */
    // private BookingManager manager;
    private JPanel top;
    private JPanel bottom;
    private JPanel lBottom;
    private JPanel rBottom;

    // Placeholders
    private JButton placeholder1 = new JButton("Button1");
    private JButton placeholder2 = new JButton("Button2");
    private JButton placeholder3 = new JButton("Button3");

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public Panel( ){
        top = new JPanel();
        top.add(placeholder1);

        bottom = new JPanel(new GridLayout(1, 2, 1, 1));
        lBottom = new JPanel();
        rBottom = new JPanel();

        lBottom.add(placeholder2);
        rBottom.add(placeholder3);

        bottom.add(lBottom);
        bottom.add(rBottom);
    }

    /**
     * Plaseholder
     */
    public JPanel getTop(){ return top; }

        /**
     * Plaseholder
     */
    public JPanel getBottom(){ return bottom; }

}
