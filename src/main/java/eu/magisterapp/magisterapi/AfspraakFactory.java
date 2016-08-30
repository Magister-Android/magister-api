package eu.magisterapp.magisterapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import eu.magisterapp.magisterapi.afwijkingen.AfwijkingInterface;
import eu.magisterapp.magisterapi.afwijkingen.ZernikeAfwijking;

/**
 * Created by max on 23-10-15.
 */
public class AfspraakFactory extends ArrayList<Afspraak> implements Iterable<Afspraak>
{
    private static Map<String, AfwijkingInterface> afwijkingen = new HashMap<>();

    /*
    * Later zullen hier ook methods bij komen om van een SQLite resultset een AfspraakList te maken.
    */

    public static AfspraakList convert(List<Afspraak> afspraken)
    {
        return new AfspraakList(afspraken);
    }

    public static AfspraakList make(Response response, String school, Aanmelding jaarlaag) throws ParseException
    {
        JSONArray json = response.getJson().getJSONArray("Items");

        AfspraakList list = new AfspraakList();

        if (json.length() == 0) return list;

        AfwijkingInterface fixer = getAfwijking(school, jaarlaag);

        for (int i = 0; i < json.length(); i++)
        {
            Afspraak a = new Afspraak(json.getJSONObject(i));

            if (fixer != null) fixer.modify(a);

            list.add(a);
        }

        return list;
    }

    private static AfwijkingInterface getAfwijking(String school, Aanmelding jaarlaag)
    {
        AfwijkingInterface a;

        if ((a = afwijkingen.get(school)) != null) return a;

        switch (school)
        {
            case "zernike":
                a = new ZernikeAfwijking(jaarlaag);
                break;
        }

        if (a != null) afwijkingen.put(school, a);

        return a;
    }
}
