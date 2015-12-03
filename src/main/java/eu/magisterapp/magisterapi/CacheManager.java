package eu.magisterapp.magisterapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 2-12-15.
 */
public class CacheManager<T> {

    private Map<String, T> cacheRepository = new HashMap<String, T>();
    private Map<String, Long> expiration = new HashMap<String, Long>();

    public final Long expiry; // ms

    public CacheManager(Long expiry)
    {
       this.expiry = expiry;
    }

    public CacheManager()
    {
        this(60*1000L); // 1 minute
    }

    public boolean has(String key)
    {
        if (expiration.get(key) == null) return false;

        if (expiration.get(key) > time())
        {
            expire(key);
            return false;
        }

        return cacheRepository.get(key) != null;
    }

    public T get(String key)
    {
        return cacheRepository.get(key);
    }

    public T put(String key, T response)
    {
        cacheRepository.put(key, response);
        expiration.put(key, getNewExpiration());

        return response;
    }

    private Long time()
    {
        return System.currentTimeMillis();
    }

    private void expire(String key)
    {
        cacheRepository.remove(key);
        expiration.remove(key);
    }

    private Long getNewExpiration()
    {
        return time() + expiry;
    }

    public void clear()
    {
        cacheRepository = new HashMap<String, T>();
        expiration = new HashMap<String, Long>();
    }

}
