package eu.magisterapp.magisterapi;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sibren Talens <me@sibrentalens.com>
 * Krijg een instance van deze class door MagisterAPI.getAfspraken();
 */
public class Afspraak {
	// Ik heb deze namen ook niet bedacht, dit is hoe ze in de API staan,
	// en het leek me wel zo netjes om die te houden
	public int Id;
	public Date Start, Einde;
	public boolean DuurtHeleDag;
	public String Omschrijving;
	public String Lokatie;
	public int Status;
	public int WeergaveType;
	public String Inhoud;
	public int InfoType;
	public String Aantekening;
	public boolean Afgerond;

	// Dit zijn arrays met daarin weer JSON objects,
	// Ik denk voor als je meerdere lessen op een uur hebt
	public String[] Vakken;
	public String[] Docenten;
	public String[] Lokalen;
	public int OpdrachtId;
	public boolean HeeftBijlagen;

	// 6 is wss vakantie en volgens mata is 1 huiswerk, 3 tentamen, 4 schriftelijk en 13 is denk ik les
	// TODO Maak hier een mooie enum van
	public int Type;

	// TODO Vind de datatypen uit van de velden die null zijn

	public Afspraak(MagisterConnection con, Date start, Date end){
		Response afspraakResponse = con.get("kl");
	}
}
