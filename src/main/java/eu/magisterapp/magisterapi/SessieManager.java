package eu.magisterapp.magisterapi;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by max on 8-12-15.
 */
public class SessieManager {

    private Map<String, Sessie> sessies = new HashMap<>();

    public Sessie add(String school, String username, String password, MagisterConnection connection)
    {
        Sessie sessie = new Sessie(username, password, school, connection);
        sessies.put(sessie.id, sessie);

        return sessie;
    }

    public Sessie get(String username)
    {
        if (sessies.get(username) != null)
            return sessies.get(username);

        throw new NoSuchElementException("That username isn't logged in.");
    }

    public void kill(String id)
    {
        sessies.remove(id);
    }

    public void logOutAll()
    {
        for(Map.Entry<String, Sessie> entries : sessies.entrySet())
        {
            entries.getValue().logOut();
        }
    }


}
