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
package com.volantis.testtools.config;

/**
 * A simple ValueObject use with {@link ConfigValue} to configure Mariner
 * project xml policy values.
 */ 
public class ConfigProjectPoliciesXmlValue extends ConfigProjectPoliciesValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * The project directory for these xml policies.
     */ 
    public String projectDir;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 ===========================================================================
*/
