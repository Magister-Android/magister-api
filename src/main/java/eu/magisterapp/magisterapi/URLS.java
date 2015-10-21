package eu.magisterapp.magisterapi;

/**
 * Created by max on 18-10-15.
 */
public class URLS {

    protected String school;

    protected MagisterConnection connection;

    protected static String base = "https://%s.magister.net/api";
    final String SESSION;
    final String LOGIN;
    final String ACCOUNT;

    final String API;
    final String AFSPRAKEN;
    final String AANMELDINGEN; // is eigenlijk een ID voor je jaarlaag

    final String CIJFERS;

    final String ROOSTERWIJZIGINGEN;

    /**
     * Constructor
     * @param school De school
     */
    public URLS(String school)
    {
        this.school = school;

        CIJFERS = API + "/aanmeldingen/%d/cijfers"; // jaarlaag (komt van request naar API)
        ROOSTERWIJZIGINGEN = API + "/roosterwijzigingen";
    }

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
        return String.format(base + "/api/personen/%d", leerling);
    }

    public static String api(Persoon leerling)
    {
        return api(leerling.Id);
    }

    public static String afspraken(Integer leerling)
    {
        return api(leerling) + "/afspraken";
    }

    public static String afspraken(Persoon leerling)
    {
        return afspraken(leerling.Id);
    }

    public static String aanmeldingen(Integer leerling)
    {
        return api(leerling) + "/aanmeldingen";
    }

    public static String aanmeldingen(Persoon leerling)
    {
        return aanmeldingen(leerling.Id);
    }

    public static String cijfers(Integer leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers", jaarlaag);
    }

    public static String cijfers(Persoon leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers", jaarlaag);
    }

    public static String cijfers(Integer leerling, )
}
