package eu.magisterapp.magisterapi;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class MagisterConnection {

    // TODO Doe dit globaal of in een class waar het logischer is
    public static String API_VERSION = "1.0.0";

    protected final String API_USER_AGENT = "Magister API/" + API_VERSION + " Java/" + System.getProperty("java.version");

    // private CacheManager<Response> cache = new CacheManager<>();

    public Response delete(String location, Sessie sessie) throws IOException
    {
        URL url = new URL(location);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Cookie", sessie.getCookies());
        connection.setRequestProperty("User-Agent", API_USER_AGENT);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.connect();

        storeCookies(connection, sessie);

        return Response.fromConnection(connection);
    }

    public Response post(String location, Map<String, String> data, Sessie sessie) throws IOException
    {
        URL url = new URL(location);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Cookie", sessie.getCookies());
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("User-Agent", API_USER_AGENT);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


        byte[] data_url = convertToDataString(data).getBytes("UTF-8");

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

        dos.write(data_url);

        storeCookies(connection, sessie);

        return Response.fromConnection(connection);
    }

    public Response get(String location, Sessie sessie) throws IOException
    {
        URL url = new URL(location);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", sessie.getCookies());
        connection.setRequestProperty("User-Agent", API_USER_AGENT);

        connection.connect();

        storeCookies(connection, sessie);

        return Response.fromConnection(connection);
    }

    protected String convertToDataString(Map<String, String> data) throws UnsupportedEncodingException
    {
        String result = "";

        for (Map.Entry<String, String> entry : data.entrySet())
        {
            result += URLEncoder.encode(entry.getKey(), "UTF-8")
                   + '='
                   + URLEncoder.encode(entry.getValue(), "UTF-8")
                   + '&';
        }

        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    protected String convertToJSONString(Map<String, String> data)
    {
        JSONObject object = new JSONObject();

        for(Map.Entry<String, String> entry : data.entrySet())
        {
            object.put(entry.getKey(), entry.getValue());
        }

        return object.toString();
    }

    private void storeCookies(HttpURLConnection connection, Sessie sessie)
    {
        if (connection.getHeaderField("Set-Cookie") != null)
        {
            sessie.storeCookies(connection.getHeaderField("Set-Cookie"));
        }
    }

}
