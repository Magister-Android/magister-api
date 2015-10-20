package eu.magisterapp.magisterapi;

/**
 * Created by max on 18-10-15.
 */
public class URLS {

    protected String school;

    final String BASE;
    final String SESSION;
    final String LOGIN;
		final String API;
		final String AFSPRAKEN;

		/**
		 * Constructor
		 * @param school De school
		 * @param llnr Het leerlingen nummer (Niet de echte maar de Magister)
		 */
    public URLS(String school, int llnr)
    {
        this.school = school;

        BASE = "https://" + school + ".magister.net/api";
        LOGIN = BASE + "/sessies";
        SESSION = LOGIN + "/huidige";
				API = BASE + "/api/personen/" + Integer.toString(llnr);
				AFSPRAKEN = API + "/afspraken";
    }

}
