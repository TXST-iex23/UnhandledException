package common;
import java.util.ArrayList;

import common.vehicles.*;

public class Inventory{
    private String agencyLocation;
    private ArrayList<Vehicle> lotStock = new ArrayList<>();

    public Inventory() { agencyLocation = "Original Lot"; }
    public Inventory(String lotLocation) { agencyLocation = lotLocation; }

    // METHODS  -------------------------------------------------------------
    // PRIVATE **************************************************************
        private int getIndexByID(String identifier){
        int cnt = lotStock.size();
        for(int i = 0; i < cnt; i++){
            if(identifier != null && identifier.equals(lotStock.get(i).getID())){ return i; }
        }
        return -1;
    }

    // PUBLIC **************************************************************
    public void addVehicle(Vehicle asset, String identifier){
        if(asset == null || identifier == null || getIndexByID(identifier) >= 0){ return; }
        asset.setID(identifier);
        asset.record(identifier + " was added to the inventory.");
        lotStock.add(asset);
    }

    public Vehicle findVehicle(String identifier){
        int index = getIndexByID(identifier);
        if(index < 0){ return null; }
        return lotStock.get(index);
    }

    public void removeVehicle(String identifier){
        int index = getIndexByID(identifier);
        if(index < 0) {
            System.out.println("Asset " + identifier + " was not found.");
        } else {
            lotStock.get(index).record(identifier + " was removed from the inventory.");
            lotStock.remove(index);
        }
    }

    public int cntDays(Date checkout, Date checkin){
        return checkin.getDateCode() - checkout.getDateCode();
    }
}
