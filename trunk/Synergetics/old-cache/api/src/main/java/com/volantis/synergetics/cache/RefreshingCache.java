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
 * 07-Aug-01    Doug            VBM:2001072503 - Storing SoftReferences to 
 *                              objects in the cache is now optional.The 
 *                              constructor takes a boolean 
 *                              - useReferenceCaching. If true the map is 
 *                              wrapped in a SoftMap which manages the storing
 *                              of objects as SoftReferences, if false objects
 *                              are stored as normal references.
 * 02-Jan-02    Paul            VBM:2002010201 - Fixed up header.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 07-May-02    Allan           VBM:2003050605 - Moved from MCS to Synergetics.                               
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;


/**
 * A RefreshingCache is a GenericCache that removes objects from the cache that
 * have timed out. An object times out when the length of time it has existed
 * in the cache is greater than the <code>timeout</code> property of the cache.
 * There is no fixed limit to the number of elements allowed in a
 * RefreshingCache.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class RefreshingCache extends GenericCache {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Constructor that creates a cache using a default FutureResultFactory.
     *
     * @param timeout the timeout to use for this cache.
     */
    public RefreshingCache(int timeout) {
        super();
        setTimeout(timeout);
    }

    /**
     * Create a new Soft RefreshingCache.
     *
     * @param factory the factory used to create FutureResultObjects this cache
     *                will use.
     * @param timeout the value for the timeout property of this cache
     */
    public RefreshingCache(FutureResultFactory factory, int timeout) {
        super(factory);
        setTimeout(timeout);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-05	379/4	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/2	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
