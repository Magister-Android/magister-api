package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by max on 23-10-15.
 */
public class AfspraakFactory extends ArrayList<Afspraak> implements Iterable<Afspraak>
{
    public static AfspraakCollection fetch(MagisterConnection con, Sessie sessie, String url) throws ParseException, IOException, JSONException
    {
        Response response = con.get(url, sessie);

        String body = response.getBody();

        JSONArray raw = new JSONObject(body).getJSONArray("Items");

        AfspraakCollection collection = new AfspraakCollection();

        if (raw.length() == 0) return collection;

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
