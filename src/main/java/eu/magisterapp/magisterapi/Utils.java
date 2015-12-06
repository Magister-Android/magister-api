package eu.magisterapp.magisterapi;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
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

    private final static DateTimeFormatter magisterToDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'kk:mm:ss.SSSSSSS'Z'");

    /**
     *
     * @param date De datum zoals hij in de magister response staat
     * @return De date in een Date object
     * @throws ParseException
     */
    public static LocalDateTime stringToDate(String date) throws ParseException
    {
        return LocalDateTime.parse(date, magisterToDateFormatter);
    }

    public static Float parseFloat(String kutfloat)
    {
        try
        {
            // kut magister gebruikt kommas ipv .
            return Float.parseFloat(kutfloat.replace(',', '.'));
        }

        catch (NumberFormatException e)
        {
            return null;
        }
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

    public static String dateToString(LocalDateTime date)
    {
        return date.toString("yyyy-MM-dd");
    }

    /**
     * Geef de begin datum (maandag) van een week waar een gegeven datum in valt.
     * @param date de datum die in die week zit
     * @return Date de maandag van die week
     */
    public static LocalDateTime getStartOfWeek(String date)
    {
        LocalDateTime day = new LocalDateTime(date);
        return day.withDayOfWeek(DateTimeConstants.MONDAY);
    }

    public static LocalDateTime getStartOfWeek()
    {
        return LocalDateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);
    }

    /**
     * Geef de laatste dag (vrijdag) van een week waar een gegeven datum in valt.
     * @param date de datum
     * @return LocalDateTime de vrijdag in die week
     */
    public static LocalDateTime getEndOfWeek(String date)
    {
        LocalDateTime day = new LocalDateTime(date);
        return day.withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static LocalDateTime getEndOfWeek()
    {
        return LocalDateTime.now().withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static LocalDateTime now()
    {
        return LocalDateTime.now();
    }

    public static LocalDateTime deltaDays(int days)
    {
        return LocalDateTime.now().plusDays(days);
    }
}
