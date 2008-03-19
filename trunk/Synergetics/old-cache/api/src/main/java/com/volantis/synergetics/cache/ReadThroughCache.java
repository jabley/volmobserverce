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
 * (c) Volantis Systems Ltd 2008. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.cache;


import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;


/**
 *
 * A simple high concurreny cache that "reads" through to perform item level
 * locking. Insertions and retrievals should not need to lock the entire table.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class ReadThroughCache implements Serializable {

    /**
     * The concurrent hash map to deleate to
     */
    private final ConcurrentHashMap delegate;

    /**
     * The default initial number of table slots for this table (32). Used when
     * not otherwise specified in constructor.
     */
    public static int DEFAULT_CONCURRENCY = 32;

    /**
     * The default load factor for this table (0.75) Used when not otherwise
     * specified in constructor.
     */

    public static final float DEFAULT_LOAD_FACTOR = 0.75f;


    /**
     * This vaiable contains the factory that will be used to create the
     * ReadThroughFutureResult objects that will be inserted into the cache.
     */
    private FutureResultFactory futureResultFactory;

    /**
     * Constructs a new, empty map with the specified initial capacity and the
     * specified load factor.
     *
     * @param initialCapacity the initial capacity. The actual initial capacity
     *                        is rounded to the nearest power of two.
     * @param loadFactor      the load factor threshold, used to control
     *                        resizing. This value is used in an approximate
     *                        way: When at least a quarter of the segments of
     *                        the table reach per-segment threshold, or one of
     *                        the segments itself exceeds overall threshold,
     *                        the table is doubled. This will on average cause
     *                        resizing when the table-wide load factor is
     *                        slightly less than the threshold. If you'd like
     *                        to avoid resizing, you can set this to a
     *                        ridiculously large value.
     * @throws IllegalArgumentException if the load factor is nonpositive.
     */
    public ReadThroughCache(FutureResultFactory factory, int initialCapacity,
                            float loadFactor) {
        if (!(loadFactor > 0)) {
            throw new IllegalArgumentException("Illegal Load factor: " +
                                               loadFactor);
        }
        this.delegate = new ConcurrentHashMap(initialCapacity, loadFactor, DEFAULT_CONCURRENCY);
        if (factory == null) {
            throw new IllegalArgumentException(
                "FutureResultDataAccessorFactory must not be null");
        }
        this.futureResultFactory = factory;
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and
     * default load factor.
     *
     * @param initialCapacity the initial capacity of the ConcurrentHashMap.
     * @throws IllegalArgumentException if the initial maximum number of
     *                                  elements is less than zero.
     */
    public ReadThroughCache(FutureResultFactory factory,
                            int initialCapacity) {
        this(factory, initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new, empty map with a default initial capacity and default
     * load factor.
     */
    public ReadThroughCache(FutureResultFactory factory) {
        this(factory, DEFAULT_CONCURRENCY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * @return the FutureResultFactory that this cache is using to populate
     *         itself.
     */
    protected FutureResultFactory getFutureResultFactory() {
        return futureResultFactory;
    }

    /**
     * Returns the number of key-value mappings in this map. Calls to this also
     * update the approximate size variable of this cache.
     *
     * @return the number of key-value mappings in this map.
     */
    public int size() {
       return delegate.size();
    }

    /**
     * Return the approximate size of the cache. This is inaccurate but
     * significantly less expensive then calling size(). A side effect of this
     * inaccuracy is that it can return negative sizes.
     *
     * @return the approximate number of entries in the cache.
     */
    public int approximateSize() {
        return size();
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings. This
     * is relatively expensive so avoid calling.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
       return delegate.isEmpty();
    }

    /**
     * Returns the value to which the specified key is mapped in this table.
     *
     * @param key        a key in the table.
     * @param timeToLive The time you wish the object to live. This is only
     *                   used when the call to get causes a new object to be
     *                   put into the map. It cannot be used to reset the
     *                   timeToLive of a pre-existing object.
     * @return the value to which the key is mapped in this table;
     *         <code>null</code> if the key is not mapped to any value in this
     *         table.
     *
     * @throws NullPointerException if the key is <code>null</code>.
     * @see #put(Object, ReadThroughFutureResult)
     */
    public ReadThroughFutureResult get(Object key,
                                       int timeToLive) {

        ReadThroughFutureResult add =
            futureResultFactory.createFutureResult(key, timeToLive);
        ReadThroughFutureResult old = (ReadThroughFutureResult)
            delegate.putIfAbsent(key, add);
        if (null == old) {
            return add;
        } else {
            return old;
        }
    }

    /**
     * Put a ReadThroughFutureResult into the Cache.
     *
     * @param key
     * @param value
     * @return
     *
     * @deprecated This only exists for backward compatibility. Users should
     *             use the get() method and set an appropriate
     *             FutureResultFactory which will populate the cache
     *             automagically.
     */
    public ReadThroughFutureResult put(Object key,
                                       ReadThroughFutureResult value) {
        return internalPut(key, value);
    }

    /**
     * Maps the specified <code>key</code> to the specified <code>value</code>
     * in this table. Neither the key nor the value can be <code>null</code>.
     * (Note that this policy is the same as for java.util.Hashtable, but
     * unlike java.util.HashMap, which does accept nulls as valid keys and
     * values.)<p>
     *
     * The value can be retrieved by calling the <code>get</code> method with a
     * key that is equal to the original key.
     *
     * @param key   the table key.
     * @param value the value.
     * @return the previous value of the specified key in this table, or
     *         <code>null</code> if it did not have one.
     *
     * @throws NullPointerException if the key or value is <code>null</code>.
     * @see Object#equals(Object)
     * @see #get(Object, int)
     */
    private ReadThroughFutureResult internalPut(Object key,
                                                ReadThroughFutureResult value) {
        if (value == null) {
            throw new NullPointerException();
        }

        return (ReadThroughFutureResult) delegate.putIfAbsent(key, value);

    }

    /**
     * Removes the key (and its corresponding value) from this table. This
     * method does nothing if the key is not in the table.
     *
     * @param key the key that needs to be removed.
     * @return the value to which the key had been mapped in this table, or
     *         <code>null</code> if the key did not have a mapping.
     *
     * @throws NullPointerException if the key is <code>null</code>.
     */
    public ReadThroughFutureResult remove(Object key) {
        return remove(key, null);
    }


    /**
     * Removes the (key, value) pair from this table. This method does nothing
     * if the key is not in the table, or if the key is associated with a
     * different value. This method is needed by EntrySet.
     *
     * @param key   the key that needs to be removed.
     * @param value the associated value. If the value is null, it means "any
     *              value".
     * @return the value to which the key had been mapped in this table, or
     *         <code>null</code> if the key did not have a mapping.
     *
     * @throws NullPointerException if the key is <code>null</code>.
     */

    protected ReadThroughFutureResult remove(Object key,
                                             ReadThroughFutureResult value) {

        if (key == null) {
            throw new NullPointerException("key is null");
        }

        ReadThroughFutureResult result = null;
        if (value == null) {
            result = (ReadThroughFutureResult) delegate.remove(key);
        } else {
            if (delegate.remove(key, value)) {
                result = value;
            }
        }

        return result;
    }

    /**
     * Removes all mappings from this map.
     */

    public void clear() {
        delegate.clear();
    }

    public Iterator keys() {
        return delegate.keySet().iterator();
    }


    /**
     * Save the state of the <tt>ConcurrentHashMap</tt> instance to a stream
     * (i.e., serialize it).
     *
     * @serialData An estimate of the table size, followed by the key (Object)
     * and value (Object) for each key-value mapping, followed by a null pair.
     * The key-value mappings are emitted in no particular order.
     */

    private void writeObject(java.io.ObjectOutputStream s)
        throws IOException {
        // Write out the loadfactor, and any hidden stuff
        s.defaultWriteObject();

    }

    /**
     * Reconstitute the <tt>ConcurrentHashMap</tt> instance from a stream
     * (i.e., deserialize it).
     */
    private void readObject(java.io.ObjectInputStream s)
        throws IOException, ClassNotFoundException {
        // Read in the threshold, loadfactor, and any hidden stuff
        s.defaultReadObject();
    }


}

