package eu.magisterapp.magisterapi;

import java.io.IOException;

/**
 * Created by max on 19-10-15.
 */
public class BadResponseException extends IOException
{
    public BadResponseException(String message)
    {
        super(message);
    }
}
