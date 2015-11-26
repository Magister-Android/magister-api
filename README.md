# magister-api
The API that our App will be using to fetch data from a magister site.


# Dit is hoe dingen gedaan zijn:

De API wordt gebruikt om shit op te halen. De parent code is verantwoordelijk
voor het cachen hiervan (in ons geval is dat onze [Magister app](https://github.com/Magister-Android/Magister-Android)).

## Het ophalen van lessen

Het ophalen van lesen kan op de volgende manier(en) gedaan worden:

```java

MagisterAPI api = new MagisterAPI(...);

// haal afspraken van nu tot en met over 1 week op.
AfspraakCollection afspraken = api.getAfspraken(Utils.now(), Utils.deltaDays(7));

```

Voor een iteratie over elke afspraak kan het volgende gedaan worden:

```java

Iterator<Afspraak> it = afspraken.iterator();

while (it.hasNext())
{
	Afspraak afspraak = it.next();

	//
}

// Dit is hetzelfde als dit:

for (Afspraak afspraak : afspraken)
{
	//
}

```

Om de afspraken dag voor dag te doorlopen kan dit worden gedaan:

```java

Iterator<AfspraakCollection> it = afspraken.dayIterator();

while (it.hasNext())
{
	AfspraakCollection dag = it.next();

	// dag is een collection met alle afspraken voor die dag. Je kan hiervan een JodaTime LocalDate instance krijgen:

	String dag = dag.getFirstDay().toString("EEEE"); // Monday, Tuesday.. etc

	// Hierna kun je over elke dag itereren zoals in het bovenstaande voorbeeld aangegeven

	for (Afspraak afspraak : dag)
	{
		//
	}
}

```
