package common;
import java.util.ArrayList;

import common.vehicles.*;

public class Inventory{
    private String agencyLocation;
    private ArrayList<Vehicle> lotStock = new ArrayList<>();
    private ArrayList<Rental> rentals = new ArrayList<>();

    public Inventory() { agencyLocation = "Original Lot"; }
    public Inventory(String lotLocation) { agencyLocation = lotLocation; }

    // METHODS  -------------------------------------------------------------
    // PRIVATE **************************************************************
        private int getIndexByID(String identifier){
        if(identifier == null){ return -1; }
        String searchID = identifier.trim();
        if(searchID.isEmpty()){ return -1; }
        int cnt = lotStock.size();
        for(int i = 0; i < cnt; i++){
            if(searchID.equals(lotStock.get(i).getID())){ return i; }
        }
        return -1;
    }

    // PUBLIC **************************************************************
    public void addVehicle(Vehicle asset, String identifier){
        if(asset == null || identifier == null || identifier.trim().isEmpty()){ return; }
        String vehicleID = identifier.trim();
        if(getIndexByID(vehicleID) >= 0 || findVehicleByObject(asset)){ return; }
        asset.setID(vehicleID);
        asset.record(vehicleID + " was added to the inventory.");
        lotStock.add(asset);
    }

    private boolean findVehicleByObject(Vehicle asset){
        for(Vehicle vehicle : lotStock){
            if(vehicle == asset){ return true; }
        }
        return false;
    }

    public Vehicle findVehicle(String identifier){
        int index = getIndexByID(identifier);
        if(index < 0){ return null; }
        return lotStock.get(index);
    }

    public ArrayList<Vehicle> getAvailableVehicles(){
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        for(Vehicle asset : lotStock){
            if(isAvailable(asset.getID())){ availableVehicles.add(asset); }
        }
        return availableVehicles;
    }

    public ArrayList<Rental> getActiveRentals(){
        ArrayList<Rental> activeRentals = new ArrayList<>();
        for(Rental rental : rentals){
            if(rental.isActive()){ activeRentals.add(rental); }
        }
        return activeRentals;
    }

    public boolean isAvailable(String identifier){
        Vehicle asset = findVehicle(identifier);
        if(asset == null){ return false; }

        for(Rental rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){ return false; }
        }
        return true;
    }

    public Rental rentVehicle(Vehicle asset, Renter renter, int duration){
        if(asset == null || renter == null || duration <= 0){ return null; }
        String identifier = asset.getID();
        if(identifier == null || findVehicle(identifier) != asset ||
           !isAvailable(identifier)){ return null; }

        Rental rental = new Rental(asset, renter, duration);
        rentals.add(rental);
        asset.record(asset.getID() + " was rented by " + renter.getName() +
                     " for " + duration + " days.");
        return rental;
    }

    public Rental returnVehicle(String identifier){
        Vehicle asset = findVehicle(identifier);
        if(asset == null){ return null; }

        for(Rental rental : rentals){
            if(rental.isActive() && rental.getVehicle() == asset){
                rental.markReturned();
                asset.record(asset.getID() + " was returned by " +
                             rental.getRenter().getName() + ".");
                return rental;
            }
        }
        return null;
    }

    public void removeVehicle(String identifier){
        int index = getIndexByID(identifier);
        if(index >= 0 && isAvailable(identifier)){
            lotStock.get(index).record(identifier + " was removed from the inventory.");
            lotStock.remove(index);
        }
    }

    public int cntDays(Date checkout, Date checkin){
        return checkin.getDateCode() - checkout.getDateCode();
    }
}
