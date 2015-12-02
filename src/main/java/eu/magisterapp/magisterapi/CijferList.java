package eu.magisterapp.magisterapi;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 2-12-15.
 */
public class CijferList {

    public final List<Cijfer> cijfers;

    public static CijferList fromResponse(Response response) throws ParseException
    {
        return new CijferList(response.getJson().getJSONArray("Items"));
    }

    public CijferList(JSONArray cijferJson) throws ParseException
    {
        List<Cijfer> localList = new ArrayList<>();

        for (int i = 0; i < cijferJson.length(); i++) {
            localList.add(new Cijfer(cijferJson.getJSONObject(i)));
        }

        // localList.sort op een of andere manier

         cijfers = localList;
    }

}
