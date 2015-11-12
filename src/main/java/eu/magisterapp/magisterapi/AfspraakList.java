package eu.magisterapp.magisterapi;

import com.sun.istack.internal.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by max on 23-10-15.
 */
public class AfspraakList extends ArrayList<Afspraak> implements Iterable<Afspraak>
{
    protected JSONArray raw;

    protected Map<Integer, Afspraak> parsed = new HashMap<>();

    private int current = 0;

    public AfspraakList(MagisterConnection con, Date van, Date tot, Boolean status, Account account)
    {
        String url = URLS.afspraken(account, van, tot, status);

        System.out.println(url);

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

    public List<Afspraak> getForDay(String ymd) throws ParseException
    {
        return getForDay(Utils.stringToDate(ymd));
    }

    public List<Afspraak> getForDay(LocalDate day) throws ParseException
    {
        parseRemaining();

        List<Afspraak> result = new ArrayList<>();

        parsed.forEach( (i, afspraak) -> {
            if (afspraak.isOp(day)) result.add(afspraak);
        });

        return result;
    }

    @Override
    public Iterator<Afspraak> iterator()
    {
        return new Iterator<Afspraak>()
        {
            @Override
            public boolean hasNext()
            {
                return current < size();
            }

            @Override
            public Afspraak next()
            {
                if (hasNext())
                {
                    return get(current++);
                }

                throw new NoSuchElementException("There are no more elements. Check hasNext(), doofus.");
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException("I am too lazy to implement this.");
            }
        };
    }
}
