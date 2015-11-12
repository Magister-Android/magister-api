package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by max on 18-10-15.
 */
public class URLS
{
    protected static String base = "https://%s.magister.net/api";

    public static void setSchool(String school)
    {
        base = String.format(base, school);
    }

    public static String login()
    {
        return base + "/sessies";
    }

    public static String session()
    {
        return login() + "/huidige";
    }

    public static String account()
    {
        return base + "/account";
    }

    public static String api(Integer leerling)
    {
        return String.format(base + "/personen/%d", leerling);
    }

    public static String api(Account leerling)
    {
        return api(leerling.Id);
    }

    public static String afspraken(Integer leerling, LocalDate van, LocalDate tot, Boolean status)
    {
        return api(leerling) + "/afspraken?status="
                + (status ? "1" : "0")
                + "&tot=" + Utils.dateToString(tot)
                + "&van=" + Utils.dateToString(van);
    }

    public static String afspraken(Account leerling, LocalDate van, LocalDate tot, Boolean status)
    {
        return afspraken(leerling.Id, van, tot, status);
    }

    public static String aanmeldingen(Integer leerling)
    {
        return api(leerling) + "/aanmeldingen";
    }

    public static String aanmeldingen(Account leerling)
    {
        return aanmeldingen(leerling.Id);
    }

    public static String roosterwijzigingen(Integer leerling)
    {
        return api(leerling) + "/roosterwijzigingen";
    }

    public static String roosterwijzigingen(Account leerling)
    {
        return roosterwijzigingen(leerling.Id);
    }

    public static String cijfers(Integer leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers", jaarlaag);
    }

    public static String cijfers(Account leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers", jaarlaag);
    }
}
