package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by max on 2-12-15.
 */
public class Vak extends Module
{
    public final Integer id;
    public final Integer studieVakId;
    public final Integer studieId;
    public final String afkorting; // inf
    public final String omschrijving; // informatica
    public final boolean vrijstelling;
    public final boolean dispensatie;
    public final Integer volgnr;
    public final String docent;
    public final LocalDate begindatum;
    public final LocalDate einddatum;
    public final boolean hogerNiveau;


    // Alles nog een keer, omdat magister fcking debiel is.
    public final Integer Id;
    public final Integer StudieVakId;
    public final Integer StudieId;
    public final String Afkorting; // inf
    public final String Omschrijving; // informatica
    public final boolean Vrijstelling;
    public final boolean Dispensatie;
    public final Integer Volgnr;
    public final String Docent;
    public final LocalDate Begindatum;
    public final LocalDate Einddatum;
    public final boolean HogerNiveau;


    public Vak(JSONObject vak) throws ParseException
    {
        Id = id = getNullableInt(vak, "id");
        StudieVakId = studieVakId = getNullableInt(vak, "studieVakId");
        StudieId = studieId = getNullableInt(vak, "studieId");
        Afkorting = afkorting = getNullableString(vak, "afkorting");
        Omschrijving = omschrijving = getNullableString(vak, "omschrijving");
        Vrijstelling = vrijstelling = getNullableBoolean(vak, "vrijstelling");
        Dispensatie = dispensatie = getNullableBoolean(vak, "dispensatie");
        Volgnr = volgnr = getNullableInt(vak, "volgnr");
        Docent = docent = getNullableString(vak, "docent");
        Begindatum = begindatum = getNullableDate(vak, "begindatum");
        Einddatum = einddatum = getNullableDate(vak, "einddatum");
        HogerNiveau = hogerNiveau = getNullableBoolean(vak, "hogerNiveau");
    }
}
