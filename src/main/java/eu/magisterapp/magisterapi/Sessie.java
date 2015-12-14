package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
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

    private MagisterConnection connection;

    private final URLS urls;

    private CookieManager cookies = new CookieManager();

    private Account account;

    public final String id;


    public Sessie(String gebruikersnaam, String wachtwoord, String school, MagisterConnection connection)
    {
        this.username = gebruikersnaam;
        this.password = wachtwoord;
        this.connection = connection;

        urls = new URLS(school);

        payload = new HashMap<String, String>() {{
            put("Gebruikersnaam", username);
            put("Wachtwoord", password);
        }};

        id = "un:" + gebruikersnaam + "sc:" + school;
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

    public void login() throws IOException
    {
        cookies.getCookieStore().removeAll();

        connection.delete(urls.session(), this);

        Response response = connection.post(urls.login(), payload, this);

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
            // Cookies worden op de sessie gezet door de connection.

            loggedInAt = System.currentTimeMillis();
        }

        else
        {
            throw new BadResponseException(ERROR_LOGIN);
        }
    }

    private void loginIfNotLoggedIn() throws IOException
    {
        if (! loggedIn()) login();
    }

    public String getCookies()
    {
        if (cookies.getCookieStore().getCookies().size() == 0)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (HttpCookie cookie : cookies.getCookieStore().getCookies())
        {
            builder.append(cookie.toString()).append(';');
        }

        return builder.toString();
    }

    public void storeCookies(String cookieString)
    {
        cookies.getCookieStore().add(null, HttpCookie.parse(cookieString).get(0));
    }

    public Account getAccount() throws IOException
    {
        loginIfNotLoggedIn();

        if (account == null)
        {
            try
            {
                return account = new Account(connection, urls.account(), this);
            }

            catch (ParseException | JSONException e)
            {
                e.printStackTrace();

                throw new BadResponseException("Fout bij het ophalen van account gegevens.");
            }
        }

        else
        {
            return account;
        }
    }

    public AfspraakCollection getAfspraken(DateTime van, DateTime tot) throws IOException
    {
        loginIfNotLoggedIn();

        return getAfspraken(van, tot, true);
    }

    public AfspraakCollection getAfspraken(DateTime van, DateTime tot, boolean geenUitval) throws IOException
    {
        loginIfNotLoggedIn();

        try
        {
            return AfspraakFactory.fetch(connection, this, urls.afspraken(getAccount(), van, tot, geenUitval));
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van afspraken");
        }
    }

    public AanmeldingenList getAanmeldingen() throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.aanmeldingen(getAccount());

        Response response = connection.get(url, this);

        try
        {
            return AanmeldingenList.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van aanmeldingen");
        }
    }

    public CijferPerioden getCijferPerioden(Aanmelding aanmelding) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.cijferPerioden(getAccount(), aanmelding);

        Response response = connection.get(url, this);

        try
        {
            return CijferPerioden.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van de cijferperioden.");
        }

    }

    public VakList getVakken(Aanmelding aanmelding) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.vakken(getAccount(), aanmelding);

        Response response = connection.get(url, this);

        try
        {
            return VakList.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van de vakken");
        }
    }


    public CijferList getCijfers(Aanmelding aanmelding, VakList vakken) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.cijfers(getAccount(), aanmelding);

        Response response = connection.get(url, this);

        try
        {
            return CijferList.fromResponse(response, vakken);
        }

        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van cijfers");
        }
    }

}
