package common;

import common.vehicles.Vehicle;

/**
 * Represents an immediate rental or a future reservation for one vehicle.
 * The booking keeps references to the existing {@link Vehicle} and {@link Renter}
 * objects instead of copying their data.
 */
public class Rental{
    /** The possible stages in a rental or reservation's life.
     * A reservation starts as {@link #RESERVED}, can become {@link #ACTIVE} on
     * its pickup day, and then becomes {@link #RETURNED}; an unactivated
     * reservation can instead become {@link #CANCELLED}.
     */
    public enum Status{
        /** A future booking that has not been activated or cancelled. */
        RESERVED,
        /** A vehicle currently checked out to its renter. */
        ACTIVE,
        /** A rental that has been returned. */
        RETURNED,
        /** A reservation cancelled before pickup. */
        CANCELLED
    }

    /** The vehicle associated with this booking. */
    private Vehicle vehicle;
    /** The customer who booked the vehicle. */
    private Renter renter;
    /** The ID used to look up a reservation; immediate rentals have no ID. */
    private String reservationID;
    /** The first day this vehicle is expected to be used. */
    private Date pickupDate;
    /** The agreed rental length in days. */
    private int duration;
    /** The current lifecycle state of this booking. */
    private Status status;

    /**
     * Creates an active rental beginning today.
     *
     * @param rentedVehicle the vehicle being rented
     * @param customer the customer renting the vehicle
     * @param rentalDuration the number of rental days; must be positive
     * @throws IllegalArgumentException if a required value is missing or invalid
     */
    public Rental(Vehicle rentedVehicle, Renter customer, int rentalDuration){
        this(null, rentedVehicle, customer, Date.today(), rentalDuration, Status.ACTIVE);
    }

    /** Creates a pending reservation; Inventory checks the request before calling this.
     * @param id the generated reservation ID
     * @param reservedVehicle the vehicle being reserved
     * @param customer the person making the reservation
     * @param pickup the first day of the reservation period
     * @param rentalDuration the number of occupied days
     */
    Rental(String id, Vehicle reservedVehicle, Renter customer, Date pickup,
           int rentalDuration){
        this(id, reservedVehicle, customer, pickup, rentalDuration, Status.RESERVED);
    }

    /** Creates an active rental using a pickup date already checked by Inventory.
     * @param rentedVehicle the vehicle being rented
     * @param customer the person renting the vehicle
     * @param pickup the rental start date
     * @param rentalDuration the number of occupied days
     */
    Rental(Vehicle rentedVehicle, Renter customer, Date pickup, int rentalDuration){
        this(null, rentedVehicle, customer, pickup, rentalDuration, Status.ACTIVE);
    }

    private Rental(String id, Vehicle bookedVehicle, Renter customer, Date pickup,
                   int rentalDuration, Status startingStatus){
        // A booking needs real objects, a valid pickup date, and a positive duration.
        if(bookedVehicle == null || customer == null || pickup == null){
            throw new IllegalArgumentException("Vehicle, renter, and pickup date are required.");
        }
        if(rentalDuration <= 0){
            throw new IllegalArgumentException("Rental duration must be greater than zero.");
        }
        if(startingStatus == Status.RESERVED && (id == null || id.trim().isEmpty())){
            throw new IllegalArgumentException("A reservation ID is required.");
        }
        long endDayCode = (long)pickup.getDateCode() + rentalDuration - 1;
        if(endDayCode > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Rental dates are outside the supported range.");
        }
        vehicle = bookedVehicle;
        renter = customer;
        reservationID = id;
        // Keep a copy so a caller cannot change the booking date later.
        pickupDate = pickup.copy();
        duration = rentalDuration;
        status = startingStatus;
    }

    /** Returns the vehicle associated with this booking.
     * @return the vehicle associated with this booking
     */
    public Vehicle getVehicle(){ return vehicle; }

    /** Returns the customer associated with this booking.
     * @return the customer associated with this booking
     */
    public Renter getRenter(){ return renter; }

    /** Returns the agreed rental length.
     * @return the duration in days
     */
    public int getDuration(){ return duration; }

    /** Returns the reservation ID assigned to a future booking.
     * @return the reservation ID, or {@code null} for an immediate rental
     */
    public String getReservationID(){ return reservationID; }

    /** Returns a copy so callers cannot change the stored pickup date.
     * @return an independent copy of the pickup date
     */
    public Date getPickupDate(){ return pickupDate.copy(); }

    /** Calculates the final occupied day, counting pickup as the first day.
     * @return the day code of the last occupied day
     */
    public long getEndDayCode(){ return (long)pickupDate.getDateCode() + duration - 1; }

    /** Checks whether the booking is currently an active rental.
     * @return {@code true} while the booking is active
     */
    public boolean isActive(){ return status == Status.ACTIVE; }

    /** Returns the lifecycle status with its first letter capitalized.
     * @return the lifecycle status as a readable word
     */
    public String getStatus(){
        String stateName = status.name().toLowerCase();
        return Character.toUpperCase(stateName.charAt(0)) + stateName.substring(1);
    }

    /** Returns the lifecycle state used to decide which operations are allowed.
     * @return the current lifecycle state
     */
    public Status getLifecycleState(){ return status; }

    /**
     * Calculates the charge through the vehicle's pricing override.
     *
     * @return the total charge for the rental duration
     */
    public double getTotalCharge(){ return vehicle.calculateRentalCost(duration); }

    /**
     * Changes a pending reservation into an active rental.
     *
     * @return {@code true} if the reservation changed to active; {@code false}
     *         if it was not pending
     */
    boolean activate(){
        if(status != Status.RESERVED){ return false; }
        status = Status.ACTIVE;
        return true;
    }

    /**
     * Cancels a pending reservation, leaving all other states unchanged.
     *
     * @return {@code true} if the reservation changed to cancelled;
     *         {@code false} if it was not pending
     */
    boolean cancel(){
        if(status != Status.RESERVED){ return false; }
        status = Status.CANCELLED;
        return true;
    }

    /**
     * Marks an active rental returned; Inventory calls this while processing a return.
     *
     * @return {@code true} if the rental changed to returned; {@code false}
     *         if it was not active
     */
    boolean markReturned(){
        if(status != Status.ACTIVE){ return false; }
        status = Status.RETURNED;
        return true;
    }
}
