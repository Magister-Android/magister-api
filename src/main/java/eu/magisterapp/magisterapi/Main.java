package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by max on 18-10-15.
 */
public class Main {

    public static void main(String[] args)
    {
        MagisterAPI api = new MagisterAPI("zernike", Credentials.username, Credentials.password);

        try
        {
            System.out.println(api.getAccount().getNaam());
            System.out.println(api.getAccount().getId());

            LocalDate van = Utils.now();
            LocalDate tot = Utils.deltaDays(7);

            AfspraakCollection afspraken = api.getAfspraken(van, tot);

            Iterator<AfspraakCollection> it = afspraken.dayIterator();

            while (it.hasNext())
            {
                AfspraakCollection dag = it.next();

                System.out.println(DateFormatSymbols.getInstance(Locale.ENGLISH).getWeekdays()[dag.getFirstDay() + 1]);
                System.out.println("---------------------------");

                for (Afspraak afspraak : dag)
                {
                    System.out.println(String.format("%2d: %-15s -> %4s", afspraak.LesuurTotMet, afspraak.getVakken(), afspraak.getLokalen()));
                }

                System.out.println();
            }

        }

        catch(ParseException | BadResponseException e)
        {
            // asdf
        }

    }
}
