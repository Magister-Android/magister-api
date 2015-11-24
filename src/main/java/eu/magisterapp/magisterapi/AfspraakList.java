package eu.magisterapp.magisterapi;

// import com.sun.istack.internal.NotNull;
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

    protected List<Afspraak> parsed = new ArrayList<>();

    protected Map<Integer, List<Afspraak>> dagen = new HashMap<>();

    private int current = 0;

    public AfspraakList(MagisterConnection con, LocalDate van, LocalDate tot, Boolean status, Account account)
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
            if (parsed.contains(i)) continue;

            parsed.add(new Afspraak(raw.getJSONObject(i)));
        }
    }

    private Afspraak parse(Integer i) throws ParseException
    {
        if (parsed.contains(i)) return null;

        Afspraak afspraak = new Afspraak(raw.getJSONObject(i));

        parsed.add(afspraak);
        dagen.get(afspraak.getDayConstant()); // TODO

        return null;
    }

    @Override
    public Afspraak get(int index)
    {
        if (parsed.contains(index))
        {
            return parsed.get(index);
        }

        try
        {
            return parse(index);
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

        parsed.forEach( (afspraak) -> {
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
