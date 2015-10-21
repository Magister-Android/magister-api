package eu.magisterapp.magisterapi;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by max on 21-10-15.
 */
public class Module {

    protected String getNullableString(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        return object.getString(key);
    }

    protected Boolean getNullableBoolean(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        return object.getBoolean(key);
    }

    protected Integer getNullableInt(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        return object.getInt(key);
    }

    protected Date getNullableDate(JSONObject object, String key) throws ParseException
    {
        if (object.isNull(key)) return null;

        return MagisterAPI.stringToDate(object.getString(key));
    }


}
