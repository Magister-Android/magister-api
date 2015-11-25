package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

/**
 * Created by max on 23-10-15.
 */
public class AfspraakFactory extends ArrayList<Afspraak> implements Iterable<Afspraak>
{
    public static AfspraakCollection fetch(MagisterConnection con, LocalDate van, LocalDate tot, boolean zonderUitval, Account account) throws ParseException
    {
        String url = URLS.afspraken(account, van, tot, zonderUitval);

        System.out.println(url);

        Response response = con.get(url);

        String body = response.getBody();

        JSONArray raw = new JSONObject(body).getJSONArray("Items");

        if (raw.length() == 0) return null;

        AfspraakCollection collection = new AfspraakCollection();

        for (int i = 0; i < raw.length(); i++)
            collection.add(new Afspraak(raw.getJSONObject(i)));

        return collection;
    }

    /*
    * Later zullen hier ook methods bij komen om van een SQLite resultset een AfspraakCollection te maken.
    */

    public static AfspraakCollection convert(List<Afspraak> afspraken)
    {
        return new AfspraakCollection(afspraken);
    }
}
