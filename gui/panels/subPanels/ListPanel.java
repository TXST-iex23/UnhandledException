package gui.panels.subpanels;
import javax.swing.*;
// import java.awt.*;

/**
 * Add description here */
public class ListPanel{
    private JPanel dataPanel;
	// Panel Components
	private JPanel topLevelPanel;
	private	JPanel bottomLevelPanel;

    /**
     * builds a list deplay panel
     *
     * @param variable  the variable description
     */
    public ListPanel( ){
		topLevelPanel = new JPanel();
		bottomLevelPanel = new JPanel();
		makePanel();
	}

	public ListPanel(JPanel topLevelPanel, JPanel bottomLevelPanel){
		this.topLevelPanel = topLevelPanel;
		this.bottomLevelPanel = bottomLevelPanel;
		makePanel();	
	}	

	// METHODS  ================================================================================
	public JPanel getPanel(){ return dataPanel; }

	public void makePanel(JPanel topLevelPanel, JPanel bottomLevelPanel){
		this.topLevelPanel = topLevelPanel;
		this.bottomLevelPanel = bottomLevelPanel;
		makePanel();	
	}	

	private void makePanel(){
		dataPanel = new JPanel();
		dataPanel.add(topLevelPanel);
		dataPanel.add(bottomLevelPanel);
	}
}
