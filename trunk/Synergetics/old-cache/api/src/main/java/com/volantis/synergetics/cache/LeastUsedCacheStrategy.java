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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 07-May-03    Allan           VBM:2003050605 - Moved from MCS to Synergetics.
 * 20-May-03    Adrian          VBM:2003051901 - Refactored from 
 *                              CacheComparator and now implements 
 *                              CacheStrategy 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

/**
 * A CacheStrategy for CacheObjects.  Caches that set a limit on the maximum
 * number of elements allowed in that cache need a strategy for removing
 * objects when the cache reaches this limit. One approach to implement this
 * strategy is to maintain a sorted cache where the sort key is defined by a
 * Comparator. LeastUsedCacheStrategy provides this facility. In other words a
 * LeastUsedCacheStrategy describes how to sort two CacheObjects by hit count.
 *
 * LeastUsedCacheStrategy assumes that objects it is given to sort are
 * CacheObjects that are held inside SoftReferences.  As a result... Note: this
 * Comparator imposes orderings that are inconsistent with equals.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class LeastUsedCacheStrategy implements CacheStrategy {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003. ";

    // Javadoc inherited from Comparator interface.
    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof Comparable) || !(o2 instanceof Comparable)) {
            return 0;
        }

        // Get the ReadThroughFutureResult
        ReadThroughFutureResult co1 = (ReadThroughFutureResult) o1;
        ReadThroughFutureResult co2 = (ReadThroughFutureResult) o2;

        if (co1 == null || co2 == null) {
            // One or both of the CacheObjects were null so return 0.
            return 0;
        }

        // LEAST_USED strategy is default that does a comparison on
        // the number of hits the PolicyCacheObjects have received. The
        // higher the hitCount the further away from the top of the 
        // cache the ReadThroughFutureResult will go.
        return (int) (co1.getHitCount() - co2.getHitCount());
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
