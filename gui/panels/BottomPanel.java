package gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * The entry point for the graphical user interface. From here all UI-based
 * operations are implemented
 */
public class BottomPanel extends JPanel{               // will become abstract
    /** The application inventory that this menu operates on. */
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton placeholder2 = new JButton("Button2");
    private JButton placeholder3 = new JButton("Button3");


    /**
     * Builds the bottom half of the GUI display
     *
     * @param leftPanel  The left side of the GUI Panel; for vehicle lists
     * @param rightPanel  The right side of the GUI Panel; for vehicle lists
     */
    public BottomPanel( ){
        mainPanel = new JPanel(new GridLayout(1, 2, 1, 1));
        leftPanel = new JPanel();
        leftPanel.add(placeholder2);
        rightPanel = new JPanel();
        rightPanel.add(placeholder3);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    /**
     * Plaseholder
     */
    public JPanel getBottomPanel(){ return mainPanel; }
}
