package gui.panels.subpanels.dataPanels;

import gui.panels.subpanels.DataPanel;
import javax.swing.*;
import java.awt.*;

/** Associated with the method that adds vehicles to the inventory  */
public class InventoryData extends DataPanel{

    /**
     * Associated with the method that adds vehicles to the inventory
     *
     * @param TBD  Desription is TBD.
     */
    public InventoryData( ){
		JPanel mainPanel = new JPanel();
		JPanel vehiclePanel = new JPanel();
		String combo[] = {"Car","Motorcycle"};
		
		JButton addButton = new JButton("ADD");
		JTextField yearField = new JTextField("Year");
		JTextField makerField = new JTextField("Year");
		JTextField modelField = new JTextField("Year");
		JComboBox typeField = new JComboBox(combo);

		vehiclePanel.add(yearField);
		vehiclePanel.add(makerField);
		vehiclePanel.add(modelField);
		vehiclePanel.add(typeField);
		mainPanel.add(vehiclePanel);
	}

}
