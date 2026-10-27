import common.Inventory;
import common.vehicles.Car;
import common.vehicles.Motorcycle;
import console.ConsoleUI;

public class Main{
    public static void main(String[] args){
        Inventory inventory = new Inventory("Main Office");
        inventory.addVehicle(new Car(2022, "Toyota", "Camry"), "C0001");
        inventory.addVehicle(new Car(2023, "Honda", "Civic"), "C0002");
        inventory.addVehicle(new Motorcycle(2024, "Honda", "Rebel"), "M0001");

        ConsoleUI console = new ConsoleUI(inventory);
        console.run();
    }
}
