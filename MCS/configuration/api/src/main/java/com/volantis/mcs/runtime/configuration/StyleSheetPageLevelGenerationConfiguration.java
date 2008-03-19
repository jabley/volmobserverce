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
public class StyleSheetPageLevelGenerationConfiguration {



    /**
     * The maximum age of the cache.  This can be >=0 or "unlimited".
     * If it is specified as "unlimited" then this should be translated to -1
     * as necessary when creating the cache.
     */
    private String maxAge;

    /**

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

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
