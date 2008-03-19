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

import java.util.Map;
import java.util.Hashtable;

/**
 * Provide a bean implementation for the InitialContext (JNDI) configuration.
 */
public class InitialContextConfiguration {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The initial context name.
     */
    private String name;

    /**
     * Store the parameters as a Hashtable which may be then used directly
     * to create the InitialContext. A HashMap would be preferable but would
     * require a conversion to a Hashtable every time one wants to create
     * an InitialContext.
     */
    private Map parameters = new Hashtable();

    /**
     * Return the initial context name.
     *
     * @return      the initial context name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the initial context name.
     *
     * @param name the initial context name.
     */
    public void setName(String initialContextName) {
        this.name = initialContextName;
    }

    /**
     * Get the map of parameters.
     *
     * @return      the map of parameters.
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Add a parameter to the map. Note that the paramters are stored in a map
     * where the name is the key and the value the actual value.
     *
     * @param config the ParameterConfiguration object storing the name and
     *               value for a parameter.
     */
    public void addParameter(ParameterConfiguration config) {
        parameters.put(config.getName(), config.getValue());
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

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 13-Jun-03	316/5	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/3	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
