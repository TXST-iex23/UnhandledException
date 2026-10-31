import common.Inventory;
import common.vehicles.Car;
import common.vehicles.Motorcycle;
import console.ConsoleUI;

/**
 * Provides the entry point that prepares sample vehicles and starts the
 * console interface. Rental rules remain in application classes such as
 * {@code Inventory} and {@code Rental}.
 */
public class Main{
    /**
     * Builds the starting inventory and launches the console menu.
     *
     * @param args command-line arguments; this program does not use them
     */
    public static void main(String[] args){
        // The vehicles are stored as a group by Inventory and can be handled
        // through their shared Vehicle superclass in the rest of the program.
        Inventory inventory = new Inventory("Main Office");
        inventory.addVehicle(new Car(2022, "Toyota", "Camry"), "C0001");
        inventory.addVehicle(new Car(2023, "Honda", "Civic"), "C0002");
        inventory.addVehicle(new Motorcycle(2024, "Honda", "Rebel"), "M0001");

        ConsoleUI console = new ConsoleUI(inventory);
        console.run();
    }
}
