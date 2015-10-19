package eu.magisterapp.magisterapi;

import java.util.HashMap;
import java.util.Map;
import org.json.*;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI {

    protected String school;
    protected String username;
    protected String password;
    protected URLS urls;
    protected MagisterConnection connection;

    protected long connectedAt;

    public static final Integer CONNECTION_TIMEOUT = 60*20*1000;

    public MagisterAPI(String school, String username, String password)
    {
        this.school = school;
        this.username = username;
        this.password = password;

        this.urls = new URLS(school);
    }

    public Boolean connect() throws BadResponseException
    {
        MagisterConnection connection = new MagisterConnection(username, password);

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

}
