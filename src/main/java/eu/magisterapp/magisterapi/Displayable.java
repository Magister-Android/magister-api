package eu.magisterapp.magisterapi;

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

    public Type getType();
}
