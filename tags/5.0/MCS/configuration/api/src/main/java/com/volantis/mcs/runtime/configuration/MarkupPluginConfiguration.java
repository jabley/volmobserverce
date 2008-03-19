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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a bean implementation for the markup-plugin configuration.
 */
public class MarkupPluginConfiguration
        extends IntegrationPluginConfiguration {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The scope of the markup plugin.
     */ 
    private String scope;

    /**
     * Set the scope of the markup plugin.
     * @param scope the scope of the markup plugin.
     */ 
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Get the scope of the markup plugin.
     * @return the scope of the markup plugin.
     */ 
    public String getScope() {
        return scope;
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 ===========================================================================
*/
