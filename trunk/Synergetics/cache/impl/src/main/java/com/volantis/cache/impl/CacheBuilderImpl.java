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

package com.volantis.cache.impl;

import com.volantis.cache.Cache;
import com.volantis.cache.CacheBuilder;
import com.volantis.cache.expiration.ExpirationChecker;
import com.volantis.cache.impl.group.GroupBuilderImpl;
import com.volantis.cache.impl.group.InternalGroupBuilder;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.shared.system.SystemClock;

/**
 * Builds the {@link Cache}.
 */
public class CacheBuilderImpl
        implements CacheBuilder {

    /**
     * The builder for the root group.
     */
    private final InternalGroupBuilder rootGroupBuilder;

    /**
     * The object provider.
     */
    private CacheableObjectProvider objectProvider;

    /**
     * The clock to use for checking time.
     */
    private SystemClock clock;

    /**
     * The object responsible for checking whether entries have expired.
     */
    private ExpirationChecker expirationChecker;

    /**
     * Initialise.
     */
    public CacheBuilderImpl() {
        rootGroupBuilder = new GroupBuilderImpl();
    }

    /**
     * Get the builder for the root group.
     *
     * @return The builder for the root group.
     */
    public InternalGroupBuilder getRootGroupBuilder() {
        return rootGroupBuilder;
    }

    // Javadoc inherited.
    public CacheableObjectProvider getObjectProvider() {
        return objectProvider;
    }

    // Javadoc inherited.
    public void setObjectProvider(CacheableObjectProvider objectProvider) {
        this.objectProvider = objectProvider;
    }

    // Javadoc inherited.
    public int getMaxCount() {
        return rootGroupBuilder.getMaxCount();
    }

    // Javadoc inherited.
    public void setMaxCount(int maxCount) {
        rootGroupBuilder.setMaxCount(maxCount);
    }

    // Javadoc inherited.
    public SystemClock getClock() {
        return clock;
    }

    // Javadoc inherited.
    public void setClock(SystemClock clock) {
        this.clock = clock;
    }

    // Javadoc inherited.
    public ExpirationChecker getExpirationChecker() {
        return expirationChecker;
    }

    // Javadoc inherited.
    public void setExpirationChecker(ExpirationChecker expirationChecker) {
        this.expirationChecker = expirationChecker;
    }

    // Javadoc inherited.
    public Cache buildCache() {
        return new CacheImpl(this);
    }
}
