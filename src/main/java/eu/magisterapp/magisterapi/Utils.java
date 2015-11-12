package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max on 12-11-15.
 */
public class Utils {

    private static DateTimeFormatter magisterToDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'kk:mm:ss.SSSSSSS'Z'");

    /**
     *
     * @param date De datum zoals hij in de magister response staat
     * @return De date in een Date object
     * @throws ParseException
     */
    public static LocalDate stringToDate(String date) throws ParseException
    {
        return LocalDate.parse(date, magisterToDateFormatter);
    }

    /**
     *
     * @param date Een Date object
     * @return De date object naar een string om in een GET request te gebruiken
     */
    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String dateToString(LocalDate date)
    {
        return date.toString("yyyy-MM-dd");
    }

    /**
     * Geef de begin datum (maandag) van een week waar een gegeven datum in valt.
     * @param date de datum die in die week zit
     * @return Date de maandag van die week
     */
    public static LocalDate getStartOfWeek(String date)
    {
        LocalDate day = new LocalDate(date);
        return day.withDayOfWeek(DateTimeConstants.MONDAY);
    }

    public static LocalDate getStartOfWeek()
    {
        return LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY);
    }

    /**
     * Geef de laatste dag (vrijdag) van een week waar een gegeven datum in valt.
     * @param date de datum
     * @return LocalDate de vrijdag in die week
     */
    public static LocalDate getEndOfWeek(String date)
    {
        LocalDate day = new LocalDate(date);
        return day.withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static LocalDate getEndOfWeek()
    {
        return LocalDate.now().withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static LocalDate now()
    {
        return LocalDate.now();
    }

    public static LocalDate deltaDays(int days)
    {
        return LocalDate.now().plusDays(days);
    }
}
