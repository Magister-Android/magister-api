package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 18-10-15.
 */
public class MagisterAPI
{
    protected MagisterConnection connection = new MagisterConnection();

    protected Sessie mainSessie;
    private final SessieManager sessies = new SessieManager();

    protected long connectedAt;


    public MagisterAPI(String school, String username, String password)
    {
        mainSessie = sessies.add(school, username, password, connection);
    }

    public Sessie getMainSessie()
    {
        return mainSessie;
    }

    public Sessie connect(String school, String username, String password)
    {
        return sessies.add(school, username, password, connection);
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

}
