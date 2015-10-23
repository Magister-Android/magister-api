package eu.magisterapp.magisterapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class Main {

    public static void main(String[] args)
    {
        MagisterAPI api = new MagisterAPI("zernike", Credentials.username, Credentials.password);

        try
        {
            // kut java
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date van = format.parse("2015-10-26");
            Date tot = format.parse("2015-10-31");

            AfspraakList afspraken = api.getAfspraken(van, tot);
            System.out.println(afspraken.size());

            // Vanaf hier is het testen. Afspraak.isOp werkt nog niet lekker
            afspraken.parseRemaining();

            Date maandag = format.parse("2015-10-26"); // maandag aanstaande

            Map<Integer, Afspraak> voorMaandag = afspraken.getDay(maandag);
            System.out.println(voorMaandag.size()); // 0, dat zou (in mijn geval) 4 moeten zijn.. = lessen op maandag

            voorMaandag.forEach(
                (i, afspraak) -> System.out.println(afspraak.getDocenten())
            );
        }

        catch(ParseException | BadResponseException e)
        {
            // asdf
        }

    }
}
