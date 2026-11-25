package gui.panels.subpanels.listPanels;

import gui.panels.subpanels.ListPanel;
import javax.swing.*;
import java.awt.*;


/** Associated with the method that adds vehicles to the inventory  */
public class InventoryList extends ListPanel{
    // Variables needed to create renters.

    /**
     * Associated with the method that adds vehicles to the inventory
     *
     * @param TBD  Desription is TBD.
     */
    public InventoryList(){
		String types [] = {"Car", "Motorcycle"};
		
		/* from https://docs.oracle.com/javase/tutorial/uiswing/components/list.html */
		JList list = new JList(types); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
	}

}
