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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.runtime.configuration.xml.digester.Enabled;

/**
 * Container for the page-cache configuration read from the mariner
 * configuration file.
 */
public class RenderedPageCacheConfiguration implements Enabled {
    /**
     * The caching strategy to use.
     */
    private String strategy;

    /**
     * The maximum number of entries in the cache.
     */
    private Integer maxEntries;

    /**
     * The default cache timeout.
     */
    private Integer timeout;

    /**
     * The default cache scope to use
     */
    private String defaultScope;

    /**
     * Get the maximum number of cache entries.
     *
     * @return The maximum number of cache entries
     */
    public Integer getMaxEntries() {
        return maxEntries;
    }

    /**
     * Set the maximum number of cache entries.
     *
     * @param maxEntries The new maximum number of cache entries
     */
    public void setMaxEntries(Integer maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Get the caching strategy.
     *
     * @return The caching strategy
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Set the caching strategy.
     *
     * @param strategy The new caching strategy
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the cache timeout.
     * 
     * @return The cache timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Set the cache timeout.
     *
     * @param timeout The new cache timeout
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    // javadoc unnecessary
    public String getDefaultScope() {
        return defaultScope;
    }

    // javadoc unnecessary
    public void setDefaultScope(String defaultScope) {
        this.defaultScope = defaultScope;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 11-Feb-05	6786/1	adrianj	VBM:2005012506 Added rendered page cache

 ===========================================================================
*/
