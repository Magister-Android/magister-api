package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import eu.magisterapp.magisterapi.afwijkingen.ZernikeAfwijking;

/**
 * Created by max on 7-12-15.
 */
public class Sessie {

    public static final Integer SESSION_TIMEOUT = 60*20*1000; // 20 minuten in ms
    public static final String ERROR_LOGIN = "Er is iets fout gegaan tijdens het inloggen.";

    private Long loggedInAt = 0L;

    private final String username;
    private final String password;
    private final String school;
    private final Map<String, String> payload;

    private String apiKeyHeader;
    private String apiKey;

    private MagisterConnection connection;

    private final URLS urls;

    private CookieManager cookies = new CookieManager();

    private Account account;
    private AanmeldingenList aanmeldingen;

    public final String id;


    public Sessie(String gebruikersnaam, String wachtwoord, String school, MagisterConnection connection)
    {
        this.username = gebruikersnaam;
        this.password = wachtwoord;
        this.school = school;
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

    public synchronized void logOut()
    {
        cookies.getCookieStore().removeAll();
        loggedInAt = 0L;
    }

    private boolean isExpired()
    {
        return loggedInAt + (SESSION_TIMEOUT - 1000) < System.currentTimeMillis();
    }

    public synchronized void login() throws IOException
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

    private synchronized void loginIfNotLoggedIn() throws IOException
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

    public String getApiKeyHeader() throws IOException
    {
        if (shouldRefresh()) refresh();

        return apiKeyHeader;
    }

    public String getApiKey() throws IOException
    {
        if (shouldRefresh()) refresh();

        return apiKey;

    }

    private boolean shouldRefresh()
    {
        return apiKeyHeader == null || "".equals(apiKeyHeader) || apiKey == null || "".equals(apiKey);
    }

    private void refresh() throws IOException
    {
        String body = MagisterConnection.anonymousGet(urls.base()).body;

        Pattern headerPattern = Pattern.compile("apiKeyHeader: '([a-zA-Z0-9-])',");
        Pattern keyPattern = Pattern.compile("apiKey: '([a-zA-Z0-9-])',");

        apiKeyHeader = headerPattern.matcher(body).group(1);
        apiKey = keyPattern.matcher(body).group(1);
    }

    public synchronized void storeCookies(String cookieString)
    {
        cookies.getCookieStore().add(null, HttpCookie.parse(cookieString).get(0));
    }

    public Account getAccount() throws IOException
    {
        if (account != null) return account;

        loginIfNotLoggedIn();

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

    public AfspraakList getAfspraken(DateTime van, DateTime tot) throws IOException
    {
        return getAfspraken(van, tot, true);
    }

    public AfspraakList getAfspraken(DateTime van, DateTime tot, boolean geenUitval) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.afspraken(getAccount(), van, tot, geenUitval);

        Response response = connection.get(url, this);

        try
        {
            AfspraakList afspraken = AfspraakFactory.make(response, school, getAanmeldingen().getCurrentAanmelding());

            afspraken.filterBullshitAfspraken();

            return afspraken;
        }

        catch (ParseException e)
        {
            throw new BadResponseException("Fout bij het ophalen van afspraken");
        }
    }

    public AanmeldingenList getAanmeldingen() throws IOException
    {
        if (aanmeldingen != null) return aanmeldingen;

        loginIfNotLoggedIn();

        String url = urls.aanmeldingen(getAccount());

        Response response = connection.get(url, this);

        try
        {
            return aanmeldingen = AanmeldingenList.fromResponse(response);
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
            return CijferList.fromResponse(response, vakken, aanmelding);
        }

        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van cijfers");
        }
    }

    public CijferList getCijfers() throws IOException
    {
        Aanmelding aanmelding = getAanmeldingen().getCurrentAanmelding();

        return getCijfers(aanmelding, getVakken(aanmelding));
    }

    public CijferList getRecentCijfers(Aanmelding aanmelding, VakList vakken) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.recentCijfers(getAccount(), aanmelding);

        Response response = connection.get(url, this);

        try
        {
            return CijferList.fromResponse(response, vakken, aanmelding);
        }

        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van recente cijfers.");
        }
    }

    public Cijfer.CijferInfo getCijferInfo(Cijfer cijfer) throws IOException
    {
        Aanmelding aanmelding = cijfer.aanmelding;

        loginIfNotLoggedIn();

        String url = urls.cijferDetails(getAccount(), aanmelding, cijfer);

        Response response = connection.get(url, this);

        try
        {
            return cijfer.new CijferInfo(response);
        }

        catch (ParseException | JSONException e)
        {
            e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van cijfer gegevens");
        }
    }

    public Cijfer attachCijferInfo(Cijfer cijfer) throws IOException
    {
        return attachCijferInfo(getCijferInfo(cijfer), cijfer);
    }

    public Cijfer attachCijferInfo(Cijfer.CijferInfo info, Cijfer cijfer)
    {
        cijfer.setInfo(info);

        return cijfer;
    }

    public AfspraakList getRoosterWijzigingen(DateTime van, DateTime tot) throws IOException
    {
        loginIfNotLoggedIn();

        String url = urls.roosterWijzigingen(getAccount(), van, tot);

        Response response = connection.get(url, this);

        try
        {
            return AfspraakFactory.make(response, school, getAanmeldingen().getCurrentAanmelding());
        }

        catch (ParseException | JSONException e)
        {
			e.printStackTrace();
            throw new BadResponseException("Fout bij het ophalen van roosterwijzigingen");
        }
    }

}
