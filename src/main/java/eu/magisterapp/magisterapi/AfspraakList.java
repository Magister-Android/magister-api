package eu.magisterapp.magisterapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

/**
 * Created by max on 23-10-15.
 */
public class AfspraakList extends ArrayList<Afspraak>
{
    protected JSONArray raw;

    protected HashMap<Integer, Afspraak> parsed = new HashMap<>();

    public AfspraakList(MagisterConnection con, Date van, Date tot, Account account)
    {
        String url = URLS.afspraken(account, van, tot, true);

        Response response = con.get(url);

        String body = response.getBody();

        raw = new JSONObject(body).getJSONArray("Items");
    }

    @Override
    public int size() {
        return raw.length();
    }

    protected Boolean fullyParsed()
    {
        return size() == parsed.size();
    }

    protected void parseRemaining() throws ParseException
    {
        if (fullyParsed()) return;

        for (int i = 0; i < size(); i++)
        {
            if (parsed.containsKey(i)) continue;

            parsed.put(i, new Afspraak(raw.getJSONObject(i)));
        }
    }

    @Override
    public Afspraak get(int index)
    {
        if (parsed.containsKey(index))
        {
            return parsed.get(index);
        }

        try
        {
            Afspraak afspraak = new Afspraak(raw.getJSONObject(index));
            parsed.put(index, afspraak);

            return afspraak;
        }

        catch (ParseException e)
        {
            // asdf error handling
            return null;
        }
    }

    public Map<Integer, Afspraak> getDay(Date day) throws ParseException
    {
        parseRemaining();

        Map<Integer, Afspraak> result = new HashMap<>();

        parsed.forEach( (i, afspraak) -> {
            if (afspraak.isOp(day)) result.put(i, afspraak);
        });

        return result;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
