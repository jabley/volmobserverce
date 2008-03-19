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
package com.volantis.mcs.runtime.configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Provide a bean implementation for the cache operation configuration.
 */
public class CacheOperationConfiguration {

    /**
     * The expiry mode.
     */
    private boolean fixedExpiryMode = true;

    /**
     * Stores the configuration for the cache xml process
     */
    private List cacheConfigurations = new ArrayList();

    /**
     * Returns true if the expiry mode is set to fixed-age.
     */
    public boolean isFixedExpiryMode() {
        return fixedExpiryMode;
    }

    /**
     * Sets the expiry mode.
     *
     * @param expiryMode true to set the expiry mode to fixed-age, false to set
     * the mode to auto
     */
    public void setExpiryMode(final String expiryMode) {
        fixedExpiryMode = !"auto".equals(expiryMode);
    }


    /**
     * Allows iteration of the  CacheProcessConfiguration objects.
     * @return an Iterator
     */
    public Iterator getCacheConfigurations() {
        return cacheConfigurations.iterator();
    }

    /**
     * Adds a cache configuration to the list
     * @param configuration The cache configuration.
     */
    public void addCache(CacheConfiguration configuration) {
        cacheConfigurations.add(configuration);
    }
}
