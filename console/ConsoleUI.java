package console;

import java.util.ArrayList;
import java.util.Scanner;

import common.Inventory;
import common.Renter;
import common.Rental;
import common.vehicles.Vehicle;

public class ConsoleUI{
    private Inventory inventory;
    private Scanner input;

    public ConsoleUI(Inventory rentalInventory){
        inventory = rentalInventory;
        input = new Scanner(System.in);
    }

    public void run(){
        boolean running = true;
        while(running){
            System.out.println("\nVehicle Rental System");
            System.out.println("1. View available vehicles");
            System.out.println("2. Rent a vehicle");
            System.out.println("3. Return a vehicle");
            System.out.println("4. View active rentals");
            System.out.println("5. Exit");

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
                    running = false;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Please choose a number from 1 to 5.");
            }
        }
    }

    private void showAvailableVehicles(){
        ArrayList<Vehicle> vehicles = inventory.getAvailableVehicles();
        if(vehicles.isEmpty()){
            System.out.println("There are no available vehicles.");
            return;
        }

        System.out.println("Available vehicles:");
        for(Vehicle vehicle : vehicles){
            System.out.println(vehicle.getID() + " - " + vehicle.getYear() + " " +
                vehicle.getMaker() + " " + vehicle.getModel() + " ($" +
                String.format("%.2f", vehicle.calculateRentalCost(1)) + " per day)");
        }
    }

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

        String firstName = readLine("Enter renter first name: ");
        if(firstName == null){ return; }
        String lastName = readLine("Enter renter last name: ");
        if(lastName == null){ return; }
        Integer duration = readInteger("Enter rental duration in days: ");
        if(duration == null){ return; }
        if(duration <= 0){
            System.out.println("Rental duration must be greater than zero.");
            return;
        }

        Renter renter = new Renter(firstName.trim(), lastName.trim());
        Rental rental = inventory.rentVehicle(vehicle, renter, duration);
        if(rental == null){
            System.out.println("The rental could not be created.");
        } else {
            System.out.println("Rental created. Total charge: $" +
                String.format("%.2f", rental.getTotalCharge()));
        }
    }

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

    private void showActiveRentals(){
        ArrayList<Rental> rentals = inventory.getActiveRentals();
        if(rentals.isEmpty()){
            System.out.println("There are no active rentals.");
            return;
        }

        System.out.println("Active rentals:");
        for(Rental rental : rentals){
            Vehicle vehicle = rental.getVehicle();
            System.out.println(vehicle.getID() + " - " + vehicle.getMaker() + " " +
                vehicle.getModel() + ", renter: " + rental.getRenter().getName() +
                ", days: " + rental.getDuration() + ", charge: $" +
                String.format("%.2f", rental.getTotalCharge()));
        }
    }

    private Integer readInteger(String prompt){
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

    private String readLine(String prompt){
        System.out.print(prompt);
        if(!input.hasNextLine()){ return null; }
        return input.nextLine();
    }
}
