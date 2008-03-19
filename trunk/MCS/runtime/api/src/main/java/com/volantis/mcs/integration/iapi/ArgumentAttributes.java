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
 
package com.volantis.mcs.integration.iapi;

/**
 * The attributes of an argument element
 */
public class ArgumentAttributes implements IAPIAttributes {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name attribute
     */ 
    private String name;

    /**
     * The value attribute
     */ 
    private String value;
    
    /**
     * Get the name attribute
     * @return the name attribute
     */ 
    public String getName() {
        return name;
    }

    /**
     * Set the name attribute
     * @param name the name attribute
     */ 
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value attribute
     * @return the value attribute
     */ 
    public String getValue() {
        return value;
    }

    /**
     * Set the value attribute
     * @param value the value attribute
     */ 
    public void setValue(String value) {
        this.value = value;
    }

    // Javadoc inherited from IAPIAttributes interface
    public void reset() {
        name = null;
        value = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
