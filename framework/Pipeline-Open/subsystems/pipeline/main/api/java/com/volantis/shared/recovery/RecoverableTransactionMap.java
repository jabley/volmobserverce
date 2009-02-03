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

package com.volantis.shared.recovery;

import com.volantis.xml.namespace.ExpandedName;

import java.util.Map;
import java.util.HashMap;

/**
 * The <code>RecoverableTransactonMap</code> class represents a mapping of
 * key-value pair Objects.  It wraps a java.util.HashMap and delegates the usual
 * put, get, and remove methods.
 *
 * This Map implements the {@link RecoverableTransaction} interface and as
 * such provides the means to:
 * <ul>
 * <li> Capture the Map state at a specified instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * Clients of RecoverableTransactionMap may define the start of a transaction
 * by calling the {@link #startTransaction()} method.  The transaction may be
 * completed successfully by a call to the {@link #commitTransaction()} method.
 * The transaction may be reverted under error conditions by a call to the
 * {@link #rollbackTransaction()} method.
 */
public class RecoverableTransactionMap extends AbstractRecoverableTransaction {

    /**
     * The Volantis copyright statement.
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The java.util.Map to which this class will delegate.
     */
    protected Map map;

    /**
     * Construct a new instance of RecoverableTransactionMap.
     */
    public RecoverableTransactionMap() {
        map = new HashMap();
    }

    /**
     * Associates the specified value with the specified key in this Map.  If
     * the map previously contained a mapping for this key, the old value is
     * replaced.
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    /**
     * Returns the value to which this Map maps the specified key.  Returns
     * <tt>null</tt> if the map contains no mapping for this key.
     * @param key key whose associated value is to be returned.
     * @return the value to which this map maps the specified key, or
     *	       <tt>null</tt> if the map contains no mapping for this key.
     */
    public Object get(Object key) {
        return map.get(key);
    }

    /**
     * Removes the mapping for this key from this map if present.
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     */
    public Object remove(Object key) {
        return map.remove(key);
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key key whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key.
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     * @return the number of key-value mappings in this map.
     */
    public int size() {
        return map.size();
    }

    // Javadoc inherited from Cloneable interface.
    protected Object clone() throws CloneNotSupportedException {
        RecoverableTransactionMap clone =
                (RecoverableTransactionMap)super.clone();
        clone.map = new HashMap(map);
        return clone;
    }

    // Javadoc inherited from superclass
    protected void startTransactionImpl() {
        // nothing to do.
    }

    // Javadoc inherited from superclass
    protected void commitTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        // nothing to do.
    }

    // Javadoc inherited from superclass
    protected void rollbackTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        // restore the Map back to the state at the time of the last call
        // to the startTransaction method.
        RecoverableTransactionMap popped =
                (RecoverableTransactionMap)poppedState;
        map = popped.map;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/2	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
