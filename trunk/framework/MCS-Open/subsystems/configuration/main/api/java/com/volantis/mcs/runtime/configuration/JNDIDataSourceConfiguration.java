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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

/**
 * Provide a bean implementation of the JNDI configuration.
 */
public class JNDIDataSourceConfiguration implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";

    /**
     * The reference name to the configuration entry for defining the JDNI
     * initial context.
     */
    private String initialContext;

    /**
     * The name of this dataSource.
     */
    private String name;

    /**
     * Return the initial context of the jndi configuration.
     *
     * @return      the initial context of the jndi configuration.
     */
    public String getInitialContext() {
        return initialContext;
    }

    /**
     * Set the initial context of the jndi configuration.
     *
     * @param initialContext the initial context of the jndi configuration.
     */
    public void setInitialContext(String initialContext) {
        this.initialContext = initialContext;
    }

    /**
     * Get the name of the configuration.
     *
     * @return      the name of the configuration.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the configuration.
     *
     * @param name the name of this configuration.
     */
    public void setName(String name) {
        this.name = name;
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

 ===========================================================================
*/
