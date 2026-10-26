package vehicles;

import java.util.ArrayList;

public abstract class Vehicle{
    private String assetNo;
    private String year;
    private String maker;
    private String model;
    private String vehicleType;
    private int mileage;
    // May change from "String" to a "Service" class which uses "Dates"
    private ArrayList<String> history;
                    // maybe add gas tank size so we can determine gas charges.

    // METHODS -----------------------------------------------------------------
    public Vehicle(){
        assetNo = "undefined";
        year = "undefined";
        maker = "undefined";
        model = "undefined";
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }
    public Vehicle(String yr, String mkr, String mdl){
        assetNo = "undefined";
        year = yr;
        maker = mkr;
        model = mdl;
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }
    public Vehicle(int yr, String mkr, String mdl){
        assetNo = "undefined";
        year = String.valueOf(yr);
        maker = mkr;
        model = mdl;
        vehicleType = "undefined";
        mileage = 0;
        history = new ArrayList<>();
    }

    // getters -----------------------------------------------------------------
    public String getID(){ return assetNo; }

    public String getYear(){ return year; }

    public String getMaker(){ return maker; }

    public String getModel(){ return model; }

    public String getType(){ return vehicleType; }

    public int getMileage(){ return mileage; }

    public String getHistory(){ return history.get(history.size() - 1); }

    public String getAllHistory(){ return "TBD"; }

    // setters -----------------------------------------------------------------
    public void setID(){ //.add code;
        }

    public void setYear(String yr){ year = yr; }

    public void setMaker(String mkr){ maker = mkr; }

    public void setModel(String mdl){ model = mdl; }

    public void setType(String vType){ vehicleType = vType; }

    public void setMileage(int miles){ mileage = miles; }

    public void record(String input){ history.add(input); }

}