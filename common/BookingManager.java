package common;
import java.util.ArrayList;

import common.vehicles.*;

/**
 * Manages the vehicles at one rental location and keeps rental records.
 * This class contains application logic; the console menu is handled elsewhere.
 */
public class BookingManager {
    /** The inventory from which bookings are made. */
    private Inventory inventory;
    /** Current and completed rentals created by this inventory. */
    private ArrayList<Booking> bookings = new ArrayList<>();
    /** Number used to assign the next reservation ID. */
    private int nextReservationNumber = 1;
    /** The Date object with the current date */
    private Date currentDate = Date.today();            // thinking to generalize this so can be used in calculation.


    // CONSTRUCTORS ------------------------------------------------------------------
    /** Begins the class with an empty inventory. */
    public BookingManager() { inventory = new Inventory(); }

    /**
     * Imports the inventory that will be referenced.
     *
     * @param inventory     the lot used for bookings
     */
    public BookingManager(Inventory inventory) {
        this.inventory = inventory;
    }


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
        int cnt = inventory.list().size();
        // Search each vehicle because the inventory is stored in a simple list.
        for(int i = 0; i < cnt; i++){
            if(searchID.equals(inventory.list().get(i).getID())){ return i; }
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
        for(Vehicle vehicle : inventory.list()){
            if(vehicle == asset){ return true; }
        }
        return false;
    }

    /** Checks whether a vehicle already has a rental that has not been returned. */
    private boolean hasActiveRental(Vehicle asset){
        for(Booking booking : bookings){
            if(booking.isActive() && booking.getVehicle() == asset){ return true; }
        }
        return false;
    }

    /** Checks whether a vehicle is referenced by a pending reservation. */
    private boolean hasPendingReservation(Vehicle asset){
        for(Booking booking : bookings){
            if(booking.getVehicle() == asset &&
               booking.getLifecycleState() == Booking.Status.RESERVED){ return true; }
        }
        return false;
    }

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

    // PUBLIC METHODS -------------------------------------------------------------------
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
    public boolean isAvailableForPeriod(String identifier, Date pickupDate, int duration){
        if(pickupDate == null || duration <= 0){ return false; }
        Vehicle asset = inventory.findVehicle(identifier);
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
    public Booking reserveVehicle(String identifier, Renter renter, Date pickupDate, int duration){
        Vehicle asset = inventory.findVehicle(identifier);
        if(asset == null || renter == null || pickupDate == null ||
           !isAvailableForPeriod(identifier, pickupDate, duration)){
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
        String identifier = asset.getID();
        // Identity comparison ensures this is the actual inventory vehicle, not a copy.
        Date pickupDate = Date.today();
        if(identifier == null || inventory.findVehicle(identifier) != asset ||
           !isAvailableForPeriod(identifier, pickupDate, duration)){ return null; }

        Booking rental = new Booking(asset, renter, pickupDate, duration);
        bookings.add(rental);
        // The vehicle stays in inventory; its active rental makes it unavailable.
        asset.record(currentDate.getDate() + ": " + asset.getID() + " was rented by " +
            renter.getName() + " for " + duration + " days.");
        return rental;
    }

    /**
     * Completes the active rental for a vehicle and records its return.
     *
     * @param identifier the ID of the vehicle being returned
     * @return the completed rental, or {@code null} if there is no active rental
     */
    public Booking returnVehicle(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return null; }

        // Mark the existing rental complete instead of deleting its history.
        for(Booking booking : bookings){
            if(booking.isActive() && booking.getVehicle() == asset){
                booking.markReturned();
                asset.record(currentDate.getDate() + ": " + asset.getID() +
                    " was returned by " + booking.getRenter().getName() + ".");
                return booking;
            }
        }
        return null;
    }

    /**
     * Calculates the number of days between two dates using their day codes.
     *
     * @return String value of the current date
     */
    public String CurrentDateString(){
        return String.valueOf(currentDate);
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
