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
 * 20-May-03    Adrian          VBM:2003051901 - Added this as the to return 
 *                              the correct cache pruning strategy for a given 
 *                              strategy name 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.cache;

/**
 * Factory class for creating CacheStrategy objects based upon a strategy
 * name.
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class CacheStrategyFactory {

    /**
     * Volantis copyright object.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new CacheStrategy appropriate to the strategy name specified.
     * Currently this will default to LeastUsedCacheStrategy for everything
     * except LeastRecentlyUsedStrategy.
     *
     * @param strategyName The name of the strategy we want to return.
     * @return An instance of the CacheStrategy requested by the specified
     *         name
     */
    public static CacheStrategy getCacheStrategy(String strategyName) {
        CacheStrategy result = null;
        if (GenericCacheConfiguration.LEAST_RECENTLY_USED.equals(strategyName)) {
            result = new LeastRecentlyUsedCacheStrategy();
        } else {
            // Default to least used for the moment.
            result = new LeastUsedCacheStrategy();
        }
        return result;
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
