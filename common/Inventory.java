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
    private ArrayList<Booking> bookings = new ArrayList<>();
    /** Number used to assign the next reservation ID. */
    private int nextReservationNumber = 1;
    /** The Date object with the current date */
    private Date currentDate = Date.today();

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
     * Makes a list of vehicles free for the full requested period.
     *
     * @param pickupDate the first day of the requested period
     * @param duration the number of occupied days
     * @return available vehicles, or an empty list for an invalid period
     */
    public ArrayList<Vehicle> getAvailableVehicles(Date pickupDate, int duration){
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        for(Vehicle asset : lotStock){
            if(isAvailableForPeriod(asset.getID(), pickupDate, duration)){
                availableVehicles.add(asset);
            }
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
     * Checks whether an inventory vehicle is not part of an active rental.
     *
     * @param identifier the vehicle ID to check
     * @return {@code true} only when the ID belongs to an available vehicle
     */
    public boolean isAvailable(String identifier){
        return isAvailableForPeriod(identifier, Date.today(), 1);
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
        Vehicle asset = findVehicle(identifier);
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
        Vehicle asset = findVehicle(identifier);
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
        if(identifier == null || findVehicle(identifier) != asset ||
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
        Vehicle asset = findVehicle(identifier);
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

    /** Checks whether a vehicle is referenced by a pending reservation. */
    private boolean hasPendingReservation(Vehicle asset){
        for(Booking booking : bookings){
            if(booking.getVehicle() == asset &&
               booking.getLifecycleState() == Booking.Status.RESERVED){ return true; }
        }
        return false;
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
     * Returns the name of the rental lot.
     *
     * @return String value of the specific lot id
     */
    public String getLotName(){ return agencyLocation; }

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
