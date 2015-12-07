package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by max on 21-10-15.
 */
public class Module {

    protected String getNullableString(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        try
        {
            return object.getString(key);
        }

        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected Boolean getNullableBoolean(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        try
        {
            return object.getBoolean(key);
        }

        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected Integer getNullableInt(JSONObject object, String key)
    {
        if (object.isNull(key)) return null;

        try
        {
            return object.getInt(key);
        }

        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected DateTime getNullableDate(JSONObject object, String key) throws ParseException
    {
        if (object.isNull(key)) return null;

        try
        {
            return Utils.stringToDate(object.getString(key));
        }

        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
}
