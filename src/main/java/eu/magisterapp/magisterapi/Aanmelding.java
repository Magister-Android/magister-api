package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by max on 2-12-15.
 */
public class Aanmelding extends Module
{

    // idk wat dit is lol
    public final boolean AanBronMelden;

    // Datums
    public final LocalDate Start, Einde;

    // Id's
    public final Integer Id, LeerlingId;

    // Profiel shit
    public final String LesPeriode, Profiel, Profiel2;

    // Omschrijvingen
    public final String GroepOmschrijving, StudieOmschrijving;

    public Aanmelding(JSONObject aanmelding) throws ParseException
    {
        // idk
        AanBronMelden = getNullableBoolean(aanmelding, "AanBronMelden");

        // Datums
        Start = getNullableDate(aanmelding, "Start");
        Einde = getNullableDate(aanmelding, "Einde");

        // Id's
        Id = getNullableInt(aanmelding, "Id");
        LeerlingId = getNullableInt(aanmelding, "LeerlingId");

        // Profiel shit
        LesPeriode = getNullableString(aanmelding, "LesPeriode");
        Profiel = getNullableString(aanmelding, "Profiel");
        Profiel2 = getNullableString(aanmelding, "Profiel2");

        // Omschrijvingen
        JSONObject groep, studie;
        groep = aanmelding.getJSONObject("Groep");
        studie = aanmelding.getJSONObject("Studie");

        GroepOmschrijving = getNullableString(groep, "Omschrijving");
        StudieOmschrijving = getNullableString(studie, "Omschrijving");
    }

    public String getOmschrijving()
    {
        if (GroepOmschrijving != null) return GroepOmschrijving;

        return StudieOmschrijving;
    }
}
