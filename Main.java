// Currently here just to test subclassess
import vehicles.*;

public class Main{
    public void main(String args[]){

        // Testing Date Class
        Date defaultDate = new Date();
        System.out.println(defaultDate.getDate());
        System.out.println(defaultDate.getDateString());

        Date nextDate = new Date(2026,12,25);
        System.out.println(nextDate.getDate());
        System.out.println(nextDate.getDateString());

        // Testing Car Class
        Car car1 = new Car("2003","Chevy","Cavalier" );
        Car car2 = new Car(2003,"Chevy","Cavalier" );

        print("");
        print(car1.getMaker());
        print(car2.getYear());
        print(car1.getModel());
        print("Mileage: " + String.valueOf(car2.getMileage()));
    }

    public void print(String someString) { System.out.println(someString); }
}