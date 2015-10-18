package eu.magisterapp.magisterapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI {

    protected String school;
    protected String username;
    protected String password;
    protected URLS urls;

    public MagisterAPI(String school, String username, String password)
    {
        this.school = school;
        this.username = username;
        this.password = password;

        this.urls = new URLS(school);
    }

    public Boolean attemptLogin()
    {
        MagisterConnection connection = new MagisterConnection(username, password);

        connection.delete(urls.SESSION);

        Map<String, String> data = new HashMap<String, String>();

        data.put("Gebruikersnaam", Credentials.username);
        data.put("Wachtwoord", Credentials.password);

        connection.post(urls.LOGIN, data);

        return true;
    }

}
