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
package com.volantis.xml.pipeline.sax.url;

import com.volantis.cache.Cache;
import com.volantis.cache.CacheBuilder;
import com.volantis.cache.CacheFactory;
import com.volantis.shared.net.url.URLContentValidationChecker;
import com.volantis.shared.system.SystemClock;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPCacheConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;

/**
 * Configuration to store the all pipeline unique URL content cache.
 */
public class URLContentCacheConfiguration implements Configuration {

    /**
     * The cache to be used if caching is enabled, null otherwise.
     */
    private Cache cache;

    /**
     * The pipeline confid that stores the other configurations that are needed
     * to lazily create the cache.
     */
    private final XMLPipelineConfiguration pipelineConfig;

    /**
     * True iff an attempt was made to create the cache.
     */
    private boolean cacheInitialized;

    /**
     * Returns the cache object built using the setting in this configuration.
     *
     * <p>Returns null if caching is disabled.</p>
     *
     * <p>The cache object is lazily created and once it is created the cache
     * related parameters (cachingEnabled, maxCacheEntries) cannot be modified.
     * Trying to set these parameters results IllegalStateException.</p>
     */
    public URLContentCacheConfiguration(
            final XMLPipelineConfiguration pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
        cacheInitialized = false;
    }

    /**
     * Returns the cache to be used.
     *
     * <p>May return null if caching is turned off.</p>
     *
     * @return the cache or null
     */
    public Cache getCache() {
        if (!cacheInitialized) {
            createCache();
        }
        return cache;
    }

    /**
     * Tries to create the cache 
     */
    private void createCache() {
        final ConnectionConfiguration connectionConfig =
            (ConnectionConfiguration) pipelineConfig.retrieveConfiguration(
                ConnectionConfiguration.class);
        final boolean enabled;
        final int maxEntries;
        if (connectionConfig != null) {
            enabled = connectionConfig.isCachingEnabled();
            maxEntries = connectionConfig.getMaxCacheEntries();
        } else {
            final WebDriverConfiguration webDriverConfig =
                (WebDriverConfiguration) pipelineConfig.retrieveConfiguration(
                    WebDriverConfiguration.class);
            if (webDriverConfig != null) {
                final HTTPCacheConfiguration httpCacheConfig =
                    webDriverConfig.getHTTPCacheConfiguration();
                if (httpCacheConfig != null) {
                    enabled = true;
                    maxEntries = httpCacheConfig.getMaxEntries();
                } else {
                    enabled = false;
                    maxEntries = 0;
                }
            } else {
                enabled = false;
                maxEntries = 0;
            }
        }
        if (enabled) {
            final CacheBuilder cacheBuilder =
                CacheFactory.getDefaultInstance().createCacheBuilder();
            cacheBuilder.setMaxCount(maxEntries);
            final SystemClock clock = SystemClock.getDefaultInstance();
            cacheBuilder.setClock(clock);
            cacheBuilder.setExpirationChecker(
                new URLContentValidationChecker());
            cache = cacheBuilder.buildCache();
        } else {
            cache = null;
        }
        cacheInitialized = true;
    }
}
