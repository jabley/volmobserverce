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
package com.volantis.mcs.servlet;

import java.util.Map;
import java.util.HashMap;

/**
 * Type safe enum that identifies a cache key scope.
 */
public class RenderedPageCacheScope {

    /**
     * The copyright statement.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2005. ";

    /**
     * Used to store allowable scopes
     */
    private static final Map SCOPES = new HashMap();

    /**
     * <code>RenderedPageCacheScop</code> for the NONE scope
     */
    public static final RenderedPageCacheScope NONE =
                new RenderedPageCacheScope("none");

    /**
     * <code>RenderedPageCacheScop</code> for the SAFE scope. This
     * scope indicates that any pipeline markup in the XDIME document should
     * be evaluated before using the XDIME as the cache key
     */
    public static final RenderedPageCacheScope SAFE =
                new RenderedPageCacheScope("safe");

    /**
     * <code>RenderedPageCacheScop</code> for the OPTIMISTIC scope. This
     * scope indicates that any pipeline markup in the XDIME document should
     * NOT be evaluated before using the XDIME as the cache key
     */
    public static final RenderedPageCacheScope OPTIMISTIC =
                new RenderedPageCacheScope("optimistic");

    /**
     * The name of the page cache scope.
     */
    private final String name;

    /**
     * Initializes a new instance with the given parameters
     * @param name the name of the scope
     */
    private RenderedPageCacheScope(String name) {
        this.name = name;
        SCOPES.put(name, this);
    }

    // javadoc inherited
    public String toString() {
        return name;
    }

    /**
     * Returns that <code>RenderedPageCacheScope</code> for the given name.
     * @param name the name
     * @return the specified <code>RenderedPageCacheScope</code> or null if
     * on does not exist for the specified name.
     */
    public static final RenderedPageCacheScope get(String name) {
        return (RenderedPageCacheScope) SCOPES.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7762/4	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 ===========================================================================
*/
