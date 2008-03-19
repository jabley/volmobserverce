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

import com.volantis.xml.pipeline.sax.config.Configuration;

/**
 * Encapsulates the configuration information for the various drivers (and
 * other processes) that rely on remote (HTTP) connections to perform their
 * processing.
 *
 * <p><strong>This interface is a facade provided for use by user code and as
 * such must not be implemented by user code.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface ConnectionConfiguration extends Configuration {
    /**
     * Permits the (default) timeout for connection handling to be set or
     * reset. A value of 0 or a negative value indicates no timeout.
     *
     * @param timeout the new timeout value, measured in seconds
     */
    void setTimeout(int timeout);

    /**
     * Returns the (default) timeout for connection handling. A value of 0 or
     * a negative value indicates no timeout.
     *
     * @return the (default) timeout, measured in seconds
     */
    int getTimeout();

    /**
     * Enables/disables caching of responses.
     *
     * @param enabled true to enable caching
     */
    void setCachingEnabled(boolean enabled);

    /**
     * Returns true, iff caching is turned on.
     *
     * @return true if caching is enabled
     */
    boolean isCachingEnabled();

    /**
     * Sets the maximum number of cache entries.
     *
     * @param maxEntries the new maximum number of cache entries.
     */
    void setMaxCacheEntries(int maxEntries);

    /**
     * Returns the maximum number of cache entries.
     *
     * @return the maximum number of cache entries
     */
    int getMaxCacheEntries();
}
