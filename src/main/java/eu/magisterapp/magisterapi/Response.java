package eu.magisterapp.magisterapi;

import java.util.List;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class Response {

    protected Map<String, List<String>> headers;

    protected int status;

    protected String body;

    public Response(int status, Map<String, List<String>> headers, String body)
    {
        this.status = status;
        this.headers = headers;

        this.body = body;
    }

    public List<String> getCookies()
    {
        if (headers == null) return null;

        if (headers.get("Set-Cookie") == null) return null;

        return null;
    }

}
