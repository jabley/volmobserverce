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

import java.util.Hashtable;
import java.util.Map;

/**
 * Provide a bean implementation of the JNDI configuration.
 */
public class JNDIConfiguration implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * A Map of JNDI Initial Contexts.
     */
    protected Map initialContexts = new Hashtable();
    
    /**
     * Add a initialContext to the map. Note that the initialContexts are 
     * stored in a map where the name is the key and the value the 
     * configuration object.
     *
     * @param config the InitialContextConfiguration object storing the name and
     *               value for a parameter.
     */
    public void addInitialContext(InitialContextConfiguration config) {
        initialContexts.put(config.getName(), config);
    }
    
    /**
     * @return The initialContextConfiguration object
     */
    public InitialContextConfiguration getInitialContext(String context) {
        return (InitialContextConfiguration)initialContexts.get(context);
    }

    /**
     * @return The map of initialContextConfiguration objects
     */
    public Map getInitialContexts() {
        return initialContexts;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 13-Jun-03	316/6	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/4	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
