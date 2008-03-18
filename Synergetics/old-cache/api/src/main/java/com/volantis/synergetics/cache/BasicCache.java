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
 * 07-Aug-01     Doug            VBM:2001072503 - Storing SoftReferences to
 *                              objects in the cache is now optional.The
 *                              constructor takes a boolean
 *                              - useReferenceCaching. If true the map is
 *                              wrapped in a SoftMap which manages the storing
 *                              of objects as SoftReferences, if false objects
 *                              are stored as normal references.
 * 02-Jan-02    Paul            VBM:2002010201 - Fixed up header.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 07-May-03    Allan           VBM:2003050605 - Moved from MCS to Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

/**
 * BasicCache is a cache that has no maximum number of elements and no timeout
 * refresh capability.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class BasicCache extends GenericCache {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001. ";

    /**
     * Default constructor
     */
    public BasicCache() {
        this(null);
    }

    /**
     * Constructor that allows the result factory to be specified.
     *
     * @param factory the FutureResultFactory to use.
     */
    public BasicCache(FutureResultFactory factory) {
        super(factory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-05	379/6	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/4	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 02-Feb-05	379/2	matthew	VBM:2005012601 Change the caching implemenation to be (hopefully) more performant

 07-Aug-03	42/1	allan	VBM:2003080502 Add timeToLive, do some refactoring, make Clock more accurate

 ===========================================================================
*/
