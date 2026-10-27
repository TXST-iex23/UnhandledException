package tests;

import common.Date;
import common.Inventory;
import common.Renter;
import common.Rental;
import common.vehicles.Car;
import common.vehicles.Vehicle;

public class VehicleRentalValidationTest{
    private static int checks = 0;

    public static void main(String[] args){
        testInventoryAndRentals();
        testCustomerAndDateValidation();
        System.out.println("Passed " + checks + " validation checks.");
    }

    private static void testInventoryAndRentals(){
        Inventory inventory = new Inventory();
        Car car = new Car(2022, "Toyota", "Camry");
        inventory.addVehicle(car, " C0001 ");
        check(inventory.findVehicle("C0001") == car, "valid vehicle can be added");
        check(inventory.findVehicle("   ") == null, "blank vehicle ID is rejected");

        inventory.addVehicle(new Car(2023, "Honda", "Civic"), "C0001");
        check(inventory.findVehicle("C0001") == car, "duplicate ID is rejected");
        inventory.addVehicle(car, "C0002");
        check(inventory.findVehicle("C0002") == null, "same vehicle cannot be added twice");
        inventory.addVehicle(null, "C0003");
        check(inventory.findVehicle("C0003") == null, "null vehicle is rejected");

        Vehicle unusedVehicle = new Car();
        check(unusedVehicle.getHistory().equals("No history available."),
              "empty vehicle history is safe to read");

        Renter renter = new Renter("Jamie", "Student");
        check(inventory.rentVehicle(car, renter, 0) == null,
              "zero-day rental is rejected");
        check(inventory.rentVehicle(car, renter, -1) == null,
              "negative-day rental is rejected");
        check(inventory.rentVehicle(null, renter, 1) == null,
              "null vehicle rental is rejected");
        check(inventory.rentVehicle(car, null, 1) == null,
              "null renter rental is rejected");
        check(inventory.returnVehicle("C0001") == null,
              "return without an active rental is safe");

        Rental rental = inventory.rentVehicle(car, renter, 2);
        check(rental != null, "valid rental succeeds");
        check(rental.getTotalCharge() == 100.0, "car rental charge is correct");
        check(!inventory.isAvailable("C0001"), "rented vehicle is unavailable");
        check(inventory.rentVehicle(car, renter, 1) == null,
              "vehicle with active rental cannot be rented again");

        inventory.removeVehicle("C0001");
        check(inventory.findVehicle("C0001") == car,
              "vehicle with active rental cannot be removed");
        check(inventory.returnVehicle("UNKNOWN") == null,
              "unknown vehicle return is safe");

        Rental returnedRental = inventory.returnVehicle("C0001");
        check(returnedRental == rental && !rental.isActive(),
              "active rental can be returned");
        check(inventory.isAvailable("C0001"), "returned vehicle is available again");
        check(car.getHistory().contains("was returned"), "return is recorded in history");
        inventory.removeVehicle("C0001");
        check(inventory.findVehicle("C0001") == null,
              "vehicle can be removed after return");

        checkThrows(() -> new Rental(car, renter, 0),
                    "Rental constructor rejects zero days");
        checkThrows(() -> car.calculateRentalCost(-1),
                    "vehicle pricing rejects negative days");
    }

    private static void testCustomerAndDateValidation(){
        checkThrows(() -> new Renter(" ", "Student"),
                    "blank customer name is rejected");
        Renter renter = new Renter("Jamie", "Student");
        checkThrows(() -> renter.setName("Jamie", null),
                    "null customer name update is rejected");
        checkThrows(() -> renter.setDOB(null), "null date of birth is rejected");

        Date february29 = new Date(2024, 2, 29);
        Date march1 = new Date(2024, 3, 1);
        check(march1.getDateCode() - february29.getDateCode() == 1,
              "leap-day calculation is correct");
        checkThrows(() -> new Date(1900, 2, 29),
                    "invalid non-leap date is rejected");
        checkThrows(() -> new Date(2024, 13, 1), "invalid month is rejected");
    }

    private static void check(boolean passed, String description){
        if(!passed){ throw new AssertionError(description); }
        checks++;
    }

    private static void checkThrows(Runnable action, String description){
        try{
            action.run();
        } catch(IllegalArgumentException exception){
            checks++;
            return;
        }
        throw new AssertionError(description);
    }
}
