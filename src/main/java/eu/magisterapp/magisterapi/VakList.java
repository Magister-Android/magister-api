package eu.magisterapp.magisterapi;

import org.json.JSONArray;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 2-12-15.
 */
public class VakList {

    public final Map<Integer, Vak> vakken;

    public static VakList fromResponse(Response response) throws ParseException
    {
        return new VakList(response.getJsonList());
    }

    public VakList(JSONArray list) throws ParseException
    {
        Map<Integer, Vak> vakList = new HashMap<>();

        for (int i = 0; i < list.length(); i++) {
            Vak vak = new Vak(list.getJSONObject(i));

            vakList.put(vak.id, vak);
        }

        vakken = vakList;
    }

    public Vak getById(Integer id)
    {
        return vakken.get(id);
    }

}
