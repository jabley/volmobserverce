/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm.util;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * A case insensitive {@link Dictionary} / {@link Map}.
 */
public class CaseInsensitiveDictionary
        extends ExtendedDictionary {

    /**
     * The underlying map.
     */
    private final Map map;

    /**
     * Initialise.
     */
    public CaseInsensitiveDictionary() {
        map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Initialise.
     *
     * @param map The map whose entries will be copied into this.
     */
    private CaseInsensitiveDictionary(Map map) {
        this();
        if (map != null) {
            this.map.putAll(map);
        }
    }

    /**
     * Initialise.
     *
     * @param dictionary The dictionary whose entries will be copied into this.
     */
    public CaseInsensitiveDictionary(Dictionary dictionary) {
        this();

        Enumeration enumeration = dictionary.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            map.put(key, dictionary.get(key));
        }
    }

    /**
     * Initialise.
     *
     * @param dictionary The dictionary whose entries will be copied into this.
     */
    public CaseInsensitiveDictionary(ExtendedDictionary dictionary) {
        this((Map) dictionary);
    }

    /**
     * Initialise.
     *
     * @param dictionary The dictionary whose entries will be copied into this.
     */
    public CaseInsensitiveDictionary(Hashtable dictionary) {
        this((Map) dictionary);
    }

    // Javadoc inherited.
    public int size() {
        return map.size();
    }

    // Javadoc inherited.
    public boolean isEmpty() {
        return map.isEmpty();
    }

    // Javadoc inherited.
    public Enumeration elements() {
        return new IteratorEnumerationAdapter(map.values().iterator());
    }

    // Javadoc inherited.
    public Enumeration keys() {
        return new IteratorEnumerationAdapter(map.keySet().iterator());
    }

    // Javadoc inherited.
    public Object get(Object key) {
        return map.get(key);
    }

    // Javadoc inherited.
    public Object remove(Object key) {
        return map.remove(key);
    }

    // Javadoc inherited.
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    // Javadoc inherited.
    public void clear() {
        map.clear();
    }

    // Javadoc inherited.
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    // Javadoc inherited.
    public Set entrySet() {
        return map.entrySet();
    }

    // Javadoc inherited.
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    // Javadoc inherited.
    public Collection values() {
        return map.values();
    }

    // Javadoc inherited.
    public void putAll(Map map) {
        this.map.putAll(map);
    }

    /**
     * Dictionary equivalent of {@link #putAll(Map)}.
     *
     * @param dictionary Mappings to be stored in this.
     */
    public void putAll(Dictionary dictionary) {
        if (dictionary instanceof CaseInsensitiveDictionary) {
            CaseInsensitiveDictionary other =
                    (CaseInsensitiveDictionary) dictionary;
            map.putAll(other.map);
        } else if (dictionary instanceof Map) {
            map.putAll((Map) dictionary);
        } else {
            Enumeration enumeration = dictionary.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                Object value = dictionary.get(key);
                map.put(key, value);
            }
        }
    }

    // Javadoc inherited.
    public Set keySet() {
        return map.keySet();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        return map.equals(obj);
    }

    // Javadoc inherited.
    public int hashCode() {
        return map.hashCode();
    }

    // Javadoc inherited.
    public String toString() {
        return map.toString();
    }
}
