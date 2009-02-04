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
 * Provide a bean implementation for the parameter configuration.
 */
public class ParameterConfiguration {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name of the parameter.
     */
    private String name;

    /**
     * The value of the parameter.
     */
    private String value;

    /**
     * Get the name of the parameter.
     *
     * @return      the name of hte parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the parameter.
     *
     * @param name the name of the parameter.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the value of the parameter.
     *
     * @return      the value of the parameter.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of the parameter.
     *
     * @param value the value of the parameter.
     */
    public void setValue(String value) {
        this.value = value;
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

 13-Jun-03	316/3	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
