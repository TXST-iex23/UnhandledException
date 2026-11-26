package gui.panels.subpanels;
import javax.swing.*;
// import java.awt.*;

/**
 * Add description here */
public abstract class DataPanel extends JPanel{
    protected JPanel dataPanel;
    // Panel Components
    protected JPanel topLevelPanel;
    protected JPanel midLevelPanel;
    protected JPanel bottomLevelPanel;

    // CONSTRUCTORS  ===========================================================================
    /**
     * builds a list deplay panel
     *
     * @param variable  the variable description
     */
    public DataPanel( ){
        topLevelPanel = new JPanel();
        midLevelPanel = new JPanel();
        bottomLevelPanel = new JPanel();
        makePanel();
    }

    public DataPanel(JPanel topLevelPanel, JPanel midLevelPanel, JPanel bottomLevelPanel){
        this.topLevelPanel = topLevelPanel;
        this.midLevelPanel = midLevelPanel;
        this.bottomLevelPanel = bottomLevelPanel;
        dataPanel = makePanel();
    }

    // METHODS  ================================================================================
    public JPanel getPanel(){ return dataPanel; }

    public void makePanel(JPanel topLevelPanel, JPanel midLevelPanel, JPanel bottomLevelPanel){
        this.topLevelPanel = topLevelPanel;
        this.midLevelPanel = midLevelPanel;
        this.bottomLevelPanel = bottomLevelPanel;
        dataPanel = makePanel();
        dataPanel.setVisible(true);
    }

    private JPanel makePanel(){
        dataPanel = new JPanel();
        dataPanel.add(topLevelPanel);
        dataPanel.add(midLevelPanel);
        dataPanel.add(bottomLevelPanel);
        dataPanel.setVisible(true);
        return dataPanel;
    }
}
