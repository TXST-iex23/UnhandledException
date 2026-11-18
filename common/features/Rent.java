package common.features;

import common.features.vehicles.*;
import java.util.ArrayList;

/** Manages vehicle rentals.        */
public class Rent{
    /** Inventory which holds the list of vehicles. (Unused but here anyway) */
    private Inventory inventory;
    /** Current and completed rentals created by this inventory. */
    private ArrayList<Booking> rentals;
    /** The Date object with the current date */
    private Date currentDate = Date.today();


// CONSTRUCTORS ---------------------------------------------------------------------------------

    /**
     * Initiates the class with an inventory and a list of bookings.
     *
     * @param inventory     A list of all vehicle in inventory
     * @param bookings      A list of rentals, reservations, and returns     */
    public Rent(Inventory inventory, ArrayList<Booking> rentals) {
        this.inventory = inventory;
        this.rentals = rentals;
    }

// PUBLIC METHODS -------------------------------------------------------------------------------

    /**
     * Completes the active rental for a vehicle and records its return.
     *
     * @param identifier the ID of the vehicle being returned
     * @return the completed rental, or {@code null} if there is no active rental
     */
    public Booking returnVehicle(Vehicle asset){
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return null; }
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