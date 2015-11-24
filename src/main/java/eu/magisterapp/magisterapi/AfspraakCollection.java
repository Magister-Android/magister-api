package eu.magisterapp.magisterapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by max on 24-11-15.
 */
public class AfspraakCollection implements Iterable<Afspraak> {

    public List<Afspraak> afspraken;

    public AfspraakCollection()
    {
        this.afspraken = new ArrayList<Afspraak>();
    }

    public AfspraakCollection(List<Afspraak> afspraken)
    {
        this.afspraken = afspraken;
    }

    public void add(Afspraak afspraak)
    {
        afspraken.add(afspraak);
    }

    public Integer getFirstDay()
    {
        if (afspraken.size() == 0) return null;

        return afspraken.get(0).getDayConstant();
    }

    public Iterator<Afspraak> iterator() {
        return new Iterator<Afspraak>() {

            int current = 0;

            @Override
            public boolean hasNext() {
                return current < afspraken.size();
            }

            @Override
            public Afspraak next() {
                if (hasNext()) {
                    return afspraken.get(current++);
                }

                throw new NoSuchElementException("There are no more elements left.");
            }

            public boolean isLastOfDay() {
                return !(hasNext() && getCurrentDay().equals(afspraken.get(current + 1).getDayConstant()));
            }

            public Integer getCurrentDay() {
                return afspraken.get(current).getDayConstant();
            }
        };
    }

    public Iterator<AfspraakCollection> dayIterator()
    {
        return new Iterator<AfspraakCollection>() {

            int current = 0; // eerste element
            int currDay = afspraken.get(current).getDayConstant(); // eerste dag


            @Override
            public boolean hasNext() {
                return current < afspraken.size();
            }

            @Override
            public AfspraakCollection next() {

                if (! hasNext()) throw new NoSuchElementException("There are no more elements left.");

                AfspraakCollection collection = new AfspraakCollection();

                while (hasNext())
                {
                    if (currDay != (currDay = getCurrentDay(current))) break;

                    collection.add(afspraken.get(current++));
                }

                return collection;
            }

            private int getCurrentDay(int i)
            {
                return afspraken.get(i).getDayConstant();
            }
        };
    }


}
