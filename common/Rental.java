package common;

import common.vehicles.Vehicle;

public class Rental{
    private Vehicle vehicle;
    private Renter renter;
    private int duration;
    private boolean active;

    public Rental(Vehicle rentedVehicle, Renter customer, int rentalDuration){
        vehicle = rentedVehicle;
        renter = customer;
        duration = rentalDuration;
        active = true;
    }

    public Vehicle getVehicle(){ return vehicle; }
    public Renter getRenter(){ return renter; }
    public int getDuration(){ return duration; }
    public boolean isActive(){ return active; }
    public String getStatus(){ return active ? "Active" : "Returned"; }

    void markReturned(){ active = false; }
}
