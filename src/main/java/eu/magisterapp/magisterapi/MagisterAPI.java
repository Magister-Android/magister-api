package eu.magisterapp.magisterapi;

import org.joda.time.LocalDateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI {

    protected String school;
    protected String username;
    protected String password;
    protected MagisterConnection connection;

    protected long connectedAt;

    public static final Integer CONNECTION_TIMEOUT = 60*20*1000; // 20 minuten in ms

    public MagisterAPI(String school, String username, String password)
    {
        this.school = school;
        this.username = username;
        this.password = password;

        URLS.setSchool(school);
    }

    public Boolean connect() throws IOException, JSONException
    {
        connection = new MagisterConnection(username, password);

        connection.delete(URLS.session());

        Map<String, String> data = new HashMap<>();

        data.put("Gebruikersnaam", username);
        data.put("Wachtwoord", password);

        Response loginResponse = connection.post(URLS.login(), data);

        if (loginResponse.isError())
        {
            JSONObject json = loginResponse.getJson();
            String message = json != null ? json.getString("message") : "Ongeldige gebruikersnaam of wachtwoord";

            throw new IOException(message);
        }

        connectedAt = System.currentTimeMillis();

        return true;
    }

    public Account getAccount() throws IOException, ParseException, JSONException
    {
        return new Account(getConnection(), URLS.account());
    }

    public AfspraakCollection getAfspraken(LocalDateTime start, LocalDateTime end) throws IOException, ParseException, JSONException
    {
        return getAfspraken(start, end, false);
    }

    public AfspraakCollection getAfspraken(LocalDateTime start, LocalDateTime end, Boolean geenUitval) throws IOException, ParseException, JSONException
    {
        return AfspraakFactory.fetch(getConnection(), start, end, geenUitval, getAccount());
    }

    public AanmeldingenList getAanmeldingen(Account account) throws IOException, ParseException, JSONException
    {
        String url = URLS.aanmeldingen(account);

        Response response = getConnection().get(url);

        return AanmeldingenList.fromResponse(response);
    }

    public AanmeldingenList getAanmeldingen() throws IOException, ParseException, JSONException
    {
        return getAanmeldingen(getAccount());
    }

    public Aanmelding getCurrentAanmelding() throws IOException, ParseException, JSONException
    {
        return getAanmeldingen().getCurrentAanmelding();
    }

    public CijferPerioden getCijferPerioden(Account account, Aanmelding aanmelding) throws IOException, ParseException, JSONException
    {
        String url = URLS.cijferPerioden(account, aanmelding);

        Response response = getConnection().get(url);

        return CijferPerioden.fromResponse(response);
    }

    public CijferList getCijfers(Account account, Aanmelding aanmelding, VakList vakken) throws IOException, ParseException, JSONException
    {
        String url = URLS.cijfers(account, aanmelding);

        Response response = getConnection().get(url);

        return CijferList.fromResponse(response, vakken);
    }

    public CijferList getCijfers() throws IOException, ParseException, JSONException
    {
        return getCijfers(getAccount(), getCurrentAanmelding(), getVakken());
    }

    public VakList getVakken(Account account, Aanmelding aanmelding) throws IOException, ParseException, JSONException
    {
        String url = URLS.vakken(account, aanmelding);

        Response response = getConnection().get(url);

        return VakList.fromResponse(response);
    }

    public VakList getVakken() throws IOException, ParseException, JSONException
    {
        return getVakken(getAccount(), getCurrentAanmelding());
    }

    public Boolean isConnected()
    {
        return connection != null // er is een verbinding gemaakt
                && connection.getCookieCount() > 0 // er is een sessie opgeslagen
                && System.currentTimeMillis() - connectedAt > CONNECTION_TIMEOUT - 1000;
                // Meer dan 1 sec over op de sessie.
                // 1 sec ivm traag zernike internet, waar je geen request kan doen in minder dan 1 sec.
                // Beetje lullig als je sessie halverwege je request verloopt.
    }

    public void disconnect()
    {
        connection = null;
        connectedAt = 0;
    }

    public MagisterConnection getConnection() throws IOException, JSONException
    {
        if (! isConnected()) connect();

        return connection;
    }

}
