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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import java.util.ArrayList;

/**
 * This class is used as a key into a cache using multiple elements.
 */
public class CacheKey {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * A list of objects which together make up our composite cache key.
     */
    protected ArrayList keys = new ArrayList();

    /**
     * Add a new item to the CacheKey
     * @param key The Object to add as part of the composite key.
     */
    public void addKey(Object key) {
        keys.add(key);
    }

    // Javadoc inherited from superclass
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof CacheKey) {
            CacheKey cacheKey = (CacheKey)obj;

            result = keys.equals(cacheKey.keys);
        }
        return result;
    }

    // Javadoc inherited from superclass
    public int hashCode() {
        return 31 * keys.hashCode();
    }

    // javadoc inherited
    public String toString() {
        return keys.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-05	8817/1	philws	VBM:2005060908 Port cache operation process fix from 3.3.1

 16-Jun-05	8815/1	philws	VBM:2005060908 Prevent flow control or other following pipeline process side-effects from disrupting the cache process

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 09-Jun-03	49/3	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/1	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
