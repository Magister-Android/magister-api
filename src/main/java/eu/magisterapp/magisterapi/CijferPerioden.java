package eu.magisterapp.magisterapi;


import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by max on 2-12-15.
 */
public class CijferPerioden implements Iterable<CijferPerioden.CijferPeriode> {

    private final List<CijferPeriode> cijferPerioden;

    public static CijferPerioden fromResponse(Response response) throws ParseException, JSONException
    {
        return new CijferPerioden(response.getJson().getJSONArray("Items"));
    }

    public CijferPerioden(JSONArray items) throws ParseException, JSONException
    {
        List<CijferPeriode> perioden = new ArrayList<CijferPeriode>();

        for (int i = 0; i < items.length(); i++) {
            perioden.add(new CijferPeriode(items.getJSONObject(i)));
        }

        cijferPerioden = perioden;
    }

    public CijferPeriode get(int index)
    {
        return cijferPerioden.get(index);
    }

    public int size()
    {
        return cijferPerioden.size();
    }

    public boolean isEmpty()
    {
        return cijferPerioden.isEmpty();
    }

    public CijferPeriode getPeriodeForDate(LocalDate date)
    {
        for (CijferPeriode periode : cijferPerioden)
        {
            if (periode.Start.isAfter(date) && periode.Einde.isBefore(date)) return periode;
        }

        return null;
    }

    public CijferPeriode getCurrentPeriode()
    {
        return getPeriodeForDate(Utils.now());
    }

    public Iterator<CijferPeriode> iterator()
    {
        return new Iterator<CijferPeriode>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < cijferPerioden.size();
            }

            @Override
            public CijferPeriode next() {
                return get(current++);
            }
        };
    }

    public static class CijferPeriode extends Module
    {
        public final String Naam, Omschrijving;
        public final LocalDate Start, Einde;
        public final Integer Id, VolgNummer;

        public CijferPeriode(JSONObject periode) throws ParseException
        {
            Naam = getNullableString(periode, "Naam");
            Omschrijving = getNullableString(periode, "Omschrijving");
            Start = getNullableDate(periode, "Start");
            Einde = getNullableDate(periode, "Einde");
            Id = getNullableInt(periode, "Id");
            VolgNummer = getNullableInt(periode, "VolgNummer");
        }
    }
}
