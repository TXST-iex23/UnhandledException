package common.features;

import common.features.vehicles.*;
import java.util.ArrayList;

/**
 * Manages the vehicles at one rental location and keeps rental records.
 * This class contains application logic; the console menu is handled elsewhere.
 */
public class Rent{
    /** Vehicles currently owned by this location. */
    private ArrayList<Vehicle> lotStock = new ArrayList<>();
    /** Current and completed rentals created by this inventory. */
    private ArrayList<Booking> rentals;
    /** Number used to assign the next reservation ID. */
    private int nextReservationNumber = 1;
    /** The Date object with the current date */
    private Date currentDate = Date.today();

    /** Creates an inventory with the default location name. */
    public Rent() { rentals = new ArrayList<>(); }

    /**
     * Creates an inventory for the supplied location.
     *
     * @param lotLocation the name of the rental location
     */
    public Rent(ArrayList<Vehicle> lotStock) {
        rentals = new ArrayList<>();
        this.lotStock = lotStock;
    }


// PRIVATE METHODS -------------------------------------------------------------------------------
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

    /** Finds a reservation by its ID only while it is pending. */
    private Booking findReservation(String reservationID){
        if(reservationID == null || reservationID.trim().isEmpty()){ return null; }
        String searchID = reservationID.trim();
        for(Booking rental : rentals){
            if(searchID.equals(rental.getReservationID()) &&
               rental.getLifecycleState() == Booking.Status.RESERVED){
                return rental;
            }
        }
        return null;
    }

    /** Checks whether a vehicle already has a rental that has not been returned. */
    private boolean hasActiveRental(Vehicle asset){
        for(Booking rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){ return true; }
        }
        return false;
    }

    /** Checks whether a vehicle is referenced by a pending reservation. */
    private boolean hasPendingReservation(Vehicle asset){
        for(Booking rental : rentals){
            if(rental.getVehicle() == asset &&
               rental.getLifecycleState() == Booking.Status.RESERVED){ return true; }
        }
        return false;
    }

// PUBLIC METHODS -------------------------------------------------------------------------------
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
     * Completes the active rental for a vehicle and records its return.
     *
     * @param identifier the ID of the vehicle being returned
     * @return the completed rental, or {@code null} if there is no active rental
     */
    public Booking returnVehicle(String identifier){

        // Mark the existing rental complete instead of deleting its history.
        for(Booking rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){
                rental.markReturned();
                asset.record(currentDate.getDate() + ": " + asset.getID() +
                    " was returned by " + rental.getRenter().getName() + ".");
                return rental;
            }
        }
        return null;
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
    public Booking rentVehicle(Vehicle asset, Renter renter, int duration){
        // Basic argument checks prevent invalid rentals from reaching Rental.
        if(asset == null || renter == null || duration <= 0){ return null; }
        Booking rental = new Booking(asset, renter, currentDate, duration);
        rentals.add(rental);
        // The vehicle stays in inventory; its active rental makes it unavailable.
        asset.record(currentDate.getDate() + ": " + asset.getID() + " was rented by " +
            renter.getName() + " for " + duration + " days.");
        return rental;
    }

}