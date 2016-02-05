package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
    public final DateTime begindatum;
    public final DateTime einddatum;
    public final boolean hogerNiveau;


    // Alles nog een keer, omdat magister fcking debiel is.
    public final Integer Id;
    public final Integer StudieVakId;
    public final Integer StudieId;
    public final String Afkorting; // inf
    public final String Omschrijving; // informatica
    public final Boolean Vrijstelling;
    public final Boolean Dispensatie;
    public final Integer Volgnr;
    public final String Docent;
    public final DateTime Begindatum;
    public final DateTime Einddatum;
    public final Boolean HogerNiveau;

    private List<Float> cijfers = new ArrayList<>();


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

    public void addCijfer(Float cijfer, Integer weging)
    {
        if (cijfer == null || weging == null) return;

        for (int i = 0; i < weging; i++)
        {
            cijfers.add(cijfer);
        }
    }

    public Float getGemiddelde()
    {
        if (cijfers.size() == 0) return 0F;

        int som = 0;

        for (Float cijfer : cijfers)
        {
            som += cijfer * 10;
		}

		return som / (cijfers.size() * 10f);
    }
}
