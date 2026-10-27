package common;

import common.vehicles.Vehicle;

/**
 * Represents one customer's rental of one vehicle for a set number of days.
 * The rental keeps references to the existing {@link Vehicle} and {@link Renter}
 * objects instead of copying their data.
 */
public class Rental{
    /** The vehicle associated with this rental. */
    private Vehicle vehicle;
    /** The customer who rented the vehicle. */
    private Renter renter;
    /** The agreed rental length in days. */
    private int duration;
    /** Whether the vehicle is still out on this rental. */
    private boolean active;

    /**
     * Creates an active rental after checking its required values.
     *
     * @param rentedVehicle the vehicle being rented
     * @param customer the customer renting the vehicle
     * @param rentalDuration the number of rental days; must be positive
     * @throws IllegalArgumentException if a required object is null or duration is not positive
     */
    public Rental(Vehicle rentedVehicle, Renter customer, int rentalDuration){
        // A rental must always refer to real objects and a positive number of days.
        if(rentedVehicle == null || customer == null){
            throw new IllegalArgumentException("Vehicle and renter are required.");
        }
        if(rentalDuration <= 0){
            throw new IllegalArgumentException("Rental duration must be greater than zero.");
        }
        vehicle = rentedVehicle;
        renter = customer;
        duration = rentalDuration;
        // New rental records begin active; Inventory changes this when the vehicle returns.
        active = true;
    }

    /**
     * Returns the vehicle for this rental.
     *
     * @return the rented vehicle
     */
    public Vehicle getVehicle(){ return vehicle; }

    /**
     * Returns the customer for this rental.
     *
     * @return the renter
     */
    public Renter getRenter(){ return renter; }

    /**
     * Returns the agreed rental length.
     *
     * @return the number of days
     */
    public int getDuration(){ return duration; }

    /**
     * Reports whether the rental has not yet been returned.
     *
     * @return {@code true} while the rental is active
     */
    public boolean isActive(){ return active; }

    /**
     * Returns a readable status label for this rental.
     *
     * @return {@code "Active"} or {@code "Returned"}
     */
    public String getStatus(){ return active ? "Active" : "Returned"; }

    /**
     * Calculates this rental's charge using the vehicle's overridden rate method.
     * This is an example of runtime polymorphism: the actual vehicle type chooses
     * which pricing implementation runs.
     *
     * @return the total charge for the rental duration
     */
    public double getTotalCharge(){ return vehicle.calculateRentalCost(duration); }

    /** Marks the rental returned; Inventory calls this while processing a return. */
    void markReturned(){ active = false; }
}
