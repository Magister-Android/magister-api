package eu.magisterapp.magisterapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by max on 2-12-15.
 */
public class CijferList implements Iterable<Cijfer>
{
    public final List<Cijfer> cijfers;

    public static CijferList fromResponse(Response response, VakList vakken) throws ParseException, JSONException
    {
        return new CijferList(response.getJson().getJSONArray("Items"), vakken);
    }

    public CijferList(JSONArray cijferJson, VakList vakken) throws ParseException, JSONException
    {
        List<Cijfer> localList = new ArrayList<>();

        for (int i = 0; i < cijferJson.length(); i++) {
            Cijfer cijfer = new Cijfer(cijferJson.getJSONObject(i), vakken);

            if (cijfer.CijferStr == null) continue; // motherfucking schoolmaster NullPointerveroorzakende teringleiers

            localList.add(cijfer);
        }

         cijfers = localList;
    }

    public Iterator<Cijfer> iterator()
    {
        return new Iterator<Cijfer>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < cijfers.size();
            }

            @Override
            public Cijfer next() {
                return cijfers.get(current++);
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

}
