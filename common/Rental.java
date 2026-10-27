package common;

import common.vehicles.Vehicle;

public class Rental{
    private Vehicle vehicle;
    private Renter renter;
    private int duration;
    private boolean active;

    public Rental(Vehicle rentedVehicle, Renter customer, int rentalDuration){
        if(rentedVehicle == null || customer == null){
            throw new IllegalArgumentException("Vehicle and renter are required.");
        }
        if(rentalDuration <= 0){
            throw new IllegalArgumentException("Rental duration must be greater than zero.");
        }
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
    public double getTotalCharge(){ return vehicle.calculateRentalCost(duration); }

    void markReturned(){ active = false; }
}
