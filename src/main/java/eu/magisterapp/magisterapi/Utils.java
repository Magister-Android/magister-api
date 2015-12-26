package eu.magisterapp.magisterapi;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max on 12-11-15.
 */
public class Utils {

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("yyyy-MM-dd'T'kk:mm:ss.SSSSSSS'Z'").withZone(DateTimeZone.UTC).getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd").getParser()
    };

    public static final DateTimeFormatter magisterToDateFormatter =
            new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    /**
     *
     * @param date De datum zoals hij in de magister response staat
     * @return De date in een Date object
     * @throws ParseException
     */
    public static DateTime stringToDate(String date) throws ParseException
    {
        return DateTime.parse(date, magisterToDateFormatter).withZone(DateTimeZone.getDefault());
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
    public static String dateToString(Date date)
    {
        return df.format(date);
    }

    public static String dateToString(DateTime date)
    {
        return date.toString("yyyy-MM-dd");
    }

    /**
     * Geef de begin datum (maandag) van een week waar een gegeven datum in valt.
     * @param date de datum die in die week zit
     * @return Date de maandag van die week
     */
    public static DateTime getStartOfWeek(String date)
    {
        DateTime day = new DateTime(date);
        return day.withDayOfWeek(DateTimeConstants.MONDAY);
    }

    public static DateTime getStartOfWeek()
    {
        return DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);
    }

    /**
     * Geef de laatste dag (vrijdag) van een week waar een gegeven datum in valt.
     * @param date de datum
     * @return DateTime de vrijdag in die week
     */
    public static DateTime getEndOfWeek(String date)
    {
        DateTime day = new DateTime(date);
        return day.withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static DateTime getEndOfWeek()
    {
        return DateTime.now().withDayOfWeek(DateTimeConstants.FRIDAY);
    }

    public static DateTime now()
    {
        return DateTime.now();
    }

    public static DateTime deltaDays(int days)
    {
        return DateTime.now().plusDays(days);
    }
}
