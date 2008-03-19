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

package com.volantis.mcs.runtime.configuration;

/**
 * Provide a bean implementation for the cache configuration.
 */
public class CacheConfiguration {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name of the cache.
     */
    private String name;

    /**
     * The strategy of the cache. Such as 'least-recently-used'.
     */
    private String strategy;

    /**
     * The string used in {@link #setMaxAge(String)} and
     * {@link #setMaxEntries(String)}  which specifies that there is no limit
     * on the age / number of entries respectively.
     */
    public static final String UNLIMITED = "unlimited";

    /**
     * The maximum number of entries permitted in the cache.  This can be > 0
     * or {@link #UNLIMITED}.  If it is specified as "unlimited" then this should be
     * translated to -1 as necessary when creating the cache.
     */
    private String maxEntries;

    /**
     * The maximum age of the cache.  This can be >=0 or "unlimited".
     * If it is specified as "unlimited" then this should be translated to -1
     * as necessary when creating the cache.
     */
    private String maxAge;

    /**
     * Return the maximum number of entries permitted in the cache.  This
     * should be > 0 or a string representation of unlimited.
     *
     * @return      the maximum number of entries permitted in the cache.
     */
    public String getMaxEntries() {
        return maxEntries;
    }

    /**
     * Set the maximum number of entries permitted in the cache.   This
     * should be > 0 or a string representation of unlimited.
     *
     * @param maxEntries the maximum number of entries permitted in the cache.
     */
    public void setMaxEntries(String maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Get the name of the cache.
     *
     * @return      the name of the cache.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the cache.
     * @param name the name of the cache.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the strategy.
     *
     * @return      the strategy e.g. 'least-recently-used'.
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Set the strategy (e.g. 'least-recently-used').
     *
     * @param strategy the strategy.
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the maximum age of the cache.   This should be >= 0 or a string
     * representation of unlimited.
     *
     * @return The maximum age of the cache in seconds.
     */
    public String getMaxAge() {
        return maxAge;
    }

    /**
     * Set the maximum age of the cache.   This should be >= 0 or a string
     * representation of unlimited.
     *
     * @param maxAge The maxium age of the cache in seconds.
     */
    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 13-Jun-03	316/3	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
