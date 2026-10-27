package common.vehicles;

import java.util.ArrayList;

/**
 * Common superclass for rentable vehicles.
 * Shared vehicle information is stored once here, while subclasses override
 * {@link #calculateRentalCost(int)} to provide their own prices.
 */
public abstract class Vehicle{
    /** The inventory ID assigned to this vehicle. */
    private String assetNo;
    /** The model year, stored as text to support the existing constructor forms. */
    private String year;
    /** The vehicle manufacturer. */
    private String maker;
    /** The vehicle model name. */
    private String model;
    /** An optional descriptive type for the vehicle. */
    private String vehicleType;
    /** The vehicle's mileage. */
    private int mileage;
    /** Messages describing inventory, rental, return, and other vehicle events. */
    private ArrayList<String> history;

    /** Creates a vehicle with placeholder details and an empty history. */
    public Vehicle(){
        assetNo = "undefined";
        year = "undefined";
        maker = "undefined";
        model = "undefined";
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }

    /**
     * Creates a vehicle from a text model year and its make and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Vehicle(String yr, String mkr, String mdl){
        assetNo = "undefined";
        year = yr;
        maker = mkr;
        model = mdl;
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }

    /**
     * Creates a vehicle from a numeric model year and its make and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Vehicle(int yr, String mkr, String mdl){
        assetNo = "undefined";
        year = String.valueOf(yr);
        maker = mkr;
        model = mdl;
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }

    /** @return the inventory ID */
    public String getID(){ return assetNo; }
    /** @return the model year */
    public String getYear(){ return year; }
    /** @return the manufacturer */
    public String getMaker(){ return maker; }
    /** @return the model name */
    public String getModel(){ return model; }
    /** @return the descriptive vehicle type */
    public String getType(){ return vehicleType; }
    /** @return the mileage */
    public int getMileage(){ return mileage; }

    /**
     * Returns the latest history message, or a safe message when there are none.
     *
     * @return the latest event in the vehicle's history
     */
    public String getHistory(){
        if(history.isEmpty()){ return "No history available."; }
        return history.get(history.size() - 1);
    }
    /**
     * Joins all vehicle history messages in the order they were recorded.
     *
     * @return the complete history, or a safe message when it is empty
     */
    public String getAllHistory(){
        if(history.isEmpty()){ return "No history available."; }
        StringBuilder allHistory = new StringBuilder();
        // A line separator keeps each event readable on the current operating system.
        for(String entry : history){
            if(allHistory.length() > 0){ allHistory.append(System.lineSeparator()); }
            allHistory.append(entry);
        }
        return allHistory.toString();
    }

    /** @param id the inventory ID to store */
    public void setID(String id){ assetNo = id; }
    /** @param yr the model year to store */
    public void setYear(String yr){ year = yr; }
    /** @param mkr the manufacturer to store */
    public void setMaker(String mkr){ maker = mkr; }
    /** @param mdl the model name to store */
    public void setModel(String mdl){ model = mdl; }
    /** @param vType the descriptive vehicle type to store */
    public void setType(String vType){ vehicleType = vType; }
    /** @param miles the mileage to store */
    public void setMileage(int miles){ mileage = miles; }

    /**
     * Adds an event message to this vehicle's history.
     *
     * @param input the event message to record
     */
    public void record(String input){ history.add(input); }

    /**
     * Calculates a rental charge for this vehicle type.
     * Each concrete subclass supplies its own rate by overriding this method.
     *
     * @param rentalDays the positive rental duration
     * @return the total rental cost
     * @throws IllegalArgumentException if the duration is not positive
     */
    public abstract double calculateRentalCost(int rentalDays);

}
