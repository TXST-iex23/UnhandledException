package common.vehicles;

/**
 * A car is a concrete kind of {@link Vehicle} with a $50 daily rental rate.
 * It inherits the shared vehicle fields and methods from its superclass.
 */
public class Car extends Vehicle{
    /* Signifies a car via the identifier */
    private String vehicleType = "C";

    /**
     * Creates a car from a text model year, manufacturer, and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Car(String yr, String mkr, String mdl, String vType){
        super(yr, mkr, mdl,vType);
    }

    /**
     * Creates a car from a numeric model year, manufacturer, and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Car(int yr, String mkr, String mdl,String vType){
        super(yr, mkr, mdl,vType);
    }

    /** Creates a car with the superclass's placeholder values. */
    public Car (){
        super();
        String temp = super.getID();
        temp = vehicleType + temp.substring(1);
        super.setID(temp);
    }

    /**
     * Overrides the superclass pricing method with the car's daily rate.
     * A call made through a {@code Vehicle} reference still uses this method
     * when the actual object is a {@code Car}.
     *
     * @param rentalDays the positive rental duration
     * @return the total cost at $50 per day
     * @throws IllegalArgumentException if the duration is not positive
     */
    @Override
    public double calculateRentalCost(int rentalDays){
        // Reject invalid durations before applying this vehicle type's daily rate.
        if(rentalDays <= 0){
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        // Multiplying by the duration keeps pricing behavior tied to the Car subclass.
        return 50.0 * rentalDays;
    }
}
