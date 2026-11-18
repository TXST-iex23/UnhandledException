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


    // PUBLIC METHODS -------------------------------------------------------------------
    // INVENTORY METHODS <><><><><><><><<><><><><><><><><><><><><><><><><><><><><><<><><>
    public String getLotName(){ return inventory.getLotName(); }
    public void setLotName(String agencyLocation){ inventory.setLotName(agencyLocation); }
    public Vehicle find(String identifier){ return inventory.findVehicle(identifier); }
    public void add(Vehicle asset){ inventory.addVehicle(asset); }

    public void remove(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        boolean isRsvd = false;
        /** Checks whether a vehicle is referenced by a pending reservation. */

        for(Booking booking : bookings){
            if(booking.getVehicle() == asset && booking.getLifecycleState() == Booking.Status.RESERVED){
                isRsvd = true;
                break;
            }
        }
        if(rsvd.isAvailableForPeriod(asset,currentDate,1) && !isRsvd){ inventory.removeVehicle(identifier); }
    }

    public ArrayList<Vehicle> list(){ return  inventory.list(); }
    public void importList(ArrayList<Vehicle> lotStock){ inventory.importList(lotStock); }



    // RENT METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>
    public Booking rentVehicle(Vehicle asset, Renter renter, int duration) {
        String identifier = asset.getID();
        if(identifier == null || inventory.findVehicle(identifier) != asset ||
        !rsvd.isAvailableForPeriod(asset, currentDate, duration)) { return null; }
        return rent.rentVehicle(asset,renter,duration);
    }

    public Booking returnVehicle(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return null; }
        return rent.returnVehicle(asset);
    }


    // RESERVE METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>
    public ArrayList<Vehicle> availableVehicles(){ return rsvd.getAvailableVehicles(); }

    public boolean isAvailable(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        return rsvd.isAvailableForPeriod(asset, currentDate, 1); }

    public ArrayList<Booking> activeRentals(){ return rsvd.getActiveRentals(); }

    public ArrayList<Booking> reservations(){ return rsvd.getReservations(); }

    public boolean willBeAvailable(String identifier, Date pickupDate, int duration){
        Vehicle asset = inventory.findVehicle(identifier);
        return rsvd.isAvailableForPeriod(asset, pickupDate, duration);
    }

    public Booking reserve(String identifier, Renter renter, Date pickupDate, int duration){
            Vehicle asset = inventory.findVehicle(identifier);
            return rsvd.reserveVehicle(asset, renter, pickupDate, duration);
        }

    public boolean cancelReservation(String reservationID){ return rsvd.cancelReservation(reservationID); }

    public Booking activate(String reservationID){ return rsvd.activateReservation(reservationID); }

    public ArrayList<Vehicle> getAvailableVehicles(Date pickupDate, int duration)
        { return rsvd.getAvailableVehicles(pickupDate, duration); }


    // ADDITIONAL METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>
    public Inventory getInventory(){
        return inventory;
    }
}
