package console;

import java.util.ArrayList;
import java.util.Scanner;

import common.Date;
import common.Inventory;
import common.Renter;
import common.Rental;
import common.vehicles.Car;
import common.vehicles.Motorcycle;
import common.vehicles.Vehicle;

/**
 * Reads user choices and displays results for the vehicle rental application.
 * Rental rules are delegated to {@link Inventory}, keeping terminal input and
 * output separate from the application's core logic.
 */
public class ConsoleUI{
    /** The application inventory that this menu operates on. */
    private Inventory inventory;
    /** Reads complete lines so invalid text can be handled without input conversion errors. */
    private Scanner input;

    /**
     * Creates the console menu for an existing inventory.
     *
     * @param rentalInventory the inventory used by the menu
     */
    public ConsoleUI(Inventory rentalInventory){
        inventory = rentalInventory;
        input = new Scanner(System.in);
    }

    /**
     * Displays the menu repeatedly until the user chooses Exit or input ends.
     * Invalid menu text is handled by {@link #readInteger(String)}.
     */
    public void run(){
        boolean running = true;
        // The loop keeps the menu available after each completed operation.
        while(running){
            System.out.println("\nVehicle Rental System: "+ inventory.CurrentDateString());
            System.out.println("Lot: "+ inventory.getLotName() +"\n");
            System.out.println("1. View available vehicles");
            System.out.println("2. Rent a vehicle");
            System.out.println("3. Return a vehicle");
            System.out.println("4. View active rentals");
            System.out.println("5. Reservations and administrator options");
            System.out.println("6. Exit");

            Integer choice = readInteger("Choose an option: ");
            if(choice == null){
                System.out.println("Input ended. Goodbye.");
                break;
            }

            switch(choice){
                case 1:
                    showAvailableVehicles();
                    break;
                case 2:
                    rentVehicle();
                    break;
                case 3:
                    returnVehicle();
                    break;
                case 4:
                    showActiveRentals();
                    break;
                case 5:
                    running = toolsMenu();
                    break;
                case 6:
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    // A number outside the menu range does not stop the program.
                    System.out.println("Please choose a number from 1 to 6.");
            }
        }
    }

    /** Displays all vehicles that Inventory reports as available. */
    private void showAvailableVehicles(){
        ArrayList<Vehicle> vehicles = inventory.getAvailableVehicles();
        if(vehicles.isEmpty()){
            System.out.println("There are no available vehicles.");
            return;
        }

        System.out.println("Available vehicles:");
        // The shared Vehicle reference calls the correct subclass pricing override.
        for(Vehicle vehicle : vehicles){
            System.out.println(vehicle.getID() + " - " + vehicle.getYear() + " " +
                vehicle.getMaker() + " " + vehicle.getModel() + " ($" +
                String.format("%.2f", vehicle.calculateRentalCost(1)) + " per day)");
        }
    }

    /**
     * Collects rental details and asks Inventory to begin the rental.
     * Invalid IDs and unavailable vehicles are rejected before customer details
     * are collected.
     */
    private void rentVehicle(){
        String identifier = readLine("Enter the vehicle ID: ");
        if(identifier == null){ return; }

        Vehicle vehicle = inventory.findVehicle(identifier.trim());
        if(vehicle == null){
            System.out.println("That vehicle ID was not found.");
            return;
        }
        if(!inventory.isAvailable(identifier.trim())){
            System.out.println("That vehicle is already rented.");
            return;
        }

        String firstName = readRequiredText("Enter renter first name: ");
        if(firstName == null){ return; }
        String lastName = readRequiredText("Enter renter last name: ");
        if(lastName == null){ return; }
        Integer duration = readPositiveInteger("Enter rental duration in days: ");
        if(duration == null){ return; }

        Renter renter = new Renter(firstName.trim(), lastName.trim());
        // Inventory performs the final availability and duration checks as well.
        Rental rental = inventory.rentVehicle(vehicle, renter, duration);
        if(rental == null){
            System.out.println("The rental could not be created.");
        } else {
            System.out.println("Rental created. Total charge: $" +
                String.format("%.2f", rental.getTotalCharge()));
        }
    }

    /** Asks Inventory to complete the active rental for a vehicle ID. */
    private void returnVehicle(){
        String identifier = readLine("Enter the vehicle ID to return: ");
        if(identifier == null){ return; }

        Rental rental = inventory.returnVehicle(identifier.trim());
        if(rental == null){
            System.out.println("No active rental was found for that vehicle ID.");
        } else {
            System.out.println("Vehicle returned successfully.");
        }
    }

    /** Displays current rentals and their charges. */
    private void showActiveRentals(){
        ArrayList<Rental> rentals = inventory.getActiveRentals();
        if(rentals.isEmpty()){
            System.out.println("There are no active rentals.");
            return;
        }

        System.out.println("Active rentals:");
        // Rental calculates each total through its vehicle's pricing method.
        for(Rental rental : rentals){
            Vehicle vehicle = rental.getVehicle();
            System.out.println(vehicle.getID() + " - " + vehicle.getMaker() + " " +
                vehicle.getModel() + ", renter: " + rental.getRenter().getName() +
                ", days: " + rental.getDuration() + ", charge: $" +
                String.format("%.2f", rental.getTotalCharge()));
        }
    }

    /** Displays reservation actions until the user returns to the main menu. */
    private boolean reservationMenu(){
        while(true){
            System.out.println("\n*** Reservations ***");
            System.out.println("1. Create a reservation");
            System.out.println("2. View pending reservations");
            System.out.println("3. Cancel a reservation");
            System.out.println("4. Activate a reservation for pickup");
            System.out.println("5. Return to the main menu");

            Integer choice = readInteger("Choose an option: ");
            if(choice == null){
                System.out.println("Input ended. Goodbye.");
                return false;
            }

            switch(choice){
                case 1:
                    if(!createReservationPrompt()){
                        System.out.println("Input ended. Goodbye.");
                        return false;
                    }
                    break;
                case 2:
                    showReservations();
                    break;
                case 3:
                    if(!cancelReservationPrompt()){
                        System.out.println("Input ended. Goodbye.");
                        return false;
                    }
                    break;
                case 4:
                    if(!activateReservationPrompt()){
                        System.out.println("Input ended. Goodbye.");
                        return false;
                    }
                    break;
                case 5:
                    return true;
                default:
                    System.out.println("Please choose a number from 1 to 5.");
            }
        }
    }

    /** Collects reservation details and saves only a valid, available request. */
    private boolean createReservationPrompt(){
        Date pickupDate = readPickupDate();
        if(pickupDate == null){ return false; }

        Integer duration = readPositiveInteger("Enter rental duration in days: ");
        if(duration == null){ return false; }

        ArrayList<Vehicle> available = inventory.getAvailableVehicles(pickupDate, duration);
        if(available.isEmpty()){
            System.out.println("No vehicles are available for that entire period.");
            return true;
        }

        System.out.println("Vehicles available for pickup on " + pickupDate.getDateString() +
            " for " + duration + " day(s):");
        for(Vehicle vehicle : available){
            System.out.println(vehicle.getID() + " - " + vehicle.getYear() + " " +
                vehicle.getMaker() + " " + vehicle.getModel() + " ($" +
                String.format("%.2f", vehicle.calculateRentalCost(1)) + " per day)");
        }

        Vehicle selectedVehicle = null;
        while(selectedVehicle == null){
            String identifier = readRequiredText("Enter the vehicle ID to reserve: ");
            if(identifier == null){ return false; }

            Vehicle candidate = inventory.findVehicle(identifier);
            if(candidate == null){
                System.out.println("That vehicle ID was not found.");
            } else if(!inventory.isAvailableForPeriod(identifier, pickupDate, duration)){
                System.out.println("That vehicle is not available for the entire requested period.");
            } else {
                selectedVehicle = candidate;
            }
        }

        String firstName = readRequiredText("Enter renter first name: ");
        if(firstName == null){ return false; }
        String lastName = readRequiredText("Enter renter last name: ");
        if(lastName == null){ return false; }

        Renter renter = new Renter(firstName, lastName);
        Rental reservation = inventory.reserveVehicle(selectedVehicle.getID(), renter,
                                                       pickupDate, duration);
        if(reservation == null){
            System.out.println("The reservation could not be created because that period is no longer available.");
        } else {
            System.out.println("Reservation " + reservation.getReservationID() +
                " created for " + selectedVehicle.getID() + " on " +
                reservation.getPickupDate().getDateString() + ".");
        }
        return true;
    }

