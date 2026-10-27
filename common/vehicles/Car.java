package common.vehicles;

public class Car extends Vehicle{
    public Car(String yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }
    public Car(int yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }
    public Car (){
        super();
    }

    @Override
    public double calculateRentalCost(int rentalDays){
        if(rentalDays <= 0){
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        return 50.0 * rentalDays;
    }
}
