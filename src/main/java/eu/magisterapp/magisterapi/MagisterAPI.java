package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI
{
    protected MagisterConnection connection = new MagisterConnection();

    protected Sessie mainSessie;
    private final SessieManager sessies = new SessieManager();

    protected long connectedAt;


    /**
     * Constructor
     * @param  school   De naam van je school als in de url
     * @param  username Je username
     * @param  password Je password
     */
    public MagisterAPI(String school, String username, String password)
    {
        mainSessie = sessies.add(school, username, password, connection);
    }

    /**
     * Krijg de main sessie
     * @return De main sessie
     */
    public Sessie getMainSessie()
    {
        return mainSessie;
    }

    /**
     * Verbind met een school
     * @param  school   De naam van je school, als in de url
     * @param  username Je username
     * @param  password Je password
     * @return          Een ingeloggde sessie
     */
    public Sessie connect(String school, String username, String password)
    {
        return sessies.add(school, username, password, connection);
    }

    /**
     * Verandert de main sessie in een nieuwe (handig voor als iemand zn gegevens update)
     *
     * @param school    De naam van je school, als in de url
     * @param username  Je username
     * @param password  Je password
     * @return          Een ingeloggde sessie
     */
    public Sessie reconnect(String school, String username, String password)
    {
        sessies.kill(mainSessie.id);

        return mainSessie = connect(school, username, password);
    }

    public Account getAccount() throws IOException
    {
        return getAccount(mainSessie);
    }

    public Account getAccount(Sessie sessie) throws IOException
    {
        return sessie.getAccount();
    }

    public AfspraakCollection getAfspraken(DateTime start, DateTime end) throws IOException
    {
        return getAfspraken(mainSessie, start, end, false);
    }

    public AfspraakCollection getAfspraken(DateTime start, DateTime end, boolean geenUitval) throws IOException
    {
        return getAfspraken(mainSessie, start, end, geenUitval);
    }

    public AfspraakCollection getAfspraken(Sessie sessie, DateTime start, DateTime end, boolean geenUitval) throws IOException
    {
        return sessie.getAfspraken(start, end, geenUitval);
    }

    public AfspraakCollection getAfspraken(Sessie sessie, DateTime start, DateTime end) throws IOException
    {
        return getAfspraken(sessie, start, end, false);
    }

    public AanmeldingenList getAanmeldingen() throws IOException
    {
        return getAanmeldingen(mainSessie);
    }

    public AanmeldingenList getAanmeldingen(Sessie sessie) throws IOException
    {
        return sessie.getAanmeldingen();
    }

    public Aanmelding getCurrentAanmelding() throws IOException
    {
        return getAanmeldingen().getCurrentAanmelding();
    }

    public Aanmelding getCurrentAanmelding(Sessie sessie) throws IOException
    {
        return getAanmeldingen(sessie).getCurrentAanmelding();
    }

    public CijferPerioden getCijferPerioden(Sessie sessie, Aanmelding aanmelding) throws IOException
    {
        return sessie.getCijferPerioden(aanmelding);
    }

    public CijferPerioden getCijferPerioden(Aanmelding aanmelding) throws IOException
    {
        return getCijferPerioden(mainSessie, aanmelding);
    }

    public CijferList getCijfers(Sessie sessie, Aanmelding aanmelding, VakList vakken) throws IOException
    {
        return sessie.getCijfers(aanmelding, vakken);
    }

    public CijferList getCijfers(Aanmelding aanmelding, VakList vakken) throws IOException
    {
        return getCijfers(mainSessie, aanmelding, vakken);
    }

    public CijferList getCijfers(Sessie sessie) throws IOException
    {
        return getCijfers(sessie, getCurrentAanmelding(sessie), getVakken(sessie, getCurrentAanmelding(sessie)));
    }

    public CijferList getCijfers() throws IOException
    {
        return getCijfers(mainSessie);
    }

    public CijferList getRecentCijfers(Sessie sessie) throws IOException
    {
        Aanmelding aanmelding = sessie.getAanmeldingen().getCurrentAanmelding();

        return sessie.getRecentCijfers(aanmelding, sessie.getVakken(aanmelding));
    }

    public CijferList getRecentCijfers() throws IOException
    {
        return getRecentCijfers(mainSessie);
    }

    public VakList getVakken(Sessie sessie, Aanmelding aanmelding) throws IOException
    {
        return sessie.getVakken(aanmelding);
    }

    public VakList getVakken(Aanmelding aanmelding) throws IOException
    {
        return getVakken(mainSessie, aanmelding);
    }

    public void disconnect()
    {
        sessies.logOutAll();
    }


    /**
     * Zoek naar scholen
     * @param  filter      De filter, moet meer dan 3 chars zijn
     * @return             Een list met volledige schoolnamen, bijvoorbeeld "Zernike College"
     * @throws IOException Als de anonymousGet faalt
     */
    public static List<String> zoekSchool(String filter) throws IOException
    {
        JSONArray response;
        List<String> scholen;

        scholen = new ArrayList<String>();

        if(filter.length() < 3)
            return new ArrayList<String>();

        response = MagisterConnection.anonymousGet("https://mijn.magister.net/api/schools?filter=" + filter).getJsonList();

        for(int i = 0; i < response.length(); i++)
            scholen.add(response.getJSONObject(i).getString("Name"));

        return scholen;
    }
}
