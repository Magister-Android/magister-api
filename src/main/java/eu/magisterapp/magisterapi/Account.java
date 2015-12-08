package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by max on 21-10-15.
 */
public class Account extends Module
{
    public final Integer Id;
    public final String Roepnaam;
    public final String Tussenvoegsel;
    public final String Achternaam;
    public final String OfficieleVoornaam;
    public final String Voorletters;
    public final String OfficieleTussenvoegsels;
    public final String OfficieleAchternaam;
    public final DateTime Geboortedatum;
    public final String GeboortenaamTussenvoegsel;
    public final Boolean GebruikGeboortenaam;

    protected MagisterConnection con;

    public Account(MagisterConnection con, String url, Sessie sessie) throws ParseException, IOException, JSONException
    {
        this.con = con;

        Response response = con.get(url, sessie);

        JSONObject persoon = response.getJson().getJSONObject("Persoon");

        Id = getNullableInt(persoon, "Id");
        Roepnaam = getNullableString(persoon, "Roepnaam");
        Tussenvoegsel = getNullableString(persoon, "Tussenvoegsel");
        Achternaam = getNullableString(persoon, "Achternaam");
        OfficieleVoornaam = getNullableString(persoon, "OfficieleVoornaam");
        Voorletters = getNullableString(persoon, "Voorletters");
        OfficieleTussenvoegsels = getNullableString(persoon, "OfficieleTussenvoegsels");
        OfficieleAchternaam = getNullableString(persoon, "OfficieleAchternaam");
        Geboortedatum = getNullableDate(persoon, "Geboortedatum");
        GeboortenaamTussenvoegsel = getNullableString(persoon, "GeboortenaamTussenvoegsel");
        GebruikGeboortenaam = getNullableBoolean(persoon, "GebruikGeboortenaam");
    }

    public String getNaam()
    {
        return GebruikGeboortenaam ? OfficieleVoornaam + " " + OfficieleAchternaam : Roepnaam + " " + Achternaam;
    }

    public int getId(){
        return Id;
    }

}
