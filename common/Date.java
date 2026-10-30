package common;

import java.time.LocalDate;

/**
 * Stores a calendar date and a day code that can be used to compare dates.
 * The day code counts days relative to January 1, 1900.
 */
public class Date{
    /** Month names indexed by the numeric month, with an empty entry at index zero. */
    private final String[] MONTHS = {"","January", "February", "March",
                                     "April","May","June",
                                     "July","August","September",
                                     "October","November","December"};

    /** Number of days in each month of a regular year, indexed by month number. */
    private final int[] DAYSOFMONTHS = {0,31,28,31,30,31,30,31,31,30,31,30,31};

    /** The calendar year. */
    private int year;
    /** The month number, from 1 through 12. */
    private int month;
    /** The day number within the month. */
    private int day;
    /** The number of days from January 1, 1900, used for date comparisons. */
    private int dateCode;

    /** Creates the default date January 1, 1900. */
    public Date() {
        year = 1900;
        month = 1;
        day = 1;
        dateCode = 0;
    }

    /**
     * Creates a date after checking that the year, month, and day are valid.
     *
     * @param annual the year, starting at year 1
     * @param lunar the month number, from 1 through 12
     * @param daily the day number within the month
     * @throws IllegalArgumentException if the date is invalid or outside the supported day-code range
     */
    public Date(int annual, int lunar, int daily) {
        setDate(annual, lunar, daily);
    }

    /**
     * Calculates the date's day code, including leap days before the date.
     *
     * @param annual the validated year
     * @param lunar the validated month number
     * @param daily the validated day number
     * @return the day code relative to January 1, 1900
     * @throws IllegalArgumentException if the result cannot fit in an integer
     */
    private int calculateDateCode(int annual, int lunar, int daily){
        final int STARTYEAR = 1900;
        long daysBeforeThisYear = (long)(annual - STARTYEAR) * 365;
        daysBeforeThisYear += leapYearsBefore(annual) - leapYearsBefore(STARTYEAR);
        long code = daysBeforeThisYear + perYear(lunar, daily) - 1;
        // A leap day belongs after February, so it shifts later dates forward.
        if(lunar > 2 && isLeapYear(annual)){ code++; }
        if(code < Integer.MIN_VALUE || code > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Date is outside the supported range.");
        }
        return (int)code;
    }

    /**
     * Counts Gregorian leap years before the supplied year.
     *
     * @param annual the year whose preceding leap years should be counted
     * @return the number of leap years before {@code annual}
     */
    private int leapYearsBefore(int annual){
        int year = annual - 1;
        return year / 4 - year / 100 + year / 400;
    }

    /**
     * Applies the Gregorian rule for leap years: divisible by four, except
     * century years that are not divisible by 400.
     *
     * @param annual the year to check
     * @return {@code true} if the year is a leap year
     */
    private boolean isLeapYear(int annual){
        return annual % 4 == 0 && (annual % 100 != 0 || annual % 400 == 0);
    }
    /**
     * Counts the days before a day within the year using regular month lengths.
     *
     * @param lunar the month number
     * @param daily the day number
     * @return the one-based day number within the year
     */
    private int perYear(int lunar, int daily){
        int days = 0;
        // Add each complete month before the requested month.
        for(int i=1; i < lunar; i++){
            days += DAYSOFMONTHS[i];
        }
        days += daily;
        return days;
    }

    /**
     * Validates and stores a new date, then calculates its day code.
     * Values are checked before fields are changed, so a failed update leaves
     * the existing date intact.
     *
     * @param annual the year, starting at year 1
     * @param lunar the month number, from 1 through 12
     * @param daily the day number valid for that month and year
     * @throws IllegalArgumentException if any part of the date is invalid
     */
    public void setDate(int annual, int lunar, int daily){
        // Validate the month before using it as an array index below.
        if(annual < 1 || lunar < 1 || lunar > 12){
            throw new IllegalArgumentException("Date has an invalid year or month.");
        }
        int maxDay = DAYSOFMONTHS[lunar];
        // February has one extra valid day in a leap year.
        if(lunar == 2 && isLeapYear(annual)){ maxDay++; }
        // Reject impossible days such as April 31 or February 29 in a common year.
        if(daily < 1 || daily > maxDay){
            throw new IllegalArgumentException("Date has an invalid day.");
        }
        int newDateCode = calculateDateCode(annual, lunar, daily);
        year = annual;
        month = lunar;
        day = daily;
        dateCode = newDateCode;
    }

    /**
     * Returns the date using numeric month/day/year notation.
     *
     * @return the date in {@code month/day/year} form
     */
    public String getDate() { return "" + month + "/" + day + "/" + year; }

    /**
     * Returns the date with the month written as a word.
     *
     * @return the date in a form such as {@code March 20, 2024}
     */
    public String getDateString () { return "" + MONTHS[month] +" "+ day + ", " + year; }

    /**
     * Returns this date's day code for comparing dates or calculating duration.
     *
     * @return the number of days relative to January 1, 1900
     */
    public int getDateCode() { return dateCode; }

    /** @return today's local calendar date */
    public static Date today(){
        LocalDate currentDate = LocalDate.now();
        return new Date(currentDate.getYear(), currentDate.getMonthValue(),
                        currentDate.getDayOfMonth());
    }

    /** @return an independent copy of this date */
    public Date copy(){ return new Date(year, month, day); }

    /**
     * Uses the readable date form when this object is printed.
     *
     * @return the long-form date string
     */
    public String toString() { return getDateString (); }
}
