package common;

import common.features.*;
import common.features.vehicles.*;
import java.util.ArrayList;

/**
 * Manages the vehicles at one rental location and keeps rental records.
 * This class contains application logic; the console menu is handled elsewhere.
 */
public class BookingManager {
    /** The inventory from which bookings are made. */
    private Inventory inventory;
    /** The rent feature which creates bookings. */
    private Rent rent;
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

    // PUBLIC METHODS -------------------------------------------------------------------

    // INVENTORY METHODS  <><><><><><><><<><><><><><><><><><><><><><>
    public Vehicle find(String identifier){ return inventory.findVehicle(identifier); }
    public void add(Vehicle asset){ inventory.addVehicle(asset); }
    public void remove(String identifier){ inventory.removeVehicle(identifier); }
    public ArrayList<Vehicle> list(){ return  inventory.list(); }
    public void importList(ArrayList<Vehicle> lotStock){ inventory.importList(lotStock); }
    public String getLotName(String agencyLocation){ return inventory.getLotName(); }
    public void setLotName(String agencyLocation){ inventory.setLotName(agencyLocation); }

    // RENT METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>
    public ArrayList<Vehicle> available(){ return rent.getAvailableVehicles(); }
    public void rentVehicle(Vehicle asset, Renter renter, int duration) { rent.rentVehicle(asset,renter,duration); }
    public void returnVehicle(String identifier){ rent.returnVehicle(identifier); }

    // RESERVE METHODS <><><><><><><><<><><><><><><><><><><><><><><><><>


}
