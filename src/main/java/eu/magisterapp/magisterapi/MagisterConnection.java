package eu.magisterapp.magisterapi;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by max on 18-10-15.
 */
public class MagisterConnection {

    protected String username;
    protected String password;

    public MagisterConnection(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public Boolean delete(String location)
    {
        try
        {
            URL url = new URL(location);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK)
            {
                return false;
            }

            String cookie = connection.getHeaderField("Set-Cookie");

            System.out.println(cookie);
        }

        catch (IOException e)
        {
            return false;
        }

        return true;
    }

}
