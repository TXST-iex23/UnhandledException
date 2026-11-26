package gui.panels.subpanels.micropanels;

import gui.panels.subpanels.DataPanel;
import javax.swing.*;
import java.awt.*;


/** Associated with the method that adds vehicles to the inventory  */
public class InventoryList extends DataPanel{
    JList<String> list;
    JScrollPane listScroller;

    /**
     * Associated with the method that adds vehicles to the inventory
     *
     * @param TBD  Desription is TBD.
     */
    public InventoryList(){
        String types [] = {"Car1", "Car2"};

        /* from https://docs.oracle.com/javase/tutorial/uiswing/components/list.html */
        list = new JList<String>(types);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));

        topLevelPanel.add(list);
        bottomLevelPanel.add(new JButton("REMOVE"));

    }
}
