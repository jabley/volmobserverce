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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.url.http;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.net.url.URLContent;
import com.volantis.cache.Cache;
import com.volantis.cache.provider.CacheableObjectProvider;

/**
 * Dependency for cacheable HTTP content. This dependency is capable of
 * revalidate itself. If during revalidation a new content is returned from the
 * origin server, it stores the new content in the actual dependency context
 * against the URL of the request.
 */
public class CacheableDependency implements Dependency {
    /**
     * Cache key for revalidation.
     */
    private final Object cacheKey;

    /**
     * Cache entry state to return freshness information.
     */
    private CachedHttpContentState state;

    /**
     * Lock to synchronise to when accessing state.
     */
    private final Object stateLock;

    /**
     * Clock to retrieve the current time from.
     */
    private final SystemClock clock;

    /**
     * Cache for revalidation
     */
    private final Cache cache;

    /**
     * Cacheable object provider to be used to revalidate the dependency.
     */
    private final CacheableObjectProvider objectProvider;

    /**
     * Creates a new cacheable dependency for a HTTP content entry.
     *
     * @param cacheKey the key in the cache for the dependency's content
     * @param state the cache entry state of the content
     * @param clock the clock used by the cache
     * @param cache the cache that contains the HTTP content
     * @param objectProvider the object provider to be used to reget the content
     */
    public CacheableDependency(final Object cacheKey,
                               final CachedHttpContentState state,
                               final SystemClock clock,
                               final Cache cache,
                               final CacheableObjectProvider objectProvider) {
        this.cacheKey = cacheKey;
        this.state = state;
        stateLock = new Object();
        this.clock = clock;
        this.cache = cache;
        this.objectProvider = objectProvider;
    }

    // javadoc inherited
    public Cacheability getCacheability() {
        synchronized (stateLock) {
            return state.isCacheable() &&
                   !state.isCcNoCache() && !state.isPragmaNoCache()?
                Cacheability.CACHEABLE: Cacheability.UNCACHEABLE;
        }
    }

    // javadoc inherited
    public Period getTimeToLive() {
        synchronized (stateLock) {
            return state.getTimeToLive(clock.getCurrentTime());
        }
    }

    // javadoc inherited
    public Freshness freshness(final DependencyContext context) {
        synchronized (stateLock) {
            return state.isStale(clock.getCurrentTime())?
                Freshness.REVALIDATE: Freshness.FRESH;
        }
    }

    /**
     * Updates the cache entry state for the dependency.
     *
     * <p>This method is to be used when the content was successfully
     * revalidated (304 Not Modified was returned from the origin server) to
     * update the freshness information of the dependency.</p>
     *
     * @param state the new cache entry state
     */
    public void setState(final CachedHttpContentState state) {
        synchronized (stateLock) {
            this.state = state;
        }
    }

    // javadoc inherited
    public Freshness revalidate(final DependencyContext context) {
        final URLContent content =
            (URLContent) cache.retrieve(cacheKey, objectProvider);
        final Freshness freshness;
        if (!this.equals(content.getDependency())) {
            freshness = Freshness.STALE;
            context.setProperty(cacheKey, content);
        } else {
            freshness = freshness(context);
        }
        return freshness;
    }
}
