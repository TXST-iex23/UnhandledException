package common;

import common.features.*;
import common.features.Inventory;
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
    public String getLotName(String agencyLocation){ return inventory.getLotName(); }
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
        if(rsvd.isAvailable(identifier) && !isRsvd){ inventory.removeVehicle(identifier); }
    }

    public ArrayList<Vehicle> list(){ return  inventory.list(); }
    public void importList(ArrayList<Vehicle> lotStock){ inventory.importList(lotStock); }



    // RENT METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>
    public void rentVehicle(Vehicle asset, Renter renter, int duration) {
        String identifier = asset.getID();
        if(identifier == null || inventory.findVehicle(identifier) != asset ||
        !rsvd.isAvailableForPeriod(identifier, currentDate, duration)) { return; }
        rent.rentVehicle(asset,renter,duration);
    }

    public void returnVehicle(String identifier){
        Vehicle asset = inventory.findVehicle(identifier);
        // An unknown ID cannot have an active rental in this inventory.
        if(asset == null){ return; }
        rent.returnVehicle(asset);
    }


    // RESERVE METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>


}
