package common; // may Need to change when if class moves

public class Date{
    private final String[] MONTHS = {"","January", "February", "March",
                                     "April","May","June",
                                     "July","August","September",
                                     "October","November","December"};

    private final int[] DAYSOFMONTHS = {0,31,28,31,30,31,30,31,31,30,31,30,31};

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

    // METHODS  =================================================================
    private void setDateCode(int annual, int lunar, int daily){
        final int STARTYEAR = 1900;
        int totaldays = -1;
        totaldays += (annual - STARTYEAR)*365;
        totaldays += perYear(lunar, daily);
        totaldays += (annual-1-STARTYEAR)/4;
        if(lunar > 2 || (lunar == 2 && daily == 29)) totaldays += 1;
        dateCode = totaldays;
    }
    private int perYear(int lunar, int daily){
        int days = 0;
        for(int i=1; i < lunar; i++){
            days += DAYSOFMONTHS[i];
        }
        days += daily;
        return days;
    }

    // public -------------------------------------------------------------------
    public void setDate(int annual, int lunar, int daily){
        // can poterntially add date validation program later
        year = annual;
        month = lunar;
        day = daily;
        setDateCode(annual, lunar, daily);
    }

    public String getDate() { return "" + month + "/" + day + "/" + year; }
    public String getDateString () { return "" + MONTHS[month] +" "+ day + ", " + year; }
    public int getDateCode() { return dateCode; }
    public String toString() { return getDateString (); }
}