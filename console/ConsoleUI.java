package console;

import java.util.ArrayList;
import java.util.Scanner;

import common.Inventory;
import common.Renter;
import common.Rental;
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
            System.out.println("5. Administrator options");
            System.out.println("6. Exit");

            Integer choice = readInteger("Choose an option: ");
            if(choice == null){ break; }

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
                    adminMenu();
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

    /** Displays a vehicles history. */
    private void showVehicleHistory() {
        ArrayList<Vehicle> vehicles = inventory.getAvailableVehicles();
        if(vehicles.isEmpty()){
            System.out.println("There are no vehicles to assess.");
            return;
        }
        String identifier = readLine("Enter the vehicle ID to assess.  ");
        Vehicle vehicle = inventory.findVehicle(identifier.trim());
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
                    System.out.println("\nNot yet programmed in");
                    break;
                case 2:
                    System.out.println("\nNot yet programmed in");
                    break;
                case 3:
                    showVehicleHistory();
                    break;
                default:
                    // A number outside the menu range returns to top menu.
                    runMenu2 = false;
                    break;
            }
        }
    }
}
