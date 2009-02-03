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
package com.volantis.synergetics.cache;

/**
 * Test case for ManagedCache.
 */
public class ManagedCacheTestCase extends LimitedCacheTestCase {

    /**
     * Create a ManagedCache with an arbitrary limit, strategy and timeout that
     * uses hard references.
     *
     * @return A ManagedCache.
     */
    protected GenericCache createTestableGenericCache() {
        return new ManagedCache(null, 1000, new LeastUsedCacheStrategy(),
                                1000);
    }

    /**
     * Create a ManagedCache with an arbitrary limit, strategy and timeout that
     * uses soft references.
     *
     * @return A ManagedCache.
     */
    protected GenericCache createTestableSoftGenericCache() {
        return new ManagedCache(null, 1000, new LeastUsedCacheStrategy(),
                                1000);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	379/1	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
