package gui.panels;

// import common.BookingManager;
import javax.swing.*;
import java.awt.*;



/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */

public class Panel extends JPanel{
    /** The application inventory that this menu operates on. */
    // private BookingManager manager;
    private JPanel top;
    private JPanel bottom;

    // Placeholders
    private JButton placeholder = new JButton("Button1");

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public Panel(JPanel lBottom, JPanel rBottom ){
        this.top = new JPanel();
        top.add(placeholder);
        setBottomPanel(lBottom, rBottom);

    }

    public void setBottomPanel(JPanel lBottom, JPanel rBottom){
        this.bottom = new JPanel(new GridLayout(1, 2, 1, 1));
        bottom.add(lBottom);
        bottom.add(rBottom);
    }

    public JPanel getTopPanel(){ return top; }
    public JPanel getBottomPanel(){ return bottom; }

}
