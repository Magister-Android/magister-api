package eu.magisterapp.magisterapi;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by max on 2-12-15.
 */
public class AanmeldingenList implements Iterable<Aanmelding>
{
    public final List<Aanmelding> aanmeldingen;

    public static AanmeldingenList fromResponse(Response response) throws ParseException
    {
        JSONArray aanmeldingen = response.getJson().getJSONArray("items");

        return new AanmeldingenList(aanmeldingen);
    }

    public AanmeldingenList(JSONArray array) throws ParseException
    {
        List<Aanmelding> raw = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            raw.add(new Aanmelding(array.getJSONObject(i)));
        }

        aanmeldingen = raw;
    }

    @Override
    public Iterator<Aanmelding> iterator() {
        return new Iterator<Aanmelding>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < aanmeldingen.size();
            }

            @Override
            public Aanmelding next() {
                return aanmeldingen.get(current++);
            }
        };
    }
}

