package eu.magisterapp.magisterapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 2-12-15.
 */
public class CijferList extends ArrayList<Cijfer> implements Serializable
{
    public final Map<Integer, CijferList> vakSorted = new HashMap<>();

    public final Aanmelding aanmelding;

    public static CijferList fromResponse(Response response, VakList vakken, Aanmelding aanmelding) throws ParseException, JSONException
    {
        return new CijferList(response.getJson().getJSONArray("Items"), vakken, aanmelding);
    }

    public CijferList() {
        this.aanmelding = null;
    }

    public CijferList(JSONArray cijferJson, VakList vakken, Aanmelding aanmelding) throws ParseException, JSONException
    {
        this.aanmelding = aanmelding;

        for (int i = 0; i < cijferJson.length(); i++) {
            Cijfer cijfer = new Cijfer(cijferJson.getJSONObject(i), vakken, aanmelding);

            if (cijfer.CijferStr == null) continue; // skip bullshitcijfers

            this.add(cijfer);

            if (cijfer.Vak == null || cijfer.Vak.Id == null) continue;

            if (vakSorted.get(cijfer.Vak.Id) == null)
                vakSorted.put(cijfer.Vak.Id, new CijferList());

            vakSorted.get(cijfer.Vak.Id).add(cijfer);
        }
    }
}
