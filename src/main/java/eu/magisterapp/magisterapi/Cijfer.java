package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by max on 2-12-15.
 */
public class Cijfer extends Module implements Displayable
{

    public final Aanmelding aanmelding;

    public final Integer CijferId;
    public final String CijferStr;
    public final Boolean IsVoldoende;

    // public final <iets> IngevoerdDoor; (is null bij mijn cjifers, misschien string (HAB), misschien docent Id)

    public final DateTime DatumIngevoerd;

    public final Integer CijferPeriodeId;
    // Er staat CijferPeriode, maar die is vrij incompleet veregeleken met onze CijferPerioden instances.
    // TODO: maak hier een instance van CijferPerioden.CijferPeriode

    public final Vak Vak;
    public final Boolean Inhalen;
    public final Boolean Vrijstelling;
    public final Boolean TeltMee;

    public final CijferKolom CijferKolom;
    public final Integer CijferKolomIdEloOpdracht;
    public final String Docent;
    public final String Docentcode; // hetzelfde als docent, maar dan voor consitency met Afspraak.Docent
    public final Boolean VakDispensatie;
    public final Boolean VakVrijstelling;

    public transient CijferInfo info = null;

    public Cijfer(JSONObject cijferJson, VakList vaklist, Aanmelding aanmelding) throws ParseException, JSONException
    {
        this.aanmelding = aanmelding;

        CijferId = getNullableInt(cijferJson, "CijferId");
        CijferStr = getNullableString(cijferJson, "CijferStr");
        IsVoldoende = getNullableBoolean(cijferJson, "IsVoldoende") != null
                ? getNullableBoolean(cijferJson, "IsVoldoende")
                : getNullableBoolean(cijferJson, "IsCijferVoldoende");

        DatumIngevoerd = getNullableDate(cijferJson, "DatumIngevoerd");
        CijferPeriodeId = getNullableInt(cijferJson, "CijferPeriodeId");

        Integer vakId = cijferJson.getJSONObject("Vak").getInt("Id");
        Vak = vaklist.getById(vakId);

        Inhalen = getNullableBoolean(cijferJson, "Inhalen");
        Vrijstelling = getNullableBoolean(cijferJson, "Vrijstelling");
        TeltMee = getNullableBoolean(cijferJson, "TeltMee");

        if (! cijferJson.isNull("CijferKolom"))
            CijferKolom = new CijferKolom(cijferJson.getJSONObject("CijferKolom"));
        else
            CijferKolom = null;

        CijferKolomIdEloOpdracht = getNullableInt(cijferJson, "CijferKolomIdEloOpdracht");
        Docent = Docentcode = getNullableString(cijferJson, "Docent");
        VakDispensatie = getNullableBoolean(cijferJson, "VakDispensatie");
        VakVrijstelling = getNullableBoolean(cijferJson, "VakVrijstelling");
    }

    public class CijferKolom implements Serializable
    {
        public final Integer Id;
        public final String KolomNaam;
        public final String KolomNummer;
        public final String KolomVolgNummer;
        public final String KolomKop;
        public final String KolomOmschrijving;
        public final Integer KolomSoort;
        public final Boolean IsHerkansingKolom;
        public final Boolean IsDocentKolom;
        public final Boolean HeeftOnderliggendeKolommen;
        public final Boolean IsPTAKolom;

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

    public class CijferInfo implements Serializable
    {
        public final Integer parent = CijferId;

        public final Integer KolomSoortKolom;
        public final String KolomNaam;
        public final String KolomKopnaam;
        // public final Object KolomNiveau;
        public final String KolomOmschrijving;
        public final Integer Weging;
        public final DateTime WerkinformatieDatumIngevoerd;
        public final String WerkInformatieOmschrijving;

        public CijferInfo(Response response) throws ParseException
        {
            JSONObject json = response.getJson();

            KolomSoortKolom = getNullableInt(json, "KolomSoortKolom");
            KolomNaam = getNullableString(json, "KolomNaam");
            KolomKopnaam = getNullableString(json, "KolomKopnaam");
            KolomOmschrijving = getNullableString(json, "KolomOmschrijving");
            Weging = getNullableInt(json, "Weging");
            WerkinformatieDatumIngevoerd = getNullableDate(json, "WerkinformatieDatumIngevoerd");
            WerkInformatieOmschrijving = getNullableString(json, "WerkInformatieOmschrijving");
        }
    }

    public boolean hasInfo()
    {
        return this.info != null;
    }

    public void setInfo(CijferInfo info)
    {
        this.info = info;

        // Nu is er genoeg informatie om gemiddelde uit te rekenen
        if (CijferKolom.KolomSoort == 3) // PTA kolom (denk ik)

            Vak.addCijfer(Utils.parseFloat(CijferStr), info.Weging);
    }

    @Override
    public String getVak() {
        return Vak.Omschrijving;
    }

    @Override
    public String getTitle() {
        return CijferStr;
    }

    @Override
    public String getDocent() {
        return Docent != null ? Docent : Vak.Docent;
    }

    @Override
    public String getTime() {
        return DatumIngevoerd.toString("yyyy-MM-dd");
    }

    @Override
    public Type getType() {
        if (IsVoldoende != null && ! IsVoldoende) return Type.NOTICE;

        return Type.NORMAL;
    }

    @Override
    public DateTime getTimeInstance() {
        return DatumIngevoerd;
    }
}
