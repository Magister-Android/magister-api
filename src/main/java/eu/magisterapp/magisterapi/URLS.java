package eu.magisterapp.magisterapi;

/**
 * Created by max on 18-10-15.
 */
public class URLS {

    protected String school;

    final String BASE;
    final String SESSION;
    final String LOGIN;

    public URLS(String school)
    {
        this.school = school;

        BASE = "https://" + school + ".magister.net/api";
        LOGIN = BASE + "/sessies";
        SESSION = LOGIN + "/huidige";
    }

}
