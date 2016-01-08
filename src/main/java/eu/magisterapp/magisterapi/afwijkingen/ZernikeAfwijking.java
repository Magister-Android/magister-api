package eu.magisterapp.magisterapi.afwijkingen;

import eu.magisterapp.magisterapi.Aanmelding;
import eu.magisterapp.magisterapi.Afspraak;

/**
 * Created by max on 4-1-16.
 */
public class ZernikeAfwijking implements AfwijkingInterface {

    private int jaar;
    private boolean doModify;

    public ZernikeAfwijking(Aanmelding jaarlaag)
    {
        // StudieOmschrijving = KL-sA5 of KL-mH4.. laatste teken is altijd het jaar.
        jaar = Integer.parseInt(jaarlaag.StudieOmschrijving.substring(jaarlaag.StudieOmschrijving.length() - 1));

        doModify = jaarlaag.StudieOmschrijving.startsWith("KL");
    }

    @Override
    public void modify(Afspraak afspraak) {
        if ((! doModify ) || afspraak.LesuurTotMet == null || afspraak.LesuurVan == null || jaar == 0) return;

        if (jaar > 4) modifyBovenbouw(afspraak);

        else modifyOnderbouw(afspraak);
    }

    public void modifyOnderbouw(Afspraak afspraak)
    {
        switch (afspraak.LesuurVan)
        {
            case 7:
                afspraak.Start = afspraak.Start.withTime(14, 20, 0, 0);
                break;

            case 8:
                afspraak.Start = afspraak.Start.withTime(15, 20, 0, 0);
                break;
        }



        switch (afspraak.LesuurTotMet)
        {
            case 7:
                afspraak.Einde = afspraak.Einde.withTime(15, 20, 0, 0);
                break;

            case 8:
                afspraak.Einde = afspraak.Einde.withTime(16, 20, 0, 0);
                break;
        }
    }

    public void modifyBovenbouw(Afspraak afspraak)
    {
        switch (afspraak.LesuurVan)
        {
            case 3:
                afspraak.Start = afspraak.Start.withTime(9, 30, 0, 0);
                break;

            case 4:
                afspraak.Start = afspraak.Start.withTime(10, 50, 0, 0);
                break;

            case 5:
                afspraak.Start = afspraak.Start.withTime(11, 50, 0, 0);
                break;

            case 7:
                afspraak.Start = afspraak.Start.withTime(14, 20, 0, 0);
                break;

            case 8:
                afspraak.Start = afspraak.Start.withTime(15, 20, 0, 0);
                break;
        }

        switch (afspraak.LesuurTotMet)
        {
            case 3:
                afspraak.Einde = afspraak.Einde.withTime(10, 30, 0, 0);
                break;

            case 4:
                afspraak.Einde = afspraak.Einde.withTime(11, 50, 0, 0);
                break;

            case 5:
                afspraak.Einde = afspraak.Einde.withTime(12, 50, 0, 0);
                break;

            case 7:
                afspraak.Einde = afspraak.Einde.withTime(15, 20, 0, 0);
                break;

            case 8:
                afspraak.Einde = afspraak.Einde.withTime(16, 20, 0, 0);
                break;
        }
    }
}
