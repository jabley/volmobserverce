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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.url;

import com.volantis.cache.Cache;
import com.volantis.shared.time.Period;

/**
 * Configuration object to store parameters needed to create a content manager.
 */
public class URLContentManagerConfiguration {

    /**
     * Default timeout to be used for requests.
     */
    private Period defaultTimeout;

    /**
     * The cache to be used or null if caching is disabled.
     */
    private Cache cache;

    /**
     * Returns the default timeout to be used for requests.
     *
     * @return the default timeout
     */
    public Period getDefaultTimeout() {
        return defaultTimeout;
    }

    /**
     * Sets the default timeout.
     *
     * @param defaultTimeout the new default timeout
     */
    public void setDefaultTimeout(final Period defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    /**
     * Returns the cache to be used. May return null indicating that caching is
     * disabled.
     *
     * @return the cache or null
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * Sets the cache to be used.
     *
     * @param cache the cache
     */
    public void setCache(final Cache cache) {
        this.cache = cache;
    }
}
