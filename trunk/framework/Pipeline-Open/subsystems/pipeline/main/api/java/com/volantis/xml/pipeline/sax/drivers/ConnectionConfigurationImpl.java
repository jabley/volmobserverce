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
package com.volantis.xml.pipeline.sax.drivers;

import com.volantis.shared.time.Period;

/**
 * An implementation of the {@link ConnectionConfiguration} interface.
 */
public class ConnectionConfigurationImpl implements ConnectionConfiguration {
    /**
     * The timeout value held in the configuration.
     */
    private int timeout = 0;

    /**
     * The cache enability held in the configuration.
     */
    private boolean cachingEnabled = false;

    /**
     * The maximum cache entries value held in the configuration.
     */
    private int maxCacheEntries;

    // javadoc inherited
    public void setTimeout(int timeout) {
        this.timeout = (timeout < 1 ? 0 : timeout);
    }

    // javadoc inherited
    public int getTimeout() {
        return timeout;
    }

    /**
     * Returns the (default) timeout for connection handling as a
     * {@link Period}.
     *
     * @return the (default) timeout
     */
    public Period getTimeoutAsPeriod() {
        return Period.treatNonPositiveAsIndefinitely(timeout * 1000);
    }

    // javadoc inherited
    public void setCachingEnabled(final boolean enabled) {
        this.cachingEnabled = enabled;
    }

    // javadoc inherited
    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    // javadoc inherited
    public void setMaxCacheEntries(final int maxEntries) {
        this.maxCacheEntries = maxEntries;
    }

    // javadoc inherited
    public int getMaxCacheEntries() {
        return maxCacheEntries;
    }
}
