package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by max on 21-10-15.
 */
public class Account extends Module
{
    public Integer Id;
    public String Roepnaam;
    public String Tussenvoegsel;
    public String Achternaam;
    public String OfficieleVoornaam;
    public String Voorletters;
    public String OfficieleTussenvoegsels;
    public String OfficieleAchternaam;
    public LocalDate Geboortedatum;
    public String GeboortenaamTussenvoegsel;
    public Boolean GebruikGeboortenaam;

    protected MagisterConnection con;

    public Account(MagisterConnection con, String url) throws ParseException, IOException
    {
        this.con = con;

        Response response = con.get(url);

        parseResponse(response);
    }

    protected void parseResponse(Response response) throws ParseException, JSONException
    {
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