    /** Reads a valid, non-past date in MM/DD/YYYY form. */
    private Date readPickupDate(){
        while(true){
            String value = readLine("Enter pickup date (MM/DD/YYYY): ");
            if(value == null){ return null; }
            String[] parts = value.trim().split("/", -1);
            if(parts.length != 3){
                System.out.println("Enter a valid date in MM/DD/YYYY format, such as 12/31/2027.");
                continue;
            }

            try{
                int month = Integer.parseInt(parts[0].trim());
                int day = Integer.parseInt(parts[1].trim());
                int year = Integer.parseInt(parts[2].trim());
                Date pickupDate = new Date(year, month, day);
                if(pickupDate.getDateCode() < Date.today().getDateCode()){
                    System.out.println("Pickup date cannot be in the past.");
                    continue;
                }
                return pickupDate;
            } catch(IllegalArgumentException exception){
                System.out.println("Enter a valid date in MM/DD/YYYY format, such as 12/31/2027.");
            }
        }
    }

    /** Displays pending reservations, if any. */
    private void showReservations(){
        ArrayList<Rental> reservations = inventory.getReservations();
        if(reservations.isEmpty()){
            System.out.println("There are no pending reservations.");
            return;
        }

        System.out.println("Pending reservations:");
        for(Rental reservation : reservations){
            Vehicle vehicle = reservation.getVehicle();
            System.out.println(reservation.getReservationID() + " - " + vehicle.getID() +
                " " + vehicle.getMaker() + " " + vehicle.getModel() +
                ", renter: " + reservation.getRenter().getName() +
                ", pickup: " + reservation.getPickupDate().getDateString() +
                ", days: " + reservation.getDuration());
        }
    }

    /** Cancels a pending reservation by its reservation ID. */
    private boolean cancelReservationPrompt(){
        String reservationID = readRequiredText("Enter reservation ID to cancel: ");
        if(reservationID == null){ return false; }
        if(inventory.cancelReservation(reservationID)){
            System.out.println("Reservation " + reservationID + " cancelled.");
        } else {
            System.out.println("No pending reservation with that ID was found.");
        }
        return true;
    }

    /** Activates a pending reservation only on its pickup date. */
    private boolean activateReservationPrompt(){
        String reservationID = readRequiredText("Enter reservation ID to activate: ");
        if(reservationID == null){ return false; }

        Rental reservation = findPendingReservation(reservationID);
        if(reservation == null){
            System.out.println("No pending reservation with that ID was found.");
            return true;
        }
        Date pickupDate = reservation.getPickupDate();
        if(pickupDate.getDateCode() != Date.today().getDateCode()){
            System.out.println("Reservation " + reservationID + " can only be activated on " +
                pickupDate.getDateString() + ".");
            return true;
        }

        Rental activeRental = inventory.activateReservation(reservationID);
        if(activeRental == null){
            System.out.println("The reservation could not be activated.");
        } else {
            System.out.println("Reservation " + reservationID + " is now an active rental.");
        }
        return true;
    }

    /** Finds a pending reservation for displaying a useful activation message. */
    private Rental findPendingReservation(String reservationID){
        for(Rental reservation : inventory.getReservations()){
            if(reservation.getReservationID().equals(reservationID.trim())){
                return reservation;
            }
        }
        return null;
    }

    /** Displays a vehicles history. */
    private void showVehicleHistory() {
        String identifier = readRequiredText("Enter the vehicle ID to assess: ");
        if(identifier == null){ return; }
        Vehicle vehicle = inventory.findVehicle(identifier);
        if(vehicle == null){
            System.out.println("\nThat vehicle ID was not found.");
            return;
        }
        else{
            System.out.println("\n" + identifier + "'s Vehicle History:");
            System.out.println(vehicle.getAllHistory());
            return;
        }
    }

