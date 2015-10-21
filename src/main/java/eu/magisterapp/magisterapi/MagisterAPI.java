package eu.magisterapp.magisterapi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import org.json.*;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI {

    protected String school;
    protected String username;
    protected String password;
    protected int leerlingnummer;
    protected URLS urls;
    protected MagisterConnection connection;

    protected long connectedAt;

    public static final Integer CONNECTION_TIMEOUT = 60*20*1000; // 20 minuten in ms

    public MagisterAPI(String school, String username, String password)
    {
        this.school = school;
        this.username = username;
        this.password = password;

        this.urls = new URLS(school);
    }

    public Boolean connect() throws BadResponseException
    {
        connection = new MagisterConnection(username, password);

        connection.delete(urls.SESSION);

        Map<String, String> data = new HashMap<>();

        data.put("Gebruikersnaam", username);
        data.put("Wachtwoord", password);

        Response loginResponse = connection.post(urls.LOGIN, data);

        if (loginResponse.isError())
        {
            JSONObject json = loginResponse.getJson();
            String message = json != null ? json.getString("message") : "Ongeldige gebruikersnaam of wachtwoord";

            throw new BadResponseException(message);
        }

        connectedAt = System.currentTimeMillis();

        return true;
    }

    public Persoon getPersoon() throws BadResponseException, ParseException
    {
        if (! isConnected())
        {
            connect();
        }

        return new Persoon(connection, urls.ACCOUNT);
    }

    public Afspraak getAfspraken(Date start, Date end) throws BadResponseException
    {
        if (! isConnected())
        {
            connect();
        }

        return new Afspraak(connection, start, end, urls.afspraken(0)); // TODO maak iets dat ll nummers fixt
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

    /**
     *
     * @param date De datum zoals hij in de magister response staat
     * @return De date in een Date object
     * @throws ParseException
     */
    public static Date stringToDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSSSSSS'Z'");
        return df.parse(date);
    }

    /**
     *
     * @param date Een Date object
     * @return De date object naar een string om in een GET request te gebruiken
     */
    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
}
