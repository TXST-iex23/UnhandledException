// Currently here just to test subclassess
import common.*;
import common.vehicles.*;

public class Main{
    public static void main(String[] args){

        // Testing Date Class
        Date defaultDate = new Date();
        System.out.println(defaultDate.getDate());
        System.out.println(defaultDate.getDateString());
        System.out.println(defaultDate.getDateCode());

        Date nextDate = new Date(2026,12,25);
        System.out.println(nextDate.getDate());
        System.out.println(nextDate.getDateString());
        System.out.println(nextDate.getDateCode());

        // Testing Car Class
        Vehicle car1 = new Car("2003","Chevy","Cavalier" );
        Vehicle car2 = new Car(2003,"Chevy","Cavalier" );

        print("");
        print(car1.getMaker());
        print(car2.getYear());
        print(car1.getModel());
        print("Mileage: " + String.valueOf(car2.getMileage()));

        // Testing Renter Class
        print("");
        Renter guy1 = new Renter();
        Renter gal1 = new Renter("Billy", "Jean");
        gal1.setDOB(new Date(1980,2,14));

        print(guy1.getName());
        guy1.setName("Michael","Jackson");
        print(guy1.getName());
        print(gal1.getName() + "  --  " + gal1.getDOB());

        // Testing Add/Remove from Inventory Class
        print("");
        Inventory store1 = new Inventory("My backyard");
        store1.addVehicle(car1,"C0001");
        store1.addVehicle(car2,"C0002");
        print(car1.getHistory());
        print(car2.getHistory());
        store1.removeVehicle("C0001");
        print(car1.getHistory());
        Date dayIn = new Date(2020,3,20);
        Date dayOut = new Date(2020,3,15);
        print("Day count: " + store1.cntDays(dayOut, dayIn ));

        // Testing vehicle rentals and returns
        print("");
        Rental successfulRental = store1.rentVehicle(car2, gal1, 3);
        print("Rental successful: " + (successfulRental != null));

        Rental duplicateRental = store1.rentVehicle(car2, guy1, 2);
        print("Duplicate rental rejected: " + (duplicateRental == null));

        Rental returnedRental = store1.returnVehicle("C0002");
        print("Return successful: " +
              (returnedRental != null && !returnedRental.isActive()));
        print(car2.getHistory());



    }

    public static void print(String someString) { System.out.println(someString); }
}
