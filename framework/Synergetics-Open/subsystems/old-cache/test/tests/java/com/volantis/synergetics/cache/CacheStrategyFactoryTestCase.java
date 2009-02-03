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
 *                              CacheStrategyFactory. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

import junit.framework.TestCase;

/**
 * This class tests CacheStrategyFactory.
 */
public class CacheStrategyFactoryTestCase extends TestCase {

    /**
     * Construct a new instance of CacheStrategyFactoryTestCase
     */
    public CacheStrategyFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Test the method getCacheStrategy.
     */
    public void testGetCacheStrategy() throws Exception {

        // Check we are correctly returned a LeastRecentlyUsed strategy
        CacheStrategy leastRecent = CacheStrategyFactory.
            getCacheStrategy(GenericCacheConfiguration.LEAST_RECENTLY_USED);

        assertTrue("Expected LeastRecentlyUsedCacheStrategy.",
                   leastRecent.getClass() ==
                   LeastRecentlyUsedCacheStrategy.class);
        
        // Check we are correctly returned a LeastUsed strategy
        CacheStrategy leastUsed = CacheStrategyFactory.
            getCacheStrategy(GenericCacheConfiguration.LEAST_USED);

        assertTrue("Expected LeastUsedCacheStrategy.",
                   leastUsed.getClass() == LeastUsedCacheStrategy.class);
        
        // Check that by defaul we are returned a LeastUsed strategy.
        CacheStrategy defaultStrategy = CacheStrategyFactory.
            getCacheStrategy("random string");

        assertTrue("Expected LeastUsedCacheStrategy.",
                   defaultStrategy.getClass() == LeastUsedCacheStrategy.class);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jun-04	259/1	claire	VBM:2004060803 Refactored location of cache config related constants

 ===========================================================================
*/
