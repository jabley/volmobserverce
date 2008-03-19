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

package com.volantis.xml.pipeline.sax.cache;

/**
 * This class wraps entries into the cache.  It also contains state
 * information as the entry can be marked as pending.  This means that
 * the entry is not yet complete.
 */
public class CacheEntry {

    /**
     * The Object we wish to store in the cache.
     */
    private Object cacheObject;

    /**
     * This flag determines whether the Object {@link #cacheObject} is in
     * a completed state.  true if the Object is complete, otherwise false.
     */
    private boolean pending = false;

    /**
     * Construct a new CacheEntry with the specified Object.  This
     * CacheEntry will be in a non-pending state.
     * @param cacheObject - The Object that we wish to store in the cache.
     */
    public CacheEntry(Object cacheObject) {
        this.cacheObject = cacheObject;
    }

    /**
     * Construct a new CacheEntry with the specified Object in the
     * specified pending state.
     * @param cacheObject - The Object that we wish to store in the cache.
     * @param pending - true if the Object is the cache is not in a
     * complete state, otherwise false.
     */
    public CacheEntry(Object cacheObject, boolean pending) {
        this.cacheObject = cacheObject;
        this.pending = pending;
    }

    /**
     * Get the state of the stored Object.
     * @return true if the stored Object is in a pending (incomplete)
     * state, otherwise false.
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * Set the state of the stored Object.
     * @param pending - true to set the state to pending, otherwise false
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    /**
     * Get the Object being cached.
     * @return The Object being cached.
     */
    public Object getCacheObject() {
        return cacheObject;
    }

    /**
     * Set the object being cached.
     * @param object The object being cached.
     */
    public void setCacheObject(Object object) {
        this.cacheObject = object;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 09-Jun-03	49/1	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 ===========================================================================
*/
