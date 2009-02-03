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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Adrian          VBM:2003051901 - Added this to test 
 *                              LeastUsedCacheStrategy. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import junit.framework.TestCase;

/**
 * This class tests LeastUsedCacheStrategy.
 */
public class LeastUsedCacheStrategyTestCase extends TestCase {

    /**
     * Construct a new instance of LeastUsedCacheStrategyTestCase
     */
    public LeastUsedCacheStrategyTestCase(String name) {
        super(name);
    }

    /**
     * Test the method compare(Object o1, Object o2)
     */
    public void testCompare() throws Exception {
        ReadThroughFutureResult less =
            new DirectValueFutureResult(new Object(), new String("less"), -1);
        less.setHitCount(1);

        int hits = 2;
        ReadThroughFutureResult baseA =
            new DirectValueFutureResult(new Object(), new String("baseA"), -1);
        baseA.setHitCount(hits);

        ReadThroughFutureResult baseB =
            new DirectValueFutureResult(new Object(), new String("baseB"), -1);
        baseB.setHitCount(hits);

        ReadThroughFutureResult more =
            new DirectValueFutureResult(new Object(), new String("more"), -1);
        more.setHitCount(3);

        LeastUsedCacheStrategy strategy =
            new LeastUsedCacheStrategy();

        int result = strategy.compare(baseA, new Object());
        assertTrue("Compare to non-comparable should return zero.",
                   result == 0);

        boolean exception = false;
        try {
            result = strategy.compare(baseA, new String("string"));
        } catch (ClassCastException cce) {
            exception = true;
        }
        assertTrue("Expected a ClassCastException from a non-CacheStrategy " +
                   "Comparable class.", exception);

        result = strategy.compare(baseA, null);
        assertTrue("Compare to a null should return zero.", result == 0);

        result = strategy.compare(baseA, less);
        assertTrue("Expected a result > zero", result > 0);

        result = strategy.compare(baseA, baseB);
        assertTrue("Expected a result equal to zero", result == 0);

        result = strategy.compare(baseA, more);
        assertTrue("Expected a result < zero", result < 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	397/1	matthew	VBM:2005020308 Implement CachingPluggableHTTPManager

 09-Feb-05	391/1	matthew	VBM:2005020308 Make cache implementation slightly more flexible

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 ===========================================================================
*/
