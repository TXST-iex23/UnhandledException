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

    }
}