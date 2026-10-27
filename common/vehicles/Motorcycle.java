package common.vehicles;

/**
 * A motorcycle is a concrete {@link Vehicle} with a $35 daily rental rate.
 * It inherits common vehicle data and overrides the pricing behavior.
 */
public class Motorcycle extends Vehicle{
    /**
     * Creates a motorcycle from a text model year, manufacturer, and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Motorcycle(String yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }

    /**
     * Creates a motorcycle from a numeric model year, manufacturer, and model.
     *
     * @param yr the model year
     * @param mkr the manufacturer
     * @param mdl the model name
     */
    public Motorcycle(int yr, String mkr, String mdl){
        super(yr, mkr, mdl);
    }

    /** Creates a motorcycle with the superclass's placeholder values. */
    public Motorcycle(){
        super();
    }

    /**
     * Overrides the superclass pricing method with the motorcycle's daily rate.
     * Java selects this version at runtime when the vehicle object is a motorcycle.
     *
     * @param rentalDays the positive rental duration
     * @return the total cost at $35 per day
     * @throws IllegalArgumentException if the duration is not positive
     */
    @Override
    public double calculateRentalCost(int rentalDays){
        // Each subclass validates its own pricing input before calculating a charge.
        if(rentalDays <= 0){
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        // This override supplies the motorcycle-specific rate through Vehicle polymorphism.
        return 35.0 * rentalDays;
    }
}
