package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sibren Talens <me@sibrentalens.com>
 * Krijg een instance van deze class door MagisterAPI.getAfspraken();
 */
public class Afspraak extends Module implements Displayable {

	public static Map<Integer, Type> typeMap = new HashMap<>();
	public static Map<Integer, Status> statusMap = new HashMap<>();
	public static Map<Integer, InfoType> infoTypeMap = new HashMap<>();
	public static Map<Integer, WeergaveType> weergaveTypeMap = new HashMap<>();

	public enum Type
	{
		NONE(0),
		PERSOONLIJK(1),
		ALGEMEEN(2),
		SCHOOLBREED(3),
		STAGE(4),
		INTAKE(5),
		ROOSTERVRIJ(6),
		KWT(7),
		STANDBY(8),
		BLOKKADE(9),
		OVERIG(10),
		BLOKKADELOKAAL(11),
		BLOKKADEKLAS(12),
		LES(13),
		STUDIEHUIS(14),
		ROOSTERVRIJESTUDIE(15),
		PLANNING(16),
		MAATREGELEN(101),
		PRESENTIES(102),
		EXAMENROOSTER(103);

		Type(int id)
		{
			typeMap.put(id, this);
		}
	}

	public enum Status
	{
		GEENSTATUS(0),
		GEROOSTERDAUTOMATISCH(1),
		GEROOSTERDHANDMATIG(2),
		GEWIJZIGD(3),
		VERVALLENHANDMATIG(4),
		VERVALLENAUTOMATISCH(5),
		INGEBRUIK(6),
		AFGESLOTEN(7),
		INGEZET(8),
		VERPLAATST(9),
		GEWIJZIGDENVERPLAATST(10);

		Status(int id)
		{
			statusMap.put(id, this);
		}
	}

	public enum InfoType
	{
		NONE(0),
		HUISWERK(1),
		PROEFWERK(2),
		TENTAMEN(3),
		SCHIFTELIJKEOVERHORING(4),
		MONDELINGEOVERHORING(5),
		INFORMATIE(6),
		AANTEKENING(7);

		InfoType(int id)
		{
			infoTypeMap.put(id, this);
		}
	}

	public enum WeergaveType
	{
		VRIJ(1),
		VOORLOPIGBEZET(2),
		BEZET(3),
		NIETAANWEZIG(4);

		WeergaveType(int id)
		{
			weergaveTypeMap.put(id, this);
		}
	}

	// Ik heb deze namen ook niet bedacht, dit is hoe ze in de API staan,
	// en het leek me wel zo netjes om die te houden
	public int Id;
	public DateTime Start, Einde;
	public Integer LesuurVan, LesuurTotMet;
	public boolean DuurtHeleDag;
	public String Omschrijving;
	public String Lokatie;
	public String Locatie; // Do you even spelling?? Schoolmaster?? Hello bic boi??
	public Status Status;
	public WeergaveType WeergaveType;
	public String Inhoud;
	public InfoType InfoType;
	public String Aantekening;
	public boolean Afgerond;

	// Dit zijn arrays met daarin weer JSON objects,
	// Voor ACFuck en shit
	public List<Vak> Vakken = new ArrayList<>();
	public List<Docent> Docenten = new ArrayList<>();
	public List<Lokaal> Lokalen = new ArrayList<>();

	public int OpdrachtId;
	public boolean HeeftBijlagen;

	protected static SimpleDateFormat testformat = new SimpleDateFormat("yyyy-MM-dd");

	// 6 is wss vakantie en volgens mata is 1 huiswerk, 3 tentamen, 4 schriftelijk en 13 is denk ik les
	public Type Type;

	// TODO Vind de datatypen uit van de velden die null zijn

	/**
	 * Create a new afspraak form a JSONObject
	 * @param  afspraak       JSONObject as provided by org.json
	 * @throws ParseException If parsing fails
	 */
	public Afspraak(JSONObject afspraak) throws ParseException, JSONException
	{
		parseResponse(afspraak);
	}

