package eu.magisterapp.magisterapi;

/**
 * Created by max on 19-10-15.
 */
public class BadResponseException extends Exception
{
    public BadResponseException(String message)
    {
        super(message);
    }
}
