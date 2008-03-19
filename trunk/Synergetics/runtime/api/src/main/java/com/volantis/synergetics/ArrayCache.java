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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * ArrayCache This is a very simple map implementation which can be used for
 * caches.  The cache maintains two arrays, one for keys and the other for
 * values. <br /> Values are added to the cache via put(), removed via remove()
 * and retrieved via get(). No attempt is made to be threadsafe. <br /> There
 * are two constructors to the class, a standard empty constructor and a
 * constructor that takes a size parameter. The size constructor allocates the
 * two arrays to that size and takes the value as a block size. Once the arrays
 * have become full from putting items into the cache, the arrays are resized
 * by adding a further blocksize elements to the end of the arrays.
 *
 * @deprecated Use normal map, or a proper cache.
 */
public class ArrayCache {

    /**
     * The default cache size
     */
    private static final int DEFAULT_CACHE_SIZE = 10;

    /**
     * The array of keys
     */
    private Object[] keys;

    /**
     * The values mapping to each key
     */
    private Object[] values;

    /**
     * The increment size to grow the cache by when full
     */
    private int blockSize;

    /**
     * The number of keys in the cache
     */
    private int count;

    /**
     * The number of elements allowed in the cache
     */
    private int size;

    /**
     * Create an array cache, default to 10 elements in the array
     */
    public ArrayCache() {
        this(DEFAULT_CACHE_SIZE);
    }

    /**
     * Create an array cache with the specified number of elements
     *
     * @param size the initial cache size
     */
    public ArrayCache(int size) {
        this.size = size;
        this.blockSize = size;
        count = 0;
        keys = new Object[size];
        values = new Object[size];
    }

    /**
     * Determine whether the cache has any entries
     *
     * @return true if the cache is empty, false otherwise
     */
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * Clear the cache
     */
    public void clear() {
        keys = new Object[blockSize];
        values = new Object[blockSize];
        count = 0;
    }

    /**
     * Get an object with a specific key.
     *
     * @param key the key of the cached object
     * @return the object with the given key or null if it does not exist
     */
    public Object get(Object key) {
        Object o = null;
        int idx = keyIndex(key);

        if (idx > -1) {
            o = values[idx];
        }

        return o;
    }

    /**
     * Determine whether a key exists in this cache and if it does, return the
     * index of it.
     *
     * @param key the cache key
     * @return the index of the key if it exists, -1 otherwise
     */
    public int keyIndex(Object key) {
        int idx = -1;

        if (key != null) {
            int i = 0;

            while ((idx == -1) && (i < size)) {
                if (key.equals(keys[i])) {
                    idx = i;
                }

                i++;
            }
        }

        return idx;
    }

    /**
     * Add an object to the cache
     *
     * @param key   the key of the item to add
     * @param value the item to cache
     * @return the object that was previously mapped to this key or null if
     *         there wasnt one
     */
    public Object put(Object key, Object value) {
        Object oldObject = null;

        if (key != null) {
            int idx = keyIndex(key);

            if (idx == -1) {
                if (count == size) {
                    Object[] newKeys = new Object[size + blockSize];
                    Object[] newValues = new Object[size + blockSize];

                    for (int i = 0; i < size; i++) {
                        newKeys[i] = keys[i];
                        newValues[i] = values[i];
                    }

                    keys = newKeys;
                    values = newValues;
                    size += blockSize;
                }

                keys[count] = key;
                values[count] = value;
                count++;
            } else {
                oldObject = values[idx];
                values[idx] = value;
            }
        }

        return oldObject;
    }

    /**
     * Add the contents of a map to the cache
     *
     * @param t the map to add
     */
    public void putAll(Map t) {
        Set keys = t.keySet();
        Iterator keyIter = keys.iterator();

        while (keyIter.hasNext()) {
            Object k = keyIter.next();
            Object v = t.get(k);
            put(k, v);
        }
    }

    /**
     * Remove an item from the cache. This is achieved by moving all the
     * elements above the item index down one position and removing the item at
     * the end. Adding elements then always adds to the end of the array. This
     * will be slow for large  caches. An alternative approach could be to set
     * the key and value to null and keep track  of which elements are
     * available for re-use. Adding an element can then use an empty slot if
     * one is available. Searching for items using keyIndex() would need to
     * handle (skip) null values which are currently not allowed.
     *
     * @param key the key of the item to remove
     * @return the value that has been removed
     */
    public Object remove(Object key) {
        Object oldObject = null;

        if (key != null) {
            int idx = keyIndex(key);

            if (idx > -1) {
                oldObject = values[idx];

                if (idx == (count - 1)) {
                    keys[idx] = null;
                    values[idx] = null;
                } else {
                    for (int i = idx + 1; i < count; i++) {
                        keys[i - 1] = keys[i];
                        values[i - 1] = values[i];
                    }

                    keys[count - 1] = null;
                    values[count - 1] = null;
                }

                count--;
            }
        }

        return oldObject;
    }

    /**
     * Return the number of elements in the cache
     *
     * @return the number of cache elements
     */
    public int size() {
        return count;
    }

    /**
     * Determine whether a value exists in this cache
     *
     * @param value the cached value
     * @return the index of the value if it exists, -1 otherwise
     */
    public int valueIndex(Object value) {
        int idx = -1;

        if (value != null) {
            int i = 0;

            while ((idx == -1) && (i < size)) {
                if (value.equals(values[i])) {
                    idx = i;
                }

                i++;
            }
        }

        return idx;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-03	66/3	steve	VBM:2003082105 Rework and code tidy up

 29-Aug-03    66/1    steve    VBM:2003082105 ArrayCache implementation

 ===========================================================================
*/
