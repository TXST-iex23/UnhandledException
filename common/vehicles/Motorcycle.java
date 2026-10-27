package common.vehicles;

public class Motorcycle extends Vehicle{
    public Motorcycle(String yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }

    public Motorcycle(int yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }

    public Motorcycle(){
        super();
    }

    @Override
    public double calculateRentalCost(int rentalDays){
        if(rentalDays <= 0){
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        return 35.0 * rentalDays;
    }
}
