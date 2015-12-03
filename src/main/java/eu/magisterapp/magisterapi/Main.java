package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;

import java.util.Iterator;

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
            LocalDate tot = Utils.deltaDays(1);

            AfspraakCollection afspraken = api.getAfspraken(van, tot);

            Iterator<AfspraakCollection> it = afspraken.dayIterator();

            while (it.hasNext())
            {
                AfspraakCollection dag = it.next();

                System.out.println(dag.getFirstDay().toString("EEEE"));
                // EEEE = dag (text, voluit geschreven), E = 1e 3 letters. Altijd in engels.
                // Gebruik android.text.format.DateUtils.formatDateTime voor goede taal.
                System.out.println("---------------------------");

                for (Afspraak afspraak : dag)
                {
                    System.out.println(String.format("%2d: %-15s -> %4s", afspraak.LesuurTotMet, afspraak.getVakken(), afspraak.getLokalen()));
                }

                System.out.println();
            }

            System.out.println("\n\nCijfers\n-------");

             CijferList cijfers = api.getCijfers();

            for (Cijfer cijfer : cijfers)
            {
                System.out.println(String.format("%25s (%4s): %4s", cijfer.Vak.omschrijving, cijfer.Vak.docent, cijfer.CijferStr));
            }

        }

        catch(Exception e)
        {
            System.out.println("Oeps..");
            e.printStackTrace();
        }

    }
}
