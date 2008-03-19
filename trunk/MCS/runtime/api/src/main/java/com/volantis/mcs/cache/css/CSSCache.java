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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.cache.css;

import com.volantis.mcs.cache.Cache;
import com.volantis.mcs.cache.CacheFactory;


/**
 * Provides the cache used to store the objects used to generate the the page
 * level CSS used in the CSSServlet.
 */
public class CSSCache extends Cache {

    /**
     * Instanciate a new CSSCache.
     *
     * @param timeToLive The time a @link CacheIdentity can be considered as
     * current within the Cache.
     */
    public CSSCache(long timeToLive) {
        super(CacheFactory
                .getDefaultInstance().createDefaultCacheStore(timeToLive),
                CacheFactory
                .getDefaultInstance().createBackgroundCacheManager());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
