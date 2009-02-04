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
 * $Header: /src/voyager/com/volantis/mcs/utilities/LocalRSBPool.java,v 1.2 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-02    Allan           VBM:2002022007 - Created. An unsynchronized
 *                              pool of ReusableStringBuffers.
 * 30-Dec-02    Byron           VBM:2002071015 - Moved from .mcs.pool package.
 *                              Modified usages of PooleableObject to
 *                              ReusableStringBuffer. No longer extends from
 *                              SingleThreadedPool. Updated comments and style.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

import java.util.ArrayList;

/**
 * Provide a pool of reusable ReusableStringBuffers. This pool does not do
 * block allocation. This pool is designed to persist as long as the object
 * declaring it persists.
 *
 * NOTE: This class is unsynchronized, designed for local thread use only.
 *
 * @todo better I don't see the benefit of having this class. According to the
 * Object Pool pattern, one uses a pool if the object is expensive to create,
 * or only a limited number of a kind need to be created.
 * In this case, neither of these conditions are true. Also, this pool is only
 * used in the MarinerPageContext where it will be created for each page
 * request. It will be released after initialisePage method is finished.
 * Therefore, the pool's active lifespan is the duration of the call to
 * initialisePage for each page request. Ideally the pool should be a singleton
 * made thread safe and ADD and REMOVE objects from the pool. More importantly,
 * the performance gain of using a pool should be re-evaluated against the
 * on demand creation of RSB's. Once this is done the LocalRSBPool may or may
 * not be removed.
 *
 * Note: The current implementation is simply a temporary cache of RSB objects
 * that persists as long as the object declaring it persists (it is not really
 * a pool).
 */
public class LocalRSBPool {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(LocalRSBPool.class);

    /**
     * The pool of ReusableStringBuffers
     */
    private ArrayList pool;

    /**
     * The index of the next object to provide in the pool.
     */
    private int next;

    /**
     * The initial size.
     */
    private final int initialSize;

    /**
     * Create a new ReusableStringBuffer for the pool.
     */
    private ReusableStringBuffer createObject() {
        return new ReusableStringBuffer(40);
    }

    /**
     * Construct a new LocalRSBPool using the provided initial size.
     */
    public LocalRSBPool(int initialSize) {
        this.initialSize = initialSize;
        pool = new ArrayList(initialSize);
        for (int i = 0; i < initialSize; i++) {
            pool.add(createObject());
        }
        next = 0;
    }

    /**
     * Allocate an object in the pool.
     *
     * @return      The object which was allocated.
     */
    public ReusableStringBuffer allocateObject() {
        if (next < pool.size()) {
            return (ReusableStringBuffer) pool.get(next++);
        } else {
            // expand the pool by the initialSize
            for (int i = 0; i < initialSize; i++) {
                pool.add(createObject());
            }
            return (ReusableStringBuffer) pool.get(next++);
        }
    }

    /**
     * Release a particular item from the pool.
     *
     * @param rsb  the <code>ReusableStringBuffer</code> to release from the
     *             pool.
     */
    public void releaseObject(ReusableStringBuffer rsb) {

        if (rsb == null) {
            return;
        }

        if(logger.isDebugEnabled()){
            logger.debug(this + ": Released object " + rsb);
        }

        rsb.setLength(0);
    }

    /**
     * Release all items in the pool.
     */
    public void release() {
        for (int i = 0; i < next; i++) {
            ReusableStringBuffer rsb = (ReusableStringBuffer) pool.get(i);
            rsb.setLength(0);
        }
        next = 0;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
