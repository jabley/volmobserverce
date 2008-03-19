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

package com.volantis.xml.pipeline.sax.cache;

import java.util.List;

/**
 * This class is used to store the properties of a given cache - cache name,
 * cache key, and cache controls.
 */
public class CacheProperties {

    /**
     * The name of the cache.
     */
    private String cacheName;

    /**
     * The cache key.
     */
    private CacheKey cacheKey;

    /**
     * The {@link CacheControl} for the cache.
     */
    private CacheControl cacheControl;

    /**
     * True if the expiry mode is fixed-age, false if the expiry mode is auto.
     */
    private boolean fixedExpiryMode;

    /**
     * Create a new CacheProperties.
     */
    public CacheProperties() {
        cacheName = null;
        cacheKey = null;
        cacheControl = null;
    }

    /**
     * Get the {@link List} of the {@link CacheControl}s
     * @return the {@link List} of the {@link CacheControl}s
     */
    public CacheControl getCacheControl() {
        return cacheControl;
    }

    /**
     * Set the {@link List} of the {@link CacheControl}s
     * @param cacheControl the {@link List} of the {@link CacheControl}s
     */
    public void setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
    }

    /**
     * Get the {@link CacheKey}
     * @return the {@link CacheKey}
     */
    public CacheKey getCacheKey() {
        return cacheKey;
    }

    /**
     * Set the {@link CacheKey}
     * @param cacheKey the {@link CacheKey}
     */
    public void setCacheKey(CacheKey cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * Add a key value to the {@link CacheKey}, creating it if necessary.
     * @param key the key to add to the CacheKey.
     */
    public void addCacheKey(String key) {
        if (cacheKey == null) {
            cacheKey = new CacheKey();
        }
        cacheKey.addKey(key);
    }

    /**
     * Get the name of the cache.
     * @return the name of the cache.
     */
    public String getCacheName() {
        return cacheName;
    }

    /**
     * Set the name of the cache.
     * @param cacheName the name of the cache.
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * Sets the expiry mode.
     *
     * @param fixedExpiryMode true to set the expiry mode to fixed-age. false to
     * set the mode to auto
     */
    public void setFixedExpiryMode(final boolean fixedExpiryMode) {
        this.fixedExpiryMode = fixedExpiryMode;
    }

    /**
     * Returns true iff the expiry mode is fixed-age.
     */
    public boolean isFixedExpiryMode() {
        return fixedExpiryMode;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Aug-03	316/1	allan	VBM:2003080501 Redesigned CacheControl and added timeToLive

 09-Jun-03	49/3	adrian	VBM:2003060505 Use CacheProperties.class as the key to instances of that class stored in the PipelineContext for Caching AdapterProcesses

 09-Jun-03	49/1	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
