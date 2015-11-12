package eu.magisterapp.magisterapi;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Date;
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

    public Boolean connect() throws BadResponseException
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

            throw new BadResponseException(message);
        }

        connectedAt = System.currentTimeMillis();

        return true;
    }

    public Account getAccount() throws BadResponseException, ParseException
    {
        return new Account(getConnection(), URLS.account());
    }

    public AfspraakList getAfspraken(Date start, Date end) throws BadResponseException, ParseException
    {
        return getAfspraken(start, end, true);
    }

    public AfspraakList getAfspraken(Date start, Date end, Boolean status) throws BadResponseException, ParseException
    {
        return new AfspraakList(getConnection(), start, end, status, getAccount());
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

    public MagisterConnection getConnection() throws BadResponseException
    {
        if (! isConnected()) connect();

        return connection;
    }

}
