# magister-api
The API that our App will be using to fetch data from a magister site.


# Dit is misschien hoe we dingen gaan doen:

We kunnen in de API steeds een dag tegelijk ophalen ofzo:

```java

MagisterAPI api = new MagisterAPI(...)

api.getAfspraken("29-10-2015");

```

Dit doet dan een lookup in een SQLite db:

```sql
SELECT * FROM afspraken WHERE date = "29-10-2015"
```

Als de resultaten hiervan niet bestaan, haal de betreffende week op van magister, en tyf dat in de db.

```java
if (results.length() == 0)
{
	// in MagisterAPI
	AfspraakList afspraken = MagisterConnection.getAfspraken(getStartOfWeek("29-10-2015"), getEndOfWeek("29-10-2015"));

	sqlitedb.insert(afspraken);

	return afspraken.getForDay("29-10-2015");
}
```
