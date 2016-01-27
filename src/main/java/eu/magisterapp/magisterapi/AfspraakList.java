package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;

import java.util.*;

import eu.magisterapp.magisterapi.afwijkingen.AfwijkingInterface;


/**
 * Created by max on 24-11-15.
 */
public class AfspraakList extends ArrayList<Afspraak> {

    public AfspraakList() {}

    public AfspraakList(List<Afspraak> afspraken)
    {
        this.addAll(afspraken);
    }

    public Afspraak first()
    {
        if (this.size() == 0) return null;

        return get(0);
    }

    public DateTime getFirstDayTime()
    {
        if (this.size() == 0) return null;

        return this.get(0).getDay();
    }

    public AfspraakList scewTimeTable(AfwijkingInterface fixer)
    {
        for (Afspraak afspraak : this)
        {
            fixer.modify(afspraak);
        }

        return this;
    }

    public Iterator<AfspraakList> dayIterator()
    {
        return new Iterator<AfspraakList>() {

            int current = 0; // eerste element
            String currDay = hasNext() ? get(current).getDateString() : ""; // eerste dag


            @Override
            public boolean hasNext() {
                return current < size();
            }

            @Override
            public AfspraakList next() {

                if (! hasNext()) throw new NoSuchElementException("There are no more elements left.");

                AfspraakList collection = new AfspraakList();

                while (hasNext())
                {
                    if (! currDay.equals(currDay = getCurrentDay(current))) break;

                    collection.add(get(current++));
                }

                return collection;
            }

            private String getCurrentDay(int i)
            {
                return get(i).getDateString();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void applyRoosterWijzigingen(AfspraakList wijzigingen)
    {
        Map<Integer, Integer> idToIndex = new HashMap<>();

        for (int i = 0; i < size(); i++)
        {
            idToIndex.put(get(i).Id, i);
        }

        for (Afspraak wijziging : wijzigingen)
        {
            Integer index;

            if ((index = idToIndex.get(wijziging.Id)) != null) // als er een overeenkomstig id in die lijst zit
            {
                set(index, wijziging); // tyf oude weg, en zet nieuwe er voor in de plaats.
            }
        }
    }


}
