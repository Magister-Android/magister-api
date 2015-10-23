package eu.magisterapp.magisterapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Sibren Talens <me@sibrentalens.com>
 * Krijg een instance van deze class door MagisterAPI.getAfspraken();
 */
public class Afspraak extends Module {
	// Ik heb deze namen ook niet bedacht, dit is hoe ze in de API staan,
	// en het leek me wel zo netjes om die te houden
	public int Id;
	public Date Start, Einde;
	public boolean DuurtHeleDag;
	public String Omschrijving;
	public String Lokatie;
	public String Locatie; // Do you even spelling?? Schoolmaster?? Hello bic boi??
	public int Status;
	public int WeergaveType;
	public String Inhoud;
	public int InfoType;
	public String Aantekening;
	public boolean Afgerond;

	// Dit zijn arrays met daarin weer JSON objects,
	// Voor ACFuck en shit
	public List<Vak> Vakken = new ArrayList<>();
	public List<Docent> Docenten = new ArrayList<>();
	public List<Lokaal> Lokalen = new ArrayList<>();

	public int OpdrachtId;
	public boolean HeeftBijlagen;

	protected SimpleDateFormat testformat = new SimpleDateFormat("yyyy-mm-dd");

	// 6 is wss vakantie en volgens mata is 1 huiswerk, 3 tentamen, 4 schriftelijk en 13 is denk ik les
	// TODO Maak hier een mooie enum van
	public int Type;

	// TODO Vind de datatypen uit van de velden die null zijn

	public Afspraak(JSONObject afspraak) throws ParseException
	{
		parseResponse(afspraak);
	}

	protected void parseResponse(JSONObject afspraak) throws ParseException
	{
		Id = getNullableInt(afspraak, "Id");
		Start = getNullableDate(afspraak, "Start");
		Einde = getNullableDate(afspraak, "Einde");
		DuurtHeleDag = getNullableBoolean(afspraak, "DuurtHeleDag");
		Omschrijving = getNullableString(afspraak, "Omschrijving");
		Lokatie = Locatie = getNullableString(afspraak, "Lokatie");
		Status = getNullableInt(afspraak, "Status");
		WeergaveType = getNullableInt(afspraak, "WeergaveType");
		Inhoud = getNullableString(afspraak, "Inhoud");
		InfoType = getNullableInt(afspraak, "InfoType");
		Aantekening = getNullableString(afspraak, "Aantekening");
		Afgerond = getNullableBoolean(afspraak, "Afgerond");
		OpdrachtId = getNullableInt(afspraak, "OpdrachtId");
		HeeftBijlagen = getNullableBoolean(afspraak, "HeeftBijlagen");
		Type = getNullableInt(afspraak, "Type");

		// JSONArray.forEach werkt niet - je krijgt dan instances van Object ipv JSONObject. ripperino..
		// dan maar for loops lol

		JSONArray vakArray = afspraak.getJSONArray("Vakken");
		for (Integer i = 0; i < vakArray.length(); i++)
		{
			Vakken.add(new Vak(vakArray.getJSONObject(i)));
		}

		JSONArray docentArray = afspraak.getJSONArray("Docenten");
		for (Integer i = 0; i < docentArray.length(); i++)
		{
			Docenten.add(new Docent(docentArray.getJSONObject(i)));
		}

		JSONArray lokaalArray = afspraak.getJSONArray("Lokalen");
		for (Integer i = 0; i < lokaalArray.length(); i++)
		{
			Lokalen.add(new Lokaal(lokaalArray.getJSONObject(i)));
		}
	}

	public class Vak
	{
		public Integer Id;
		public String Naam;

		public Vak(JSONObject vak)
		{
			Id = getNullableInt(vak, "Id");
			Naam = getNullableString(vak, "Naam");
		}
	}

	public class Docent
	{
		public Integer Id;
		public String Naam;
		public String Docentcode;

		public Pattern achternaamPattern = Pattern.compile("\\w+$");

		public Docent(JSONObject vak)
		{
			Id = getNullableInt(vak, "Id");
			Naam = getNullableString(vak, "Naam");
			Docentcode = getNullableString(vak, "Docentcode");
		}
	}

	public class Lokaal
	{
		public String Naam;

		public Lokaal(JSONObject lokaal)
		{
			Naam = getNullableString(lokaal, "Naam");
		}
	}

	public Boolean isOp(Date dag)
	{
		return testformat.format(Start).equals(testformat.format(dag))
			|| testformat.format(Einde).equals(testformat.format(dag))
			|| (dag.after(Start) && dag.before(Einde));
	}

	public String getDocenten()
	{
		String result = "";

		for (Docent docent : Docenten)
		{
			result += docent.Naam.charAt(0);
			result += ". ";
			result += docent.achternaamPattern.matcher(docent.Naam).toString();
			result += ", ";
		}

		if (result.isEmpty()) return result;

		return result.substring(0, -2);
	}
}
