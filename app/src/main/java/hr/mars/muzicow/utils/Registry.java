package hr.mars.muzicow.utils;

import java.util.HashMap;

/**
 * Created by Emil on 12.1.2016..
 */
public class Registry {

    volatile HashMap<String, Object> items = new HashMap<String, Object>();

    private static volatile Registry instance = null;

    protected Registry() {
        // Exists only to defeat instantiation.
    }

    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }
    /**
     * Methods for setting and gettng "alive" objects
     * in aplication
     * @param key   key for hashmap
     * @param value   value of object for hashmap
     */
    public void set(String key, Object value) {
        this.items.put(key, value);
    }

    public Object get(String key) {
        return this.items.get(key);
    }

}
