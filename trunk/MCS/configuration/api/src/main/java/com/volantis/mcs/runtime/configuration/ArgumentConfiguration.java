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
 * Provide a bean implementation for the markup-plugin argument configuration.
 */
public class ArgumentConfiguration {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * The name of the argument
     */ 
    private String name;

    /**
     * The value of the argument
     */ 
    private String value;

    /**
     * Get the value of the argument name.
     * @return the name of the argument.
     */ 
    public String getName() {
        return name;
    }

    /**
     * Set the value of the argument name
     * @param name - The name of the argument.
     */ 
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The the value of the argument value.
     * @return the value of the argument.
     */ 
    public String getValue() {
        return value;
    }

    /**
     * Set the value of the argument value.
     * @param value - the value of the argument.
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

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 ===========================================================================
*/
