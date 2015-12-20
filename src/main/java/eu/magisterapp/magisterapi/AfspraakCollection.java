package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;

import java.util.*;


/**
 * Created by max on 24-11-15.
 */
public class AfspraakCollection extends ArrayList<Afspraak> {

    public AfspraakCollection() {}

    public AfspraakCollection(List<Afspraak> afspraken)
    {
        this.addAll(afspraken);
    }

    public DateTime getFirstDay()
    {
        if (this.size() == 0) return null;

        return this.get(0).getDay();
    }

    public Iterator<AfspraakCollection> dayIterator()
    {
        return new Iterator<AfspraakCollection>() {

            int current = 0; // eerste element
            String currDay = hasNext() ? get(current).getDateString() : ""; // eerste dag


            @Override
            public boolean hasNext() {
                return current < size();
            }

            @Override
            public AfspraakCollection next() {

                if (! hasNext()) throw new NoSuchElementException("There are no more elements left.");

                AfspraakCollection collection = new AfspraakCollection();

                while (hasNext())
                {
                    if (!Objects.equals(currDay, currDay = getCurrentDay(current))) break;

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
}
