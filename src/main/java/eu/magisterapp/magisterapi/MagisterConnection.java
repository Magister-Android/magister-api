package eu.magisterapp.magisterapi;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class MagisterConnection {

    protected String username;
    protected String password;

    protected final String API_USER_AGENT = "GET REKT MOTHERFUCKERS";

    protected CookieManager cookieJar = new CookieManager();

    public MagisterConnection(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public Response delete(String location)
    {
        try
        {
            URL url = new URL(location);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Cookie", getCurrentCookies());
            connection.setRequestProperty("User-Agent", API_USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.connect();

            storeCookies(connection);

            return Response.fromConnection(connection);
        }

        catch (IOException e)
        {
            return null;
        }
    }

    public Response post(String location, Map<String, String> data)
    {
        try
        {
            URL url = new URL(location);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cookie", getCurrentCookies());
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("User-Agent", API_USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            byte[] data_url = convertToDataString(data).getBytes(StandardCharsets.UTF_8);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            dos.write(data_url);

            storeCookies(connection);

            return Response.fromConnection(connection);
        }

        catch (IOException e)
        {
            return null;
        }
    }

    public Response get(String location)
    {
        try
        {
            URL url = new URL(location);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", getCurrentCookies());
            connection.setRequestProperty("User-Agent", API_USER_AGENT);

            connection.connect();

            storeCookies(connection);

            return Response.fromConnection(connection);
        }

        catch (IOException e)
        {
            return null;
        }
    }

    protected void storeCookies(HttpsURLConnection connection)
    {
        Map<String, List<String>> headers = connection.getHeaderFields();

        List<String> cookies = headers.get("Set-Cookie");

        if (cookies != null)
        {
            for (String cookie : cookies)
            {
                cookieJar.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    protected String getCurrentCookies()
    {
        String result = "";

        for (HttpCookie cookie : cookieJar.getCookieStore().getCookies())
        {
            result = result.concat(cookie.toString() + ';');
        }

        return result;
    }

    protected String convertToDataString(Map<String, String> data) throws UnsupportedEncodingException
    {
        String result = "";

        for (Map.Entry entry : data.entrySet())
        {
            result += URLEncoder.encode(entry.getKey().toString(), "UTF-8")
                   + '='
                   + URLEncoder.encode(entry.getValue().toString(), "UTF-8")
                   + '&';
        }

        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    public Integer getCookieCount()
    {
        return cookieJar.getCookieStore().getCookies().size();
    }

}
