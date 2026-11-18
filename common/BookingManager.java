package common;

import common.features.*;
import common.features.vehicles.*;
import java.util.ArrayList;

/**
 * Manages the vehicles at one rental location and keeps rental records.
 * This class contains application logic; the console menu is handled elsewhere.
 */
public class BookingManager {
    private Inventory inventory;
    private Rent rent;
    private Reserve rsvd;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private Date currentDate = Date.today();

    // CONSTRUCTORS ------------------------------------------------------------------

    /** Begins the manager with an empty inventory. */
    public BookingManager() {
        inventory = new Inventory();
        rent = new Rent(inventory, bookings);
        rsvd = new Reserve(inventory, bookings);
    }

    /**
     * Imports the inventory that will be referenced by manager.
     *
     * @param inventory     holds the list of vehicles owned by the lot.
     * @param booking       holds the list of rentals and reservations.     */
    public BookingManager(Inventory inventory) {
        this.inventory = inventory;
        rent = new Rent(inventory,bookings);
        rsvd = new Reserve(inventory,bookings);
    }


    // PUBLIC METHODS -------------------------------------------------------------------
    // INVENTORY METHODS <><><><><><><><<><><><><><><><><><><><><><><><><><><><><><<><><>

    /** Returns the name of the vehicle lot.    */
    public String getLotName(){ return inventory.getLotName(); }

    /** Changes the name of the vehicle lot.    */
    public void setLotName(String agencyLocation){ inventory.setLotName(agencyLocation); }

    /** Returns the vehicle object associated with the provided id.
     *
     * @param identifier    The id associated with some vehicle object.
     */
    public Vehicle find(String identifier){ return inventory.findVehicle(identifier); }

    /** Adds a vehicle into the vehicle lots inventory.
     *
     * @param asset     The vehicle being added into inventory.     */
    public void add(Vehicle asset){ inventory.addVehicle(asset); }

    /** Remove a vehicle from the vehicle lots inventory.
     *
     * @param asset     The vehicle being removed from inventory.     */
    public void remove(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        boolean isRsvd = false;
        for(Booking booking : bookings){
            if(booking.getVehicle() == asset && booking.getLifecycleState() == Booking.Status.RESERVED){
                isRsvd = true;
                break;
            }
        }
        if(rsvd.isAvailableForPeriod(asset,currentDate,1) && !isRsvd){ inventory.removeVehicle(identifier); }
    }

    /** Returns a list of the lot's owned vehicles. */
    public ArrayList<Vehicle> list(){ return  inventory.list(); }

    /** Imports a list of vehicle to replace the lots inventory.
     *
     * @param lotStock  The replacement list of vehicles     */
    public void importList(ArrayList<Vehicle> lotStock){ inventory.importList(lotStock); }


    // RENT METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>

    /** Rents a vehicle out to customer for a predetermined number of days.
     *
     * @param asset     The vehicle being rented
     * @param renter    The customer making the rental
     * @param duration  The durations of the rental in days     */
    public Booking rentVehicle(Vehicle asset, Renter renter, int duration) {
        String identifier = asset.getID();
        if(identifier == null || inventory.findVehicle(identifier) != asset ||
        !rsvd.isAvailableForPeriod(asset, currentDate, duration)) { return null; }
        return rent.rentVehicle(asset,renter,duration);
    }

    /** Returns a vehicle that has been rented out.
     *
     * @param identifier    Refers to the vehicle that is being returned     */
    public Booking returnVehicle(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return null; }
        return rent.returnVehicle(asset);
    }


    // RESERVE METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>

    /** Returns a list of the vehicles not currently rented out or reserved. */
    public ArrayList<Vehicle> availableVehicles(){ return rsvd.getAvailableVehicles(); }

    /** Indicates if a specific vehicle is currently available.
     *
     * @param identifier    The reference of vehicle of inquiry     */
    public boolean isAvailable(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        return rsvd.isAvailableForPeriod(asset, currentDate, 1); }

    /** Returns a list of the vehicles currently rented out.    */
    public ArrayList<Booking> activeRentals(){ return rsvd.getActiveRentals(); }

    /** Returns a list of the vehicles currently reserved for rental.   */
    public ArrayList<Booking> reservations(){ return rsvd.getReservations(); }

    /** Indicates if a specific vehicle will available at a future date.
     *
     * @param asset         The vehicle of inquiry
     * @param pickupDate    The later date in questions
     * @param duration      The durations of the rental in days     */
    public boolean willBeAvailable(String identifier, Date pickupDate, int duration){
        Vehicle asset = inventory.findVehicle(identifier);
        return rsvd.isAvailableForPeriod(asset, pickupDate, duration);
    }

    /** Makes the reservation for a future rental.
     *
     * @param identifier        The vehicle of inquiry
     * @param pickupDate    The later date in questions
     * @param duration      The durations of the rental in days     */
    public Booking reserve(String identifier, Renter renter, Date pickupDate, int duration){
            Vehicle asset = inventory.findVehicle(identifier);
            return rsvd.reserveVehicle(asset, renter, pickupDate, duration);
        }

    /** Cancels a previously made reservation for a future rental.
     *
     * @param identifier        The vehicle of inquiry     */
    public boolean cancelReservation(String reservationID){ return rsvd.cancelReservation(reservationID); }

    /** Rents out a vehicle that was previously reserved.
     *
     * @param identifier        The vehicle of inquiry     */
    public Booking activate(String reservationID){ return rsvd.activateReservation(reservationID); }

    /** Returns a list of the vehicles that will be available at a later date.  */
    public ArrayList<Vehicle> getAvailableVehicles(Date pickupDate, int duration)
        { return rsvd.getAvailableVehicles(pickupDate, duration); }


    // ADDITIONAL METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>

    /** Returns the inventory class to so other classes can use its methods.    */
    public Inventory getInventory(){
        return inventory;
    }
}
