package eu.magisterapp.magisterapi;

import org.joda.time.DateTime;

/**
 * Created by max on 20-12-15.
 */
public interface Displayable {

    public static enum Type
    {
        NORMAL, // geen speciale opmaak
        INVALID, // rode tekst met streep er doorheen
        NOTICE, // rode tekst
        WARNING // rode achtergrond
    }

    public String getVak();

    public String getTitle();

    public String getDocent();

    public String getTime();

    public DateTime getTimeInstance();

    public Type getType();
}