    /**
     * Reads and parses an integer, prompting again when the input is not numeric.
     *
     * @param prompt the message shown before reading input
     * @return the parsed integer, or {@code null} when input has ended
     */
    private Integer readInteger(String prompt){
        // Retrying keeps malformed text from reaching the menu's numeric logic.
        while(true){
            String value = readLine(prompt);
            if(value == null){ return null; }
            try{
                return Integer.parseInt(value.trim());
            } catch(NumberFormatException exception){
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /**
     * Reads an integer until it is greater than zero.
     *
     * @param prompt the message shown before reading input
     * @return a positive integer, or {@code null} when input has ended
     */
    private Integer readPositiveInteger(String prompt){
        while(true){
            Integer value = readInteger(prompt);
            if(value == null){ return null; }
            if(value > 0){ return value; }
            System.out.println("Rental duration must be greater than zero.");
        }
    }

    /**
     * Reads a required text value and prompts again if it is blank.
     *
     * @param prompt the message shown before reading input
     * @return trimmed nonblank text, or {@code null} when input has ended
     */
    private String readRequiredText(String prompt){
        while(true){
            String value = readLine(prompt);
            if(value == null){ return null; }
            if(!value.trim().isEmpty()){ return value.trim(); }
            System.out.println("This value cannot be blank.");
        }
    }

    /**
     * Displays a prompt and reads one line without converting it.
     *
     * @param prompt the message shown to the user
     * @return the entered line, or {@code null} at end of input
     */
    private String readLine(String prompt){
        System.out.print(prompt);
        if(!input.hasNextLine()){ return null; }
        return input.nextLine();
    }

    /** Asks Inventory to complete the active rental for a vehicle ID. */
    private void adminMenu(){
        boolean runMenu2 = true;
        // The loop keeps the menu available after each completed operation.
        while(runMenu2){
            System.out.println("\n*** Administrator Menu ***");
            System.out.println("Lot: "+ inventory.getLotName() +"\n");
            System.out.println("1. Add a vehicle");
            System.out.println("2. Remove a vehicle");
            System.out.println("3. View vehicle history");
            System.out.println("4. Return to previous menu.");

            Integer choice = readInteger("Choose an option: ");
            if(choice == null){ break; }

            switch(choice){
                case 1:
                    addVehiclePrompt();
                    break;
                case 2:
                    removeVehiclePrompt();
                    break;
                case 3:
                    showVehicleHistory();
                    break;
                case 4:
                    runMenu2 = false;
                    break;
                default:
                    System.out.println("Please choose a number from 1 to 4.");
            }
        }
    }

    private void addVehiclePrompt(){
        String vType;
        Integer choice = readInteger("Is the new vehicle a Car (1) or motorcycle (2)? ");
        if(choice == null){ return; }
        if(choice == 1){vType = "C";}
        else if(choice == 2){vType = "M";}
        else { return; }

        String maker = readLine("Enter the new vehicle's maker: ");
        if(maker == null){ return; }

        String model = readLine("Enter the model of the vehicle: ");
        if(model == null){ return; }

        String year = readLine("Now enter the year of the vehicle: ");
        if(year == null){ return; }

        Vehicle vehicle = choice == 1 ? new Car(year, maker, model, vType) :
                                       new Motorcycle(year, maker, model, vType);
        inventory.addVehicle(vehicle);
        System.out.println("The vehicle was added as " + vehicle.getID() + " into the system.");
    }

    private void removeVehiclePrompt(){
        String id = readLine("Enter the ID of the vehicle to remove? ");
        if(id == null){ return; }
        Vehicle vehicle = inventory.findVehicle(id);
        if(vehicle == null){
            System.out.println("That vehicle ID was not found.");
        } else if(!inventory.isAvailable(id)){
            System.out.println("That vehicle cannot be removed while it has an active rental.");
        } else if(hasPendingReservation(vehicle)){
            System.out.println("That vehicle cannot be removed while it has a pending reservation.");
        } else {
            inventory.removeVehicle(id);
            System.out.println(id + " was removed from the system.");
        }
    }

    /** Checks whether a pending reservation depends on the vehicle. */
    private boolean hasPendingReservation(Vehicle vehicle){
        for(Rental reservation : inventory.getReservations()){
            if(reservation.getVehicle() == vehicle){ return true; }
        }
        return false;
    }

    /** Lets the user open either the reservation or administrator workflow. */
    private boolean toolsMenu(){
        while(true){
            System.out.println("\n*** Rental Management ***");
            System.out.println("1. Reservations");
            System.out.println("2. Administrator options");
            System.out.println("3. Return to the main menu");
            Integer choice = readInteger("Choose an option: ");
            if(choice == null){
                System.out.println("Input ended. Goodbye.");
                return false;
            }
            switch(choice){
                case 1:
                    if(!reservationMenu()){ return false; }
                    break;
                case 2:
                    adminMenu();
                    break;
                case 3:
                    return true;
                default:
                    System.out.println("Please choose a number from 1 to 3.");
            }
        }
    }
}