	/**
	 * Parse the response object and set the local variables
	 * @param  afspraak       The JSONObject
	 * @throws ParseException If parsing fails
	 */
	protected void parseResponse(JSONObject afspraak) throws ParseException, JSONException {
		Id = getNullableInt(afspraak, "Id");
		Start = getNullableDate(afspraak, "Start");
		Einde = getNullableDate(afspraak, "Einde");
		LesuurVan = getNullableInt(afspraak, "LesuurVan");
		LesuurTotMet = getNullableInt(afspraak, "LesuurTotMet");
		DuurtHeleDag = getNullableBoolean(afspraak, "DuurtHeleDag");
		Omschrijving = getNullableString(afspraak, "Omschrijving");
		Lokatie = Locatie = getNullableString(afspraak, "Lokatie");
		Status = statusMap.get(getNullableInt(afspraak, "Status"));
		WeergaveType = weergaveTypeMap.get(getNullableInt(afspraak, "WeergaveType"));
		Inhoud = getNullableString(afspraak, "Inhoud");
		InfoType = infoTypeMap.get(getNullableInt(afspraak, "InfoType"));
		Aantekening = getNullableString(afspraak, "Aantekening");
		Afgerond = getNullableBoolean(afspraak, "Afgerond");
		OpdrachtId = getNullableInt(afspraak, "OpdrachtId");
		HeeftBijlagen = getNullableBoolean(afspraak, "HeeftBijlagen");
		Type = typeMap.get(getNullableInt(afspraak, "Type"));

		JSONArray vakArray = afspraak.getJSONArray("Vakken");

		for (Integer i = 0; i < vakArray.length(); i++) {
			Vakken.add(new Vak(vakArray.getJSONObject(i)));
		}

		JSONArray docentArray = afspraak.getJSONArray("Docenten");
		for (Integer i = 0; i < docentArray.length(); i++) {
			Docenten.add(new Docent(docentArray.getJSONObject(i)));
		}

		JSONArray lokaalArray = afspraak.getJSONArray("Lokalen");
		for (Integer i = 0; i < lokaalArray.length(); i++) {
			Lokalen.add(new Lokaal(lokaalArray.getJSONObject(i)));
		}
	}

	/**
	 * Subclass Vak
	 * to store the subject and its id
	 */
	public class Vak implements Serializable
	{
		public Integer Id;
		public String Naam;

		public Vak(JSONObject vak)
		{
			Id = getNullableInt(vak, "Id");
			Naam = getNullableString(vak, "Naam");
		}
	}

	/**
	 * Subclass Docent
	 */
	public class Docent implements Serializable
	{
		public Integer Id;
		public String Naam;
		public String Docentcode;

		public Docent(JSONObject vak)
		{
			Id = getNullableInt(vak, "Id");
			Naam = getNullableString(vak, "Naam");
			Docentcode = getNullableString(vak, "Docentcode");
		}


		/**
		 * Return the shortened full name of a teacher
		 * @return The shortened full name
		 */
		public String getFullName()
		{
			if (Naam.isEmpty()) return "";

			if (! Naam.contains(" ")) return Docentcode; // De aardige mensen op het zernike hebben een typfout gemaakt.

			return Naam.charAt(0) + ". " + Naam.substring(Naam.lastIndexOf(' ') + 1);
		}
	}

	/**
	 * Subclass Lokaal
	 */
	public class Lokaal implements Serializable
	{
		public String Naam;

		/**
		 * [Lokaal description]
		 * @param  lokaal [description]
		 */
		public Lokaal(JSONObject lokaal)
		{
			Naam = getNullableString(lokaal, "Naam");
		}
	}

	public Boolean isOp(DateTime dag)
	{
		return testformat.format(Start).equals(testformat.format(dag))
			|| testformat.format(Einde).equals(testformat.format(dag))
			|| (dag.isAfter(Start) && dag.isBefore(Einde));
	}

	public String getDocenten()
	{
		String result = "";

		for (Docent docent : Docenten)
		{
			result += docent.getFullName() + ", ";
		}

		if (result.isEmpty()) return result;

		return result.substring(0, result.length() - 2);
	}

	public String getVakken()
	{
		String result = "";

		for (Vak vak : Vakken)
		{
			result += vak.Naam + ", ";
		}

		if (result.isEmpty() || result.length() > 20) return Omschrijving;

		return result.substring(0, result.length() - 2);
	}

	public String getLokalen()
	{
		String result = "";

		for (Lokaal lokaal : Lokalen)
		{
			result += lokaal.Naam + ", ";
		}

		if (result.isEmpty()) return Locatie;

		return result.substring(0, result.length() - 2);
	}

	public Boolean valtUit()
	{
		return Status == Status.GEENSTATUS || Status == Status.VERVALLENAUTOMATISCH || Status == Status.VERVALLENHANDMATIG;
	}

	public DateTime getDay()
	{
		return Start;
	}

	public String getDateString()
	{
		return getDay().toString("yyyy-MM-dd");
	}

	public Integer getDayConstant()
	{
		return getDay().dayOfWeek().get();
	}

	@Override
	public String getVak() {
		return getVakken();
	}

	@Override
	public String getTitle() {
		return getLokalen();
	}

	@Override
	public String getDocent() {
		return getDocenten();
	}

	@Override
	public String getTime() {
		return Start.toString("HH:mm") + " - " + Einde.toString("HH:mm");
	}

	@Override
	public Displayable.Type getType() {
		// TODO: test voor uitval, en return NOTICE oid
		return Displayable.Type.NORMAL;
	}

	@Override
	public DateTime getTimeInstance() {
		return Start;
	}
}
