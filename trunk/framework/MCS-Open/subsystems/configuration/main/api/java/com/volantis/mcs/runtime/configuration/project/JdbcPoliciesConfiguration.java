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
package com.volantis.mcs.runtime.configuration.project;

/**
 * Holds configuration information about a project's jdbc policies.
 * <p>
 * This corresponds to the mcs-config/projects/[default|project]/jdbc-policies
 * element.
 */ 
public class JdbcPoliciesConfiguration extends AbstractPoliciesConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The name of the project within the jdbc repository.
     */ 
    private String name;

    /**
     * Returns the name of the project within the jdbc repository.
     */ 
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project within the jdbc repository.
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Jan-04	2724/3	geoff	VBM:2004011911 Add projects to config (whoops - add javadoc)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
