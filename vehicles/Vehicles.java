package vehicles;

import java.util.ArrayList;

abstract class Vehicle{
    private String assetNo;
    private String year;
    private String maker;
    private String model;
    private String vehicleType;
    private int mileage;
    // May change from "String" to a "Service" class which uses "Dates"
    private ArrayList<String> history = new ArrayList<>();
                    // maybe add gas tank size so we can determine gas charges.

    // Methods -----------------------------------------------------------------
    public String getID(){ return assetNo; }

    public String getYear(){ return year; }

    public String getMaker(){ return maker; }

    public String getModel(){ return model; }

    public String getType(){ return vehicleType; }

    public int getMileage(){ return mileage; }

    public String getHistory(){ return history.get(history.size() - 1); }

    public String getAllHistory(){
        // do later
    }
}