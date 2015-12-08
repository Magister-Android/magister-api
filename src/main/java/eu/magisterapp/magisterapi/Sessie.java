package eu.magisterapp.magisterapi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 7-12-15.
 */
public class Sessie {

    public static final Integer SESSION_TIMEOUT = 60*20*1000; // 20 minuten in ms
    public static final String ERROR_LOGIN = "Er is iets fout gegaan tijdens het inloggen.";

    private Long loggedInAt = 0L;

    private final String username;
    private final String password;
    private final Map<String, String> payload;

    private final URLS urls;

    private CookieManager cookies = new CookieManager();


    public Sessie(String gebruikersnaam, String wachtwoord, String school)
    {
        this.username = gebruikersnaam;
        this.password = wachtwoord;

        urls = new URLS(school);

        payload = new HashMap<String, String>() {{
            put("Gebruikersnaam", username);
            put("Wachtwoord", password);
        }};
    }

    public boolean loggedIn()
    {
        return ! isExpired() && cookies.getCookieStore().getCookies().size() > 0;
    }

    public void logOut()
    {
        cookies.getCookieStore().removeAll();
        loggedInAt = 0L;
    }

    private boolean isExpired()
    {
        return loggedInAt + (SESSION_TIMEOUT - 1000) < System.currentTimeMillis();
    }

    private void login(MagisterConnection connection) throws IOException
    {
        connection.delete(urls.session());

        Response response = connection.post(urls.login(), payload);

        if (response.isError() && response.isJson())
        {
            try
            {
                JSONObject json;

                if ((json = response.getJson()) != null)
                {
                    throw new BadResponseException(json.getString("message"));
                }
            }

            catch (JSONException e)
            {
                throw new BadResponseException(ERROR_LOGIN);
            }
        }

        else if(! response.isJson())
        {
            // Als het response geen JSON is, kunnen we vrij
            // weinig. Daarom boeken we 'm hier maar voordat
            // we Nullpointers naar niet-bestaande json krijgen.
            throw new BadResponseException(ERROR_LOGIN);
        }

        if (response.headers.get("Set-Cookie") != null && response.headers.get("Set-Cookie").size() > 0)
        {
            for(String cookie : response.headers.get("Set-Cookie"))
            {
                cookies.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }

            loggedInAt = System.currentTimeMillis();
        }

        else
        {
            throw new BadResponseException(ERROR_LOGIN);
        }
    }

    public String getCookies(MagisterConnection connection) throws IOException
    {
        if (cookies.getCookieStore().getCookies().size() > 0)
        {
            login(connection);
        }

        // Als het goed is, zijn er op dit punt altijd
        // cookies. Als het niet gelukt is om deze uit de
        // headers te halen was er een exception ontstaan.
        StringBuilder builder = new StringBuilder();

        for (HttpCookie cookie : cookies.getCookieStore().getCookies())
        {
            builder.append(cookie.toString()).append(';');
        }

        return builder.toString();
    }

}
