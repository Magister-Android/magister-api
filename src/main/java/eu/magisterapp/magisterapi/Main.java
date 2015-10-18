package eu.magisterapp.magisterapi;

/**
 * Created by max on 18-10-15.
 */
public class Main {

    public static void main(String[] args)
    {
        MagisterAPI api = new MagisterAPI("zernike", Credentials.username, Credentials.password);

        api.attemptLogin();
    }
}
