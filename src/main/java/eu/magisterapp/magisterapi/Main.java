package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
            System.out.println(api.getAccount().getNaam());
            System.out.println(api.getAccount().getId());

            LocalDate van = Utils.now();
            LocalDate tot = Utils.now();

            AfspraakList afspraken = api.getAfspraken(van, tot, false);

            for (Afspraak afspraak : afspraken)
                System.out.println(afspraak.getVakken() + " -> " + afspraak.getLokalen());


            // Dit dumpt je rooster vanaf 2015-10-23 (maandag) tot en met 2015-10-30 (vrijdag)
            // in dit formaat:

            // Engels - K202                 -> Normale naam (Vakken.Naam)
            // kwt - null                    -> Beschrijving (er is geen Vakken.Naam) -> null omdat er geen lokaal is (ook op magister niet)
            // s_lo - Kext                   -> Beschrijving (er is geen Vakken.Naam)
            // acv - WOL - sA51acv - K012    -> Beschrijving (Vakken.Naam > 20 tekens en dat is kut)



        }

        catch(ParseException | BadResponseException e)
        {
            // asdf
        }

    }
}
