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
    private int calculateDateCode(int annual, int lunar, int daily){
        final int STARTYEAR = 1900;
        long daysBeforeThisYear = (long)(annual - STARTYEAR) * 365;
        daysBeforeThisYear += leapYearsBefore(annual) - leapYearsBefore(STARTYEAR);
        long code = daysBeforeThisYear + perYear(lunar, daily) - 1;
        if(lunar > 2 && isLeapYear(annual)){ code++; }
        if(code < Integer.MIN_VALUE || code > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Date is outside the supported range.");
        }
        return (int)code;
    }

    private int leapYearsBefore(int annual){
        int year = annual - 1;
        return year / 4 - year / 100 + year / 400;
    }

    private boolean isLeapYear(int annual){
        return annual % 4 == 0 && (annual % 100 != 0 || annual % 400 == 0);
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
        if(annual < 1 || lunar < 1 || lunar > 12){
            throw new IllegalArgumentException("Date has an invalid year or month.");
        }
        int maxDay = DAYSOFMONTHS[lunar];
        if(lunar == 2 && isLeapYear(annual)){ maxDay++; }
        if(daily < 1 || daily > maxDay){
            throw new IllegalArgumentException("Date has an invalid day.");
        }
        int newDateCode = calculateDateCode(annual, lunar, daily);
        year = annual;
        month = lunar;
        day = daily;
        dateCode = newDateCode;
    }

    public String getDate() { return "" + month + "/" + day + "/" + year; }
    public String getDateString () { return "" + MONTHS[month] +" "+ day + ", " + year; }
    public int getDateCode() { return dateCode; }
    public String toString() { return getDateString (); }
}
