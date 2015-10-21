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
            System.out.println(api.getPersoon().getNaam());
        }

        catch(ParseException | BadResponseException e)
        {
            // asdf
        }

    }
}
