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
import java.util.Set;
import java.util.HashSet;

/**
 * The <code>RecoverableTransactonSet</code> class represents a mapping of
 * key-value pair Objects.  It wraps a java.util.HashSet and delegates the usual
 * put, get, and remove methods.
 *
 * This Set implements the {@link RecoverableTransaction} interface and as
 * such provides the means to:
 * <ul>
 * <li> Capture the Map state at a specified instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * Clients of RecoverableTransactionSet may define the start of a transaction
 * by calling the {@link #startTransaction()} method.  The transaction may be
 * completed successfully by a call to the {@link #commitTransaction()} method.
 * The transaction may be reverted under error conditions by a call to the
 * {@link #rollbackTransaction()} method.
 */
public class RecoverableTransactionSet extends AbstractRecoverableTransaction {

    /**
     * The Volantis copyright statement.
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The java.util.Set to which this class will delegate.
     */
    protected Set set;

    /**
     * Construct a new instance of RecoverableTransactionMap.
     */
    public RecoverableTransactionSet() {
        set = new HashSet();
    }

    /**
     * Adds the specified element to this set if it is not already present
     * @param object element to be added to this set.
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
     */
    public boolean add(Object object) {
        return set.add(object);
    }

    /**
     * Removes the specified element from this set if it is present
     * @param object the object to be removed from this set, if present.
     * @return true if the set contained the specified element.
     */
    public boolean remove(Object object) {
        return set.remove(object);
    }

    /**
     * Get the internal java.util.Set
     * <p>
     * Clients <b>should not</b> modify the content of this Set.  The behaviour
     * resultant from such a modification is undefined.
     *
     * @return the internal java.util.Set
     */
    public Set getSet() {
        return set;
    }

    // Javadoc inherited from Cloneable interface.
    protected Object clone() throws CloneNotSupportedException {
        RecoverableTransactionSet clone =
                (RecoverableTransactionSet)super.clone();
        clone.set = new HashSet(set);
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
        RecoverableTransactionSet popped =
                (RecoverableTransactionSet)poppedState;
        set = popped.set;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
