package eu.magisterapp.magisterapi;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by max on 2-12-15.
 */
public class Cijfer extends Module
{

    public final Integer CijferId;
    public final String CijferStr;
    public final boolean IsVoldoende;

    // public final <iets> IngevoerdDoor; (is null bij mijn cjifers, misschien string (HAB), misschien docent Id)

    public final LocalDate DatumIngevoerd;

    public final Integer CijferPeriodeId;
    // Er staat CijferPeriode, maar die is vrij incompleet veregeleken met onze CijferPerioden instances.
    // TODO: maak hier een instance van CijferPerioden.CijferPeriode

    public final Vak Vak;
    public final boolean Inhalen;
    public final boolean Vrijstelling;
    public final boolean TeltMee;

    public final CijferKolom CijferKolom;
    public final Integer CijferKolomIdEloOpdracht;
    public final String Docent;
    public final String Docentcode; // hetzelfde als docent, maar dan voor consitency met Afspraak.Docent
    public final boolean VakDispensatie;
    public final boolean VakVrijstelling;

    public Cijfer(JSONObject cijferJson, VakList vaklist) throws ParseException
    {

        CijferId = getNullableInt(cijferJson, "CijferId");
        CijferStr = getNullableString(cijferJson, "CijferStr");
        IsVoldoende = getNullableBoolean(cijferJson, "IsVoldoende");
        DatumIngevoerd = getNullableDate(cijferJson, "DatumIngevoerd");
        CijferPeriodeId = getNullableInt(cijferJson, "CijferPeriodeId");

        Integer vakId = cijferJson.getJSONObject("Vak").getInt("Id");
        Vak = vaklist.getById(vakId);

        Inhalen = getNullableBoolean(cijferJson, "Inhalen");
        Vrijstelling = getNullableBoolean(cijferJson, "Vrijstelling");
        TeltMee = getNullableBoolean(cijferJson, "TeltMee");

        CijferKolom = new CijferKolom(cijferJson.getJSONObject("CijferKolom"));

        CijferKolomIdEloOpdracht = getNullableInt(cijferJson, "CijferKolomIdEloOpdracht");
        Docent = Docentcode = getNullableString(cijferJson, "Docent");
        VakDispensatie = getNullableBoolean(cijferJson, "VakDispensatie");
        VakVrijstelling = getNullableBoolean(cijferJson, "VakVrijstelling");
    }

    public class CijferKolom
    {
        public final Integer Id;
        public final String KolomNaam;
        public final String KolomNummer;
        public final String KolomVolgNummer;
        public final String KolomKop;
        public final String KolomOmschrijving;
        public final Integer KolomSoort;
        public final boolean IsHerkansingKolom;
        public final boolean IsDocentKolom;
        public final boolean HeeftOnderliggendeKolommen;
        public final boolean IsPTAKolom;

        public CijferKolom(JSONObject kolomJson)
        {
            Id = getNullableInt(kolomJson, "Id");
            KolomNaam = getNullableString(kolomJson, "KolomNaam");
            KolomNummer = getNullableString(kolomJson, "KolomNummer");
            KolomVolgNummer = getNullableString(kolomJson, "KolomVolgNummer");
            KolomKop = getNullableString(kolomJson, "KolomKop");
            KolomOmschrijving = getNullableString(kolomJson, "KolomOmschrijving");
            KolomSoort = getNullableInt(kolomJson, "KolomSoort");
            IsHerkansingKolom = getNullableBoolean(kolomJson, "IsHerkansingKolom");
            IsDocentKolom = getNullableBoolean(kolomJson, "IsDocentKolom");
            HeeftOnderliggendeKolommen = getNullableBoolean(kolomJson, "HeeftOnderliggendeKolommen");
            IsPTAKolom = getNullableBoolean(kolomJson, "IsPTAKolom");
        }

    }

}
