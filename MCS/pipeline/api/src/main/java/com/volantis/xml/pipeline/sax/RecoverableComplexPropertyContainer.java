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

package com.volantis.xml.pipeline.sax;

import com.volantis.shared.recovery.RecoverableTransactionMap;
import com.volantis.shared.recovery.AbstractRecoverableTransaction;
import com.volantis.xml.pipeline.sax.ResourceOwner;
import com.volantis.xml.pipeline.sax.PropertyContainer;

import java.util.Set;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * This specialisation of RecoverableTransactionMap allows us encapsulate the
 * recoverable map behaviour and call {@link ResourceOwner#release} on any
 * ResourceOwner Objects set in the Map or 'unadded' as a result of reverting
 * the state.
 *
 * {@see RecoverableTransactionMap} for more details of recoverable Map
 * behaviour.
 *
 * This Container can be used to store any arbitary property. However,
 * properties that are {@link ResourceOwner} instances are treated specially.
 * If {@link #setProperty} is invoked to overwrite an existing property that
 * is an instance of {@link ResourceOwner} the existing properties
 * {@link ResourceOwner#release} method is invoked. Likewise, if
 * {@link #removeProperty} is invoked and a ResourceOwner instance is
 * removed its release method will be invoked. The {@link #release} method
 * will call release on all ResourceOwner properties in this container.
 */
public class RecoverableComplexPropertyContainer
        extends RecoverableTransactionMap
        implements PropertyContainer, ResourceOwner {

    /**
     * The Volantis copyright statement.
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * This Object is used to denote that a value has been removed from the
     * map within a transaction.  This makes life a lot easier when merging
     * the transaction map back into the parent map.
     */
    private static final Object REMOVE_ON_MERGE = new Object();

    /**
     * Construct a new instance of RecoverableComplexPropertyContainer.
     */
    public RecoverableComplexPropertyContainer() {
    }

    // javadoc inherited
    public void setProperty(Object key, Object value, boolean release) {
        Object toPush = value;
        if (release && toPush instanceof ResourceOwner) {
            toPush = new ReleasableResourceOwner((ResourceOwner)toPush);
        }

        Object previous = map.put(key, toPush);
        if (previous != null &&
                !ResourceOwnerHelper.sameResourceOwner(toPush, previous)) {
            ResourceOwnerHelper.releaseIfReleasableResourceOwner(previous);
        }
    }

    // javadoc inherited from superclass
    public final Object put(Object key, Object value) {
        Object previous = getProperty(key);
        setProperty(key, value, true);
        return previous;
    }

    // javadoc inherited
    public Object getProperty(Object key) {
        Object result = map.get(key);

        if (result == null && !clones.isEmpty()) {
            RecoverableComplexPropertyContainer clone =
                    (RecoverableComplexPropertyContainer)clones.peek();
            result = clone.getProperty(key);
        }

        if (result instanceof ReleasableResourceOwner) {
            result = ((ReleasableResourceOwner)result).getResourceOwner();
        }
        return result;
    }

    // javadoc inherited from superclass
    public final Object get(Object key) {
        return getProperty(key);
    }

    // javadoc inherited
    public Object removeProperty(Object key) {
        Object removed = map.remove(key);
        if (removed == null && inTransaction()) {
            map.put(key, REMOVE_ON_MERGE);
        }
        ResourceOwnerHelper.releaseIfReleasableResourceOwner(removed);
        if (removed instanceof ReleasableResourceOwner) {
            removed = ((ReleasableResourceOwner)removed).getResourceOwner();
        }
        return removed;
    }

    // javadoc inherited from superclass
    public final Object remove(Object key) {
        return removeProperty(key);
    }

    // javadoc inherited
    public void release() {
        releaseMap(map);
        if (inTransaction()) {
            RecoverableComplexPropertyContainer clone =
                    (RecoverableComplexPropertyContainer)clones.peek();
            clone.release();
        }
    }

    /**
     * This helper method removes each the items in the specified Map, calling
     * {@link ResourceOwner#release} on any {@link com.volantis.xml.pipeline.sax.ReleasableResourceOwner}
     * @param map The Map from which the content will be removed and released.
     */
    private void releaseMap(Map map) {
        Set keys = map.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            Object removed = map.get(object);
            ResourceOwnerHelper.releaseIfReleasableResourceOwner(removed);
        }
        map.clear();
    }

    /**
     * This helper merges the current map into the specified map and sets the
     * result as the current map.
     * @param clone The map to merge the current map into.
     */
    private void mergeChanges(Map clone) {
        Map current = map;
        this.map = clone;

        Set keys = current.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            Object property = current.get(key);

            if (property == REMOVE_ON_MERGE) {
                removeProperty(key);
            } else {
                setProperty(key, property, false);
            }
        }
    }

    // Javadoc inherited from superclass
    protected void startTransactionImpl() {
        map.clear();
        super.startTransactionImpl();
    }

    // Javadoc inherited from superclass
    protected void commitTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        Map clone = ((RecoverableComplexPropertyContainer)poppedState).map;
        mergeChanges(clone);
        super.commitTransactionImpl(poppedState);
    }

    // Javadoc inherited from superclass
    protected void rollbackTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        releaseMap(map);
        map = ((RecoverableComplexPropertyContainer)poppedState).map;
        super.rollbackTransactionImpl(poppedState);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 10-Aug-03	264/2	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
