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

    private final URLS urls;

    private CookieManager cookies = new CookieManager();

    private Account account;


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
            // Cookies worden op de sessie gezet door de connection.

            loggedInAt = System.currentTimeMillis();
        }

        else
        {
            throw new BadResponseException(ERROR_LOGIN);
        }
    }

    private void loginIfNotLoggedIn(MagisterConnection connection) throws IOException
    {
        if (! loggedIn()) login(connection);
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

    public Account getAccount(MagisterConnection connection) throws IOException
    {
        loginIfNotLoggedIn(connection);

        if (account == null)
        {
            try
            {
                System.out.println(urls.account());
                return account = new Account(connection, urls.account());
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

    public AfspraakCollection getAfspraken(MagisterConnection connection, DateTime van, DateTime tot) throws IOException
    {
        loginIfNotLoggedIn(connection);

        return getAfspraken(connection, van, tot, true);
    }

    public AfspraakCollection getAfspraken(MagisterConnection connection, DateTime van, DateTime tot, boolean geenUitval) throws IOException
    {
        loginIfNotLoggedIn(connection);

        try
        {
            return AfspraakFactory.fetch(connection, urls.afspraken(getAccount(connection), van, tot, geenUitval));
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van afspraken");
        }
    }

    public AanmeldingenList getAanmeldingen(MagisterConnection connection) throws IOException
    {
        loginIfNotLoggedIn(connection);

        String url = urls.aanmeldingen(getAccount(connection));

        Response response = connection.get(url);

        try
        {
            return AanmeldingenList.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van aanmeldingen");
        }
    }

    public CijferPerioden getCijferPerioden(MagisterConnection connection, Aanmelding aanmelding) throws IOException
    {
        loginIfNotLoggedIn(connection);

        String url = urls.cijferPerioden(getAccount(connection), aanmelding);

        Response response = connection.get(url);

        try
        {
            return CijferPerioden.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van de cijferperioden.");
        }

    }

    public VakList getVakken(MagisterConnection connection, Aanmelding aanmelding) throws IOException
    {
        loginIfNotLoggedIn(connection);

        String url = urls.vakken(getAccount(connection), aanmelding);

        Response response = connection.get(url);

        try
        {
            return VakList.fromResponse(response);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van de vakken");
        }
    }


    public CijferList getCijfers(MagisterConnection connection, Aanmelding aanmelding, VakList vakken) throws IOException
    {
        loginIfNotLoggedIn(connection);

        String url = urls.cijfers(getAccount(connection), aanmelding);

        Response response = connection.get(url);

        try
        {
            return CijferList.fromResponse(response, vakken);
        }

        catch (ParseException | JSONException e)
        {
            throw new BadResponseException("Fout bij het ophalen van cijfers");
        }
    }

}
