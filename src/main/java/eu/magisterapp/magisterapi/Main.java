package eu.magisterapp.magisterapi;

import java.text.ParseException;

/**
 * Created by max on 18-10-15.
 */
public class Main {

    public static void main(String[] args)
    {
        MagisterAPI api = new MagisterAPI("zernike", Credentials.username, Credentials.password);

        try
        {
            System.out.println(api.getAccount().getNaam());

            System.out.println(api.getConnection().get(URLS.aanmeldingen(12130)).getBody());
        }

        catch(ParseException | BadResponseException e)
        {
            // asdf
        }

    }
}
