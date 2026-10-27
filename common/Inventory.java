package common;
import java.util.ArrayList;

import common.vehicles.*;

/**
 * Manages the vehicles at one rental location and keeps rental records.
 * This class contains application logic; the console menu is handled elsewhere.
 */
public class Inventory{
    /** The human-readable location name for this inventory. */
    private String agencyLocation;
    /** Vehicles currently owned by this location. */
    private ArrayList<Vehicle> lotStock = new ArrayList<>();
    /** Current and completed rentals created by this inventory. */
    private ArrayList<Rental> rentals = new ArrayList<>();

    /** Creates an inventory with the default location name. */
    public Inventory() { agencyLocation = "Original Lot"; }

    /**
     * Creates an inventory for the supplied location.
     *
     * @param lotLocation the name of the rental location
     */
    public Inventory(String lotLocation) { agencyLocation = lotLocation; }

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
     * Adds a vehicle and records the addition in its history.
     * Blank IDs, duplicate IDs, duplicate vehicle objects, and null vehicles
     * are ignored so the inventory stays consistent.
     *
     * @param asset the vehicle to add
     * @param identifier the unique ID to assign to the vehicle
     */
    public void addVehicle(Vehicle asset, String identifier){
        // Reject bad input before changing the vehicle or its history.
        if(asset == null || identifier == null || identifier.trim().isEmpty()){ return; }
        String vehicleID = identifier.trim();
        // A vehicle ID and vehicle object should each appear only once here.
        if(getIndexByID(vehicleID) >= 0 || findVehicleByObject(asset)){ return; }
        asset.setID(vehicleID);
        asset.record(vehicleID + " was added to the inventory.");
        lotStock.add(asset);
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
     * Makes a new list containing vehicles that are not currently rented.
     * Changes to the returned list do not change the inventory itself.
     *
     * @return a list of currently available vehicles
     */
    public ArrayList<Vehicle> getAvailableVehicles(){
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        // Check rental status for each vehicle rather than storing a second status flag.
        for(Vehicle asset : lotStock){
            if(isAvailable(asset.getID())){ availableVehicles.add(asset); }
        }
        return availableVehicles;
    }

    /**
     * Makes a new list of rentals that have not yet been returned.
     *
     * @return a list of active rentals
     */
    public ArrayList<Rental> getActiveRentals(){
        ArrayList<Rental> activeRentals = new ArrayList<>();
        // Completed rentals remain in the history list but are not shown as active.
        for(Rental rental : rentals){
            if(rental.isActive()){ activeRentals.add(rental); }
        }
        return activeRentals;
    }

    /**
     * Checks whether an inventory vehicle is not part of an active rental.
     *
     * @param identifier the vehicle ID to check
     * @return {@code true} only when the ID belongs to an available vehicle
     */
    public boolean isAvailable(String identifier){
        Vehicle asset = findVehicle(identifier);
        if(asset == null){ return false; }

        // Identity comparison connects the rental to the vehicle object in this inventory.
        for(Rental rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){ return false; }
        }
        return true;
    }

    /**
     * Starts a rental when the vehicle is in this inventory and available.
     * A successful rental is saved and written to the vehicle's history.
     *
     * @param asset the vehicle to rent
     * @param renter the customer renting it
     * @param duration the rental length in days; must be positive
     * @return the new rental, or {@code null} if the request is invalid or unavailable
     */
    public Rental rentVehicle(Vehicle asset, Renter renter, int duration){
        // Basic argument checks prevent invalid rentals from reaching Rental.
        if(asset == null || renter == null || duration <= 0){ return null; }
        String identifier = asset.getID();
        // Identity comparison ensures this is the actual inventory vehicle, not a copy.
        if(identifier == null || findVehicle(identifier) != asset ||
           !isAvailable(identifier)){ return null; }

        Rental rental = new Rental(asset, renter, duration);
        rentals.add(rental);
        // The vehicle stays in inventory; its active rental makes it unavailable.
        asset.record(asset.getID() + " was rented by " + renter.getName() +
                     " for " + duration + " days.");
        return rental;
    }

    /**
     * Completes the active rental for a vehicle and records its return.
     *
     * @param identifier the ID of the vehicle being returned
     * @return the completed rental, or {@code null} if there is no active rental
     */
    public Rental returnVehicle(String identifier){
        Vehicle asset = findVehicle(identifier);
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return null; }

        // Mark the existing rental complete instead of deleting its history.
        for(Rental rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){
                rental.markReturned();
                asset.record(asset.getID() + " was returned by " +
                             rental.getRenter().getName() + ".");
                return rental;
            }
        }
        return null;
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
        if(index >= 0 && isAvailable(identifier)){
            lotStock.get(index).record(identifier + " was removed from the inventory.");
            lotStock.remove(index);
        }
    }

    /**
     * Calculates the number of days between two dates using their day codes.
     *
     * @param checkout the rental start date
     * @param checkin the rental return date
     * @return the day-code difference from checkout to checkin
     * @throws NullPointerException if either date is {@code null}
     */
    public int cntDays(Date checkout, Date checkin){
        return checkin.getDateCode() - checkout.getDateCode();
    }
}
