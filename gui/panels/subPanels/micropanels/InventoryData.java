package gui.panels.subpanels.micropanels;

import gui.panels.subpanels.DataPanel;
import javax.swing.*;
// import java.awt.*;

/** Associated with the method that adds vehicles to the inventory  */
public class InventoryData extends DataPanel{
    private String[] combo = {"Car","Motorcycle"};
    private JButton addButton;
    private JTextField yearField;
    private JTextField makerField;
    private JTextField modelField;
    private JComboBox<String> typeField = new JComboBox<>(combo);

    /**
     * Associated with the method that adds vehicles to the inventory
     *
     * @param TBD  Desription is TBD.
     */
    public InventoryData( ){
        addButton = new JButton("ADD");
        yearField = new JTextField("Year");
        makerField = new JTextField("Make");
        modelField = new JTextField("Model");

        topLevelPanel = makeTopPanel(topLevelPanel);
        bottomLevelPanel.add(addButton);
        makePanel(topLevelPanel, midLevelPanel, bottomLevelPanel);
    }

    // METHODS  ================================================================================
    private JPanel makeTopPanel(JPanel topLevelPanel){
        topLevelPanel.add(yearField);
        topLevelPanel.add(makerField);
        topLevelPanel.add(modelField);
        topLevelPanel.add(typeField);
        return topLevelPanel;
    }
}
