package common.features;

import common.features.vehicles.*;
import java.util.ArrayList;

/** Manages vehicle reservations.       */
public class Reserve{
    /** Vehicles currently owned by this location. */
    private ArrayList<Vehicle> lotStock = new ArrayList<>();
    /** Current and completed rentals created by this inventory. */
    private ArrayList<Booking> bookings;
    /** Number used to assign the next reservation ID. */
    private int nextReservationNumber = 1;
    /** The Date object with the current date */
    private Date currentDate = Date.today();
    /** Creates an inventory with the default location name. */


    // CONSTRUCTOR ------------------------------------------------------------------------------

    /**
     * Initiates the class with an inventory and a list of bookings.
     *
     * @param inventory     A list of all vehicle in inventory
     * @param bookings      A list of rentals, reservations, and returns     */
    public Reserve(Inventory inventory, ArrayList<Booking> bookings) {
        this.bookings = bookings;
        this.lotStock = inventory.list();
    }

    // PRIVATE METHODS --------------------------------------------------------------------------

    /** Finds a reservation by its ID only while it is pending. */
    private Booking findReservation(String reservationID){
        if(reservationID == null || reservationID.trim().isEmpty()){ return null; }
        String searchID = reservationID.trim();
        for(Booking booking : bookings){
            if(searchID.equals(booking.getReservationID()) &&
               booking.getLifecycleState() == Booking.Status.RESERVED){
                return booking;
            }
        }
        return null;
    }

    /** Checks whether a vehicle already has a rental that has not been returned. */
    private boolean hasActiveRental(Vehicle asset){
        for(Booking booking : bookings){
            if(booking.isActive() && booking.getVehicle() == asset){ return true; }
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
            if(isAvailableForPeriod(asset,currentDate,1)){ availableVehicles.add(asset); }
        }
        return availableVehicles;
    }


    /**
     * Makes a new list of rentals that have not yet been returned.
     *
     * @return a list of active rentals
     */
    public ArrayList<Booking> getActiveRentals(){
        ArrayList<Booking> activeRentals = new ArrayList<>();
        // Completed rentals remain in the history list but are not shown as active.
        for(Booking booking : bookings){
            if(booking.isActive()){ activeRentals.add(booking); }
        }
        return activeRentals;
    }

    /**
     * Makes a list containing reservations that have not been cancelled or activated.
     *
     * @return the pending reservations
     */
    public ArrayList<Booking> getReservations(){
        ArrayList<Booking> reservations = new ArrayList<>();
        for(Booking booking : bookings){
            if(booking.getLifecycleState() == Booking.Status.RESERVED){
                reservations.add(booking);
            }
        }
        return reservations;
    }

    /**
     * Checks whether a vehicle is free for every day in a requested period.
     * Active rentals conservatively block the vehicle until it is returned.
     *
     * @param identifier the vehicle ID to check
     * @param pickupDate the first day of the requested period
     * @param duration the number of occupied days; must be positive
     * @return {@code true} if the vehicle is in this inventory and has no conflict
     */
    public boolean isAvailableForPeriod(Vehicle asset, Date pickupDate, int duration){
        if(pickupDate == null || duration <= 0){ return false; }
        if(asset == null){ return false; }

        long newStart = pickupDate.getDateCode();
        long newEnd = newStart + duration - 1L;
        if(newStart < Date.today().getDateCode() || newEnd > Integer.MAX_VALUE){
            return false;
        }

        for(Booking booking : bookings){
            if(booking.getVehicle() != asset){ continue; }
            if(booking.getLifecycleState() == Booking.Status.ACTIVE){ return false; }
            if(booking.getLifecycleState() == Booking.Status.RESERVED){
                long existingStart = booking.getPickupDate().getDateCode();
                long existingEnd = booking.getEndDayCode();
                // Both ranges include their pickup and final occupied days.
                if(newStart <= existingEnd && existingStart <= newEnd){ return false; }
            }
        }
        return true;
    }

    /**
     * Creates a reservation when the vehicle is free for the complete period.
     * Failed requests do not change the reservation list or its next ID.
     *
     * @param identifier the ID of the requested vehicle
     * @param renter the person making the reservation
     * @param pickupDate the first day of the requested period
     * @param duration the number of occupied days; must be positive
     * @return the new reservation, or {@code null} if the request is invalid or unavailable
     */
    public Booking reserveVehicle(Vehicle asset, Renter renter, Date pickupDate, int duration){
        if(asset == null || renter == null || pickupDate == null ||
           !isAvailableForPeriod(asset, pickupDate, duration)){
            return null;
        }

        String reservationID = String.format("R%04d", nextReservationNumber);
        Booking reservation = new Booking(reservationID, asset, renter, pickupDate, duration);
        bookings.add(reservation);
        nextReservationNumber++;
        asset.record(currentDate.getDate() + ": " + reservationID + " was reserved for " +
            renter.getName() + " starting " + pickupDate.getDateString() + ".");
        return reservation;
    }

    /**
     * Cancels a pending reservation. Other reservation states cannot be cancelled.
     *
     * @param reservationID the ID returned when the reservation was created
     * @return {@code true} if a pending reservation was cancelled
     */
    public boolean cancelReservation(String reservationID){
        Booking reservation = findReservation(reservationID);
        if(reservation == null || !reservation.cancel()){ return false; }
        reservation.getVehicle().record(currentDate.getDate() + ": " +
            reservation.getReservationID() + " was cancelled.");
        return true;
    }

    /**
     * Activates a pending reservation on its pickup date.
     *
     * @param reservationID the reservation ID
     * @return the active rental, or {@code null} if activation is not allowed
     */
    public Booking activateReservation(String reservationID){
        Booking reservation = findReservation(reservationID);
        if(reservation == null ||
           reservation.getPickupDate().getDateCode() != Date.today().getDateCode() ||
           hasActiveRental(reservation.getVehicle())){
            return null;
        }
        if(!reservation.activate()){ return null; }
        reservation.getVehicle().record(currentDate.getDate() + ": " +
            reservationID.trim() + " was picked up.");
        return reservation;
    }

    /**
     * Makes a list of vehicles free for the full requested period.
     *
     * @param pickupDate the first day of the requested period
     * @param duration the number of occupied days
     * @return available vehicles, or an empty list for an invalid period
     */
    public ArrayList<Vehicle> getAvailableVehicles(Date pickupDate, int duration){
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        for(Vehicle asset : lotStock){
            if(isAvailableForPeriod(asset, pickupDate, duration)){
                availableVehicles.add(asset);
            }
        }
        return availableVehicles;
    }
}