package gui;

import common.BookingManager;
import gui.panels.Panel;
import javax.swing.*;
import java.awt.*;

/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class GraphicUI{
    /** CONSTANTS*/
    private final static int HEIGHT = 300;
    private final static int WIDTH = HEIGHT * 2;
    private final static int BANNER = 100;

    /** The application inventory that this menu operates on. */
    //private BookingManager manager;
    private JFrame main;
    private JPanel top;
    private JPanel bottom;

    /**
     * Initiates the graphical window of the application    */
    public GraphicUI( ){
        //this.manager = new BookingManager();
        buildFrame();
    }

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public GraphicUI(BookingManager manager){
        //this.manager = manager;
        buildFrame();
    }



//-----------------------------------------------------------------

    private void buildFrame(){
        main = new JFrame();
        main.setLayout(new BorderLayout());
        top = new Panel().getTop();
        bottom = new Panel().getBottom();

        top.setPreferredSize(new Dimension(WIDTH, BANNER));

        main.add(top, BorderLayout.NORTH);
        main.add(bottom, BorderLayout.CENTER);

        main.setSize(HEIGHT * 2, HEIGHT);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

    public void run( ){

    }

}
