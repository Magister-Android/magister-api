package eu.magisterapp.magisterapi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by max on 18-10-15.
 */
public class Response {

    protected Map<String, List<String>> headers;

    protected int status;

    protected String body;

    protected JSONObject parsed;
    protected Boolean isJson = false;

    public Response(int status, Map<String, List<String>> headers, String body)
    {
        this.status = status;
        this.headers = headers;

        this.body = body;

        if (body.isEmpty()) return;

        if (headers.get("Content-Type").get(0).contains("application/json"))
        {
            parsed = new JSONObject(body);
            isJson = true;
        }
    }

    public static Response fromConnection(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();

        // Als response code 2xx is, inputStream anders is het een errorStream
        InputStream is = status / 100 == 2 ? connection.getInputStream() : connection.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        Map<String, List<String>> headers = connection.getHeaderFields();
        String body = sb.toString();

        return new Response(status, headers, body);
    }

    public boolean isError()
    {
        return status / 100 != 2;
    }

    public boolean isJson()
    {
        return this.isJson;
    }

    public JSONObject getJson()
    {
        return parsed;
    }

    public String getBody()
    {
        return body;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void dumpHeaders()
    {
        Set<Map.Entry<String, List<String>>> entries = headers.entrySet();

        for (Map.Entry<String, List<String>> entry : entries)
        {
            System.out.println(entry.getKey() + ":");

            for (String value : entry.getValue())
            {
                System.out.println("\t" + value);
            }

        }
    }


}
