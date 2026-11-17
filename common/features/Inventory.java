package common.features;

import common.features.vehicles.*;
import java.util.ArrayList;

/** Manages the vehicles at a rental location and keeps record of inventory changes. */
public class Inventory{
    /** The human-readable location name for this inventory. */
    private String agencyLocation;
    /** Vehicles currently owned by this location. */
    private ArrayList<Vehicle> lotStock;
    /** The Date object with the current date */
    private Date currentDate = Date.today();


    // CONSTRUCTORS ---------------------------------------------------------------------
    /** Creates an inventory with the default location name. */
    public Inventory() {
        agencyLocation = "Unnamed Lot";
        lotStock = new ArrayList<>();
    }

    public Inventory(String lotLocation, ArrayList<Vehicle> lotStock) {
        agencyLocation = lotLocation;
        this.lotStock = lotStock;
    }

    /**
     * Creates an inventory for the supplied location.
     *
     * @param lotLocation the name of the rental location
     */
    public Inventory(String lotLocation) { agencyLocation = lotLocation; }

    public Inventory(ArrayList<Vehicle> lotStock) { this.lotStock = lotStock; }


    // PRIVATE METHODS ------------------------------------------------------------------
    /**
     * Finds the list position of a vehicle ID, ignoring surrounding spaces.
     *
     * @param identifier the vehicle ID to search for
     * @return the vehicle's index, or {@code -1} when the ID is invalid or absent
     */
        private int getIndexByID(String identifier){
        if(identifier == null){ return -1; }
        String searchID = identifier.trim();
        if(searchID.isEmpty()){ return -1; }
        int cnt = lotStock.size();
        // Search each vehicle because the inventory is stored in a simple list.
        for(int i = 0; i < cnt; i++){
            if(searchID.equals(lotStock.get(i).getID())){ return i; }
        }
        return -1;
    }

    /**
     * Checks whether the exact vehicle object is already in the inventory.
     * Reference equality is used because two distinct cars may have identical details.
     *
     * @param asset the vehicle object to check
     * @return {@code true} if this object is already present
     */
    private boolean findVehicleByObject(Vehicle asset){
        for(Vehicle vehicle : lotStock){
            if(vehicle == asset){ return true; }
        }
        return false;
    }

    // PUBLIC METHODS -------------------------------------------------------------------
    /**
     * Finds a vehicle by its ID.
     *
     * @param identifier the ID to search for; surrounding spaces are ignored
     * @return the matching vehicle, or {@code null} if no vehicle matches
     */
    public Vehicle findVehicle(String identifier){
        int index = getIndexByID(identifier);
        if(index < 0){ return null; }
        return lotStock.get(index);
    }

    /**
     * Adds a vehicle and records the addition in its history.
     * Duplicate IDs, duplicate vehicle objects, and null vehicles are ignored
     * so the inventory stays consistent.
     *
     * @param asset the vehicle to add
     */
    public void addVehicle(Vehicle asset){
        // Reject bad input before changing the vehicle or its history.
        if(asset == null){ return; }
        String vehicleID = asset.getID();
        // A vehicle ID and vehicle object should each appear only once here.
        if(getIndexByID(vehicleID) >= 0 || findVehicleByObject(asset)){ return; }
        asset.setID(vehicleID);
        asset.record(currentDate.getDate() + ": " + vehicleID + " was added to the inventory.");
        lotStock.add(asset);
    }

    /**
     * Removes an available vehicle and records the removal in its history.
     * Missing IDs and vehicles with active rentals are safely ignored.
     *
     * @param identifier the ID of the vehicle to remove
     */
    public void removeVehicle(String identifier){
        int index = getIndexByID(identifier);
        // Keep the vehicle in the lot until its active rental has been returned.
        if(index >= 0 && isAvailable(identifier) && !hasPendingReservation(lotStock.get(index))){
            lotStock.get(index).record(currentDate.getDate() + ": " + identifier +
                " was removed from the inventory.");
            lotStock.remove(index);
        }
    }

    /**
     * Returns the list of vehicles on the lot.
     *
     * @return ArrayList<Vehicle> the list of vehicles on the lot.
     */
    public ArrayList<Vehicle> list(){ return lotStock; }

    /**
     * Returns the list of vehicles on the lot.
     *
     * @return ArrayList<Vehicle> the list of vehicles on the lot.
     */
    public void importList(ArrayList<Vehicle> lotStock){ this.lotStock = lotStock; }


    /** Changes the name of the rental lot.
     *
     * @param agencyLocation    Name of the lot being inventoried
     */
    public void setLotName(String agencyLocation){ this.agencyLocation = agencyLocation; }

    /**
     * Returns the name of the rental lot.
     *
     * @return String value of the specific lot id
     */
    public String getLotName(){ return agencyLocation; }

}
