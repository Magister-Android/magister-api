package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;


/**
 * Created by max on 18-10-15.
 */
public class URLS
{
    private static final String SCHOOL_PATTERN = "https://%s.magister.net";

    private final String school;
    private final String base;

    public URLS(String school)
    {

        this.school = String.format(SCHOOL_PATTERN, school);
        base = this.school.concat("/api");
    }

    public String base()
    {
        return school;
    }

    public String login()
    {
        return base + "/sessies";
    }

    public String session()
    {
        return login() + "/huidige";
    }

    public String account()
    {
        return base + "/account";
    }

    public String api(Integer leerling)
    {
        return String.format(base + "/personen/%d", leerling);
    }

    public String api(Account leerling)
    {
        return api(leerling.Id);
    }

    public String afspraken(Integer leerling, DateTime van, DateTime tot, Boolean status)
    {
        return api(leerling) + "/afspraken?status="
                + (status ? "1" : "0")
                + "&tot=" + Utils.dateToString(tot)
                + "&van=" + Utils.dateToString(van);
    }

    public String afspraken(Account leerling, DateTime van, DateTime tot, Boolean status)
    {
        return afspraken(leerling.Id, van, tot, status);
    }

    public String aanmeldingen(Integer leerling)
    {
        return api(leerling) + "/aanmeldingen";
    }

    public String aanmeldingen(Account leerling)
    {
        return aanmeldingen(leerling.Id);
    }

    public String roosterwijzigingen(Integer leerling)
    {
        return api(leerling) + "/roosterwijzigingen";
    }

    public String cijfers(Integer leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers/cijferoverzichtvooraanmelding?actievePerioden=false", jaarlaag);
    }

    public String cijfers(Account leerling, Integer jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers/cijferoverzichtvooraanmelding?actievePerioden=false", jaarlaag);
    }

    public String cijfers(Account leerling, Aanmelding jaarlaag)
    {
        return cijfers(leerling, jaarlaag.Id);
    }

    public String cijferPerioden(Account leerling, Aanmelding jaarlaag)
    {
        return String.format(api(leerling) + "/aanmeldingen/%d/cijfers/cijferperiodenvooraanmelding", jaarlaag.Id);
    }

    public String vakken(Account account, Aanmelding jaarlaag)
    {
        return api(account) + String.format("/aanmeldingen/%d/vakken", jaarlaag.Id);
    }

    public String recentCijfers(Account account, Aanmelding jaarlaag)
    {
        return api(account) + String.format("/aanmeldingen/%d/cijfers", jaarlaag.Id);
    }

    public String cijferDetails(Account account, Aanmelding aanmelding, Cijfer cijfer)
    {
        return api(account) + String.format("/aanmeldingen/%d/cijfers/extracijferkolominfo/%d", aanmelding.Id, cijfer.CijferKolom.Id);
    }

    public String roosterWijzigingen(Account account, DateTime van, DateTime tot)
    {
        return api(account) + String.format("/roosterwijzigingen?van=%s&tot=%s", Utils.dateToString(van), Utils.dateToString(tot));
    }
}
