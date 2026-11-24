package gui;

import common.BookingManager;
import javax.swing.*;
import java.awt.*;

/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class GraphicUI{
    private int HEIGHT = 300;
    private int WIDTH = HEIGHT * 2;
    private int BANNER = 150;

    /** The application inventory that this menu operates on. */
    private BookingManager manager;
    private JFrame main;
    private JPanel top;
    private JPanel bottom;
    private JButton placeholder1 = new JButton("Button1");
    private JButton placeholder2 = new JButton("Button2");


    /**
     * Initiates the graphical window of the application    */
    public GraphicUI( ){
        this.manager = new BookingManager();
        buildFrame();
    }

    /**
     * Initiates the graphical window of the application
     *
     * @param manager  the entry point to the backend of the application
     */
    public GraphicUI(BookingManager manager){
        this.manager = manager;
        buildFrame();
    }



//-----------------------------------------------------------------

    private void buildFrame(){
        main = new JFrame();
        main.setLayout(new BorderLayout());

        top = new JPanel();
        bottom = new JPanel();

        top.add(placeholder1);
        bottom.add(placeholder2);

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
