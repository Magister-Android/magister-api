package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by max on 2-12-15.
 */
public class AanmeldingenList implements Iterable<Aanmelding>
{
    public final List<Aanmelding> aanmeldingen;

    public static AanmeldingenList fromResponse(Response response) throws ParseException
    {
        JSONArray aanmeldingen = response.getJson().getJSONArray("Items");

        return new AanmeldingenList(aanmeldingen);
    }

    public AanmeldingenList(JSONArray array) throws ParseException
    {
        List<Aanmelding> raw = new ArrayList<Aanmelding>();

        for (int i = 0; i < array.length(); i++) {
            raw.add(new Aanmelding(array.getJSONObject(i)));
        }

        aanmeldingen = raw;
    }

    public Aanmelding getAanmeldingForDate(LocalDate date)
    {
        for (Aanmelding aanmelding : aanmeldingen)
        {
            if (aanmelding.Start.isBefore(date) && aanmelding.Einde.isAfter(date)) return aanmelding;
        }

        throw new NoSuchElementException();
    }

    public Aanmelding getCurrentAanmelding()
    {
        return getAanmeldingForDate(Utils.now());
    }

    public Iterator<Aanmelding> iterator() {
        return new Iterator<Aanmelding>() {

            int current = 0;

            public boolean hasNext() {
                return current < aanmeldingen.size();
            }

            public Aanmelding next() {
                return aanmeldingen.get(current++);
            }

            public void remove() {
                throw new UnsupportedOperationException("Aanmeldingen can not be altered.");
            }
        };
    }
}

