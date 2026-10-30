package common;

import common.vehicles.Vehicle;

/**
 * Represents an immediate rental or a future reservation for one vehicle.
 * The booking keeps references to the existing {@link Vehicle} and {@link Renter}
 * objects instead of copying their data.
 */
public class Rental{
    /** The possible stages in a rental or reservation's life. */
    public enum Status{ RESERVED, ACTIVE, RETURNED, CANCELLED }

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

    /** Creates a reservation; Inventory checks the requested date before calling this. */
    Rental(String id, Vehicle reservedVehicle, Renter customer, Date pickup,
           int rentalDuration){
        this(id, reservedVehicle, customer, pickup, rentalDuration, Status.RESERVED);
    }

    /** Creates an immediate rental using the pickup date already checked by Inventory. */
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

    /** @return the vehicle associated with this booking */
    public Vehicle getVehicle(){ return vehicle; }

    /** @return the customer associated with this booking */
    public Renter getRenter(){ return renter; }

    /** @return the agreed rental length in days */
    public int getDuration(){ return duration; }

    /** @return the reservation ID, or {@code null} for an immediate rental */
    public String getReservationID(){ return reservationID; }

    /** @return an independent copy of the pickup date */
    public Date getPickupDate(){ return pickupDate.copy(); }

    /** @return the last occupied day, including the pickup day */
    public long getEndDayCode(){ return (long)pickupDate.getDateCode() + duration - 1; }

    /** @return {@code true} while the booking is an active rental */
    public boolean isActive(){ return status == Status.ACTIVE; }

    /** @return the lifecycle status as a readable word */
    public String getStatus(){
        String stateName = status.name().toLowerCase();
        return Character.toUpperCase(stateName.charAt(0)) + stateName.substring(1);
    }

    /** @return the lifecycle state used by Inventory to check allowed operations */
    public Status getLifecycleState(){ return status; }

    /**
     * Calculates the charge through the vehicle's pricing override.
     *
     * @return the total charge for the rental duration
     */
    public double getTotalCharge(){ return vehicle.calculateRentalCost(duration); }

    /** Changes a pending reservation into an active rental. */
    boolean activate(){
        if(status != Status.RESERVED){ return false; }
        status = Status.ACTIVE;
        return true;
    }

    /** Cancels a pending reservation, leaving all other states unchanged. */
    boolean cancel(){
        if(status != Status.RESERVED){ return false; }
        status = Status.CANCELLED;
        return true;
    }

    /** Marks an active rental returned; Inventory calls this while processing a return. */
    boolean markReturned(){
        if(status != Status.ACTIVE){ return false; }
        status = Status.RETURNED;
        return true;
    }
}
