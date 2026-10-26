package common; // may Need to change when if class moves

public class Date{
    private final String[] MONTHS = {"","January", "February", "March",
                                     "April","May","June",
                                     "July","August","September",
                                     "October","November","December"};

    private int year;
    private int month;
    private int day;
    private int dateCode; // used for count day;

    public Date() {
        year = 1900;
        month = 1;
        day = 1;
        dateCode = 0;
    }

    public Date(int annual, int lunar, int daily) {
        setDate(annual, lunar, daily);
    }

    public void setDate(int annual, int lunar, int daily){
        // can poterntially add date validation program later
        year = annual;
        month = lunar;
        day = daily;
    }

    public String getDate() {
        return "" + month + "/" + day + "/" + year;
    }

    public String getDateString () {
        return "" + MONTHS[month] +" "+ day + ", " + year;
    }

    public String toString() {
        return getDateString ();
    }
}