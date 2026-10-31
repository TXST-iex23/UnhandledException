package tests;

import java.time.LocalDate;

import common.Date;
import common.Inventory;
import common.Renter;
import common.Rental;
import common.vehicles.Car;
import common.vehicles.Motorcycle;
import common.vehicles.Vehicle;

/**
 * Small, dependency-free checks for rental and input-validation behavior.
 * Run this class directly to stop at the first failed check.
 */
public class VehicleRentalValidationTest{
    /** Number of checks completed successfully so far. */
    private static int checks = 0;

    /** Runs all validation checks and prints the number that succeeded.
     *
     * @param args command-line arguments; this test driver does not use them
     */
    public static void main(String[] args){
        testInventoryAndRentals();
        testCustomerAndDateValidation();
        testReservations();
        System.out.println("Passed " + checks + " validation checks.");
    }

    /** Checks inventory, rental, return, pricing, and vehicle-history behavior. */
    private static void testInventoryAndRentals(){
        Inventory inventory = new Inventory();
        Car car = new Car(2022, "Toyota", "Camry", "C");
        inventory.addVehicle(car);
        String carID = car.getID();
        check(inventory.findVehicle(carID) == car, "vehicle receives a generated ID when added");
        check(carID.startsWith("C"), "generated car ID has the car prefix");

        Car duplicateIDCar = new Car(2023, "Honda", "Civic", "C");
        duplicateIDCar.setID(carID);
        inventory.addVehicle(duplicateIDCar);
        // The existing vehicle remains the match when an ID is duplicated.
        check(inventory.findVehicle(carID) == car, "duplicate ID is rejected");
        inventory.addVehicle(car);
        check(inventory.getAvailableVehicles().size() == 1,
              "duplicate vehicle is not added to inventory");
        inventory.addVehicle(null);
        check(inventory.getAvailableVehicles().size() == 1, "null vehicle is rejected");

        Vehicle unusedVehicle = new Car();
        check(unusedVehicle.getHistory().equals("No history available."),
              "empty vehicle history is safe to read");

        Renter renter = new Renter("Jamie", "Student");
        Vehicle carVehicle = car;
        Vehicle motorcycle = new Motorcycle(2024, "Honda", "Rebel", "M");
        inventory.addVehicle(motorcycle);
        String motorcycleID = motorcycle.getID();
        check(carVehicle.calculateRentalCost(2) != motorcycle.calculateRentalCost(2),
              "car and motorcycle use different pricing overrides through Vehicle methods");
        Rental motorcycleRental = inventory.rentVehicle(motorcycle, renter, 2);
        check(motorcycleRental != null && motorcycleRental.getTotalCharge() == 70.0,
              "motorcycle rental charge uses its $35 daily rate through a Vehicle reference");
        inventory.returnVehicle(motorcycleID);

        check(inventory.rentVehicle(car, renter, 0) == null,
              "zero-day rental is rejected");
        check(inventory.rentVehicle(car, renter, -1) == null,
              "negative-day rental is rejected");
        check(inventory.rentVehicle(null, renter, 1) == null,
              "null vehicle rental is rejected");
        check(inventory.rentVehicle(car, null, 1) == null,
              "null renter rental is rejected");
        check(inventory.returnVehicle(carID) == null,
              "return without an active rental is safe");

        Rental rental = inventory.rentVehicle(car, renter, 2);
        // A two-day car rental uses the Car pricing override: $50 per day.
        check(rental != null, "valid rental succeeds");
        check(rental.getTotalCharge() == 100.0, "car rental charge is correct");
        check(!inventory.isAvailable(carID), "rented vehicle is unavailable");
        check(inventory.rentVehicle(car, renter, 1) == null,
              "vehicle with active rental cannot be rented again");

        inventory.removeVehicle(carID);
        // Removal is ignored while the vehicle is part of an active rental.
        check(inventory.findVehicle(carID) == car,
              "vehicle with active rental cannot be removed");
        check(inventory.returnVehicle("UNKNOWN") == null,
              "unknown vehicle return is safe");

        Rental returnedRental = inventory.returnVehicle(carID);
        check(returnedRental == rental && !rental.isActive(),
              "active rental can be returned");
        check(inventory.isAvailable(carID), "returned vehicle is available again");
        check(car.getHistory().contains("was returned"), "return is recorded in history");
        inventory.removeVehicle(carID);
        check(inventory.findVehicle(carID) == null,
              "vehicle can be removed after return");

        checkThrows(() -> new Rental(car, renter, 0),
                    "Rental constructor rejects zero days");
        checkThrows(() -> car.calculateRentalCost(-1),
                    "vehicle pricing rejects negative days");
    }

    /** Checks required customer information and valid calendar-date behavior. */
    private static void testCustomerAndDateValidation(){
        checkThrows(() -> new Renter(" ", "Student"),
                    "blank customer name is rejected");
        Renter renter = new Renter("Jamie", "Student");
        checkThrows(() -> renter.setName("Jamie", null),
                    "null customer name update is rejected");
        checkThrows(() -> renter.setDOB(null), "null date of birth is rejected");

        Date february29 = new Date(2024, 2, 29);
        Date march1 = new Date(2024, 3, 1);
        // Consecutive dates must differ by exactly one day, including leap day.
        check(march1.getDateCode() - february29.getDateCode() == 1,
              "leap-day calculation is correct");
        checkThrows(() -> new Date(1900, 2, 29),
                    "invalid non-leap date is rejected");
        checkThrows(() -> new Date(2024, 13, 1), "invalid month is rejected");
    }

    /** Checks reservation validation, date conflicts, and lifecycle changes. */
    private static void testReservations(){
        Inventory inventory = new Inventory();
        Car firstCar = new Car(2022, "Toyota", "Camry", "C");
        Car secondCar = new Car(2023, "Honda", "Civic", "C");
        inventory.addVehicle(firstCar);
        inventory.addVehicle(secondCar);
        String firstCarID = firstCar.getID();
        String secondCarID = secondCar.getID();
        Renter renter = new Renter("Jamie", "Student");
        Date firstPickup = dateAfter(5);

        check(inventory.reserveVehicle("UNKNOWN", renter, firstPickup, 3) == null,
              "unknown vehicle cannot be reserved");
        check(inventory.reserveVehicle(firstCarID, null, firstPickup, 3) == null,
              "reservation requires a renter");
        check(inventory.reserveVehicle(firstCarID, renter, null, 3) == null,
              "reservation requires a pickup date");
        check(inventory.reserveVehicle(firstCarID, renter, dateAfter(-1), 3) == null,
              "past pickup date is rejected");
        check(inventory.reserveVehicle(firstCarID, renter, firstPickup, 0) == null,
              "zero-day reservation is rejected");
        check(inventory.reserveVehicle(firstCarID, renter, firstPickup, -1) == null,
              "negative-day reservation is rejected");
        check(inventory.getReservations().isEmpty(),
              "invalid reservation requests leave the list unchanged");

        Rental first = inventory.reserveVehicle(firstCarID, renter, firstPickup, 3);
        check(first != null && first.getReservationID().equals("R0001"),
              "valid reservation is created after invalid requests");
        check(first.getLifecycleState() == Rental.Status.RESERVED,
              "new reservation starts in reserved state");
        check(!inventory.getAvailableVehicles(firstPickup, 3).contains(firstCar) &&
              inventory.getAvailableVehicles(firstPickup, 3).contains(secondCar),
              "period availability excludes only the reserved vehicle");

        int savedPickupCode = first.getPickupDate().getDateCode();
        firstPickup.setDate(2000, 1, 1);
        Date returnedPickup = first.getPickupDate();
        returnedPickup.setDate(2000, 1, 1);
        check(first.getPickupDate().getDateCode() == savedPickupCode,
              "reservation keeps a protected copy of its pickup date");

        check(inventory.reserveVehicle(firstCarID, renter, dateAfter(7), 1) == null,
              "reservation sharing the final occupied day is rejected");
        check(inventory.getReservations().size() == 1 &&
              first.getLifecycleState() == Rental.Status.RESERVED,
              "overlap rejection leaves existing reservation unchanged");

        Rental adjacent = inventory.reserveVehicle(firstCarID, renter, dateAfter(8), 2);
        check(adjacent != null, "adjacent reservation periods are allowed");
        check(adjacent.getReservationID().equals("R0002"),
              "adjacent reservation receives the next ID");

        check(inventory.cancelReservation(first.getReservationID()),
              "pending reservation can be cancelled");
        check(first.getLifecycleState() == Rental.Status.CANCELLED,
              "cancellation changes the reservation state");
        Rental replacement = inventory.reserveVehicle(firstCarID, renter, dateAfter(5), 3);
        check(replacement != null, "cancellation frees the reserved dates");
        check(!inventory.cancelReservation("NO-SUCH-RESERVATION"),
              "unknown reservation ID is rejected");
        check(!inventory.cancelReservation(first.getReservationID()),
              "cancelled reservation cannot be cancelled again");
        check(first.getLifecycleState() == Rental.Status.CANCELLED &&
              inventory.getReservations().size() == 2,
              "invalid cancellation leaves current reservation state unchanged");

        Rental future = inventory.reserveVehicle(secondCarID, renter, dateAfter(1), 2);
        check(future != null, "second vehicle can be reserved for a future date");
        check(inventory.activateReservation("UNKNOWN") == null,
              "unknown reservation cannot be activated");
        check(inventory.activateReservation(future.getReservationID()) == null,
              "reservation cannot be activated before its pickup date");
        check(future.getLifecycleState() == Rental.Status.RESERVED &&
              inventory.getReservations().contains(future),
              "early activation leaves reservation pending");
        check(inventory.cancelReservation(future.getReservationID()),
              "future reservation can be cancelled");

        Rental today = inventory.reserveVehicle(secondCarID, renter, Date.today(), 2);
        check(today != null, "today is accepted as a pickup date");
        check(!inventory.isAvailable(secondCarID),
              "reservation starting today makes vehicle unavailable today");
        check(inventory.rentVehicle(secondCar, renter, 1) == null,
              "immediate rental cannot overlap a pending reservation");

        Rental activated = inventory.activateReservation(today.getReservationID());
        check(activated == today && today.isActive(),
              "reservation activates on its pickup date");
        check(today.getLifecycleState() == Rental.Status.ACTIVE &&
              !inventory.getReservations().contains(today),
              "activated reservation leaves the pending list");
        check(inventory.reserveVehicle(secondCarID, renter, dateAfter(10), 1) == null,
              "active rental prevents another reservation");
        check(!inventory.cancelReservation(today.getReservationID()),
              "active rental cannot be cancelled as a reservation");

        Rental returned = inventory.returnVehicle(secondCarID);
        check(returned == today && today.getLifecycleState() == Rental.Status.RETURNED,
              "existing return process completes an activated reservation");
        check(!today.isActive() && inventory.isAvailable(secondCarID),
              "returned activated reservation releases the vehicle");
        check(inventory.returnVehicle(secondCarID) == null,
              "returned rental cannot be returned again");
        check(!inventory.cancelReservation(today.getReservationID()) &&
              today.getLifecycleState() == Rental.Status.RETURNED,
              "returned reservation cannot be cancelled");
        check(inventory.activateReservation(today.getReservationID()) == null,
              "returned reservation cannot be activated again");
    }

    /** Makes a valid date relative to today for reservation checks. */
    private static Date dateAfter(int days){
        LocalDate date = LocalDate.now().plusDays(days);
        return new Date(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    /**
     * Records one successful test condition.
     *
     * @param passed whether the condition succeeded
     * @param description a short explanation used if the check fails
     */
    private static void check(boolean passed, String description){
        if(!passed){ throw new AssertionError(description); }
        checks++;
    }

    /**
     * Checks that an operation rejects invalid input with an argument exception.
     *
     * @param action the operation to run
     * @param description a short explanation used if no exception is thrown
     */
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
