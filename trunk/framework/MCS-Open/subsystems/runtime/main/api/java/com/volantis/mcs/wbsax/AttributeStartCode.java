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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-May-03    Geoff           VBM:2003042904 - Created; represents the start 
 *                              of an attribute in a WBSAX event stream.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a "start" (name and optional value prefix) of an attribute in 
 * a WBSAX event stream. 
 */ 
public class AttributeStartCode extends AttributeCode {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The name of the attribute.
     */ 
    private String name;

    /**
     * The optional prefix for the attribute value; may be null. 
     */ 
    private String valuePrefix;

    /**
     * Construct an instance of this class from it's token value and attribute 
     * name (i.e. without a value prefix).
     * 
     * @param token the token value; must be within the valid range for 
     * attribute start tokens (0 - 0x7F). 
     * @param name the name of the attribute
     */ 
    AttributeStartCode(int token, String name) {
        if (token > ATTRIBUTE_START_MAX) {
            throw new IllegalArgumentException(
                    "Attribute start code " + token + " invalid");
        }
        setInteger(token);
        this.name = name;
    }

    /**
     * Construct an instance of this class from it's token value, attribute 
     * name and value prefix.
     * 
     * @param token the token value; must be within the valid range for 
     * attribute start tokens (0 - 0x7F). 
     * @param name the name of the attribute
     * @param valuePrefix the prefix of the attribute value.
     */ 
    AttributeStartCode(int token, String name, String valuePrefix) {
        this(token, name);
        this.valuePrefix = valuePrefix;
    }

    /**
     * Returns the name of the attribute.
     * 
     * @return the name of the attribute.
     */ 
    public String getName() {
        return name;
    }

    /**
     * Returns the optional value prefix; will be null if this attribute start
     * code did not have a value prefix.
     * 
     * @return the optional value prefix, or null if there was none.
     */ 
    public String getValuePrefix() {
        return valuePrefix;
    }

    // Inherit javadoc.
    public String toString() {
        return "[AttributeStartCode:name=" + name + ",valuePrefix=" + 
                valuePrefix + "," + super.toString() + "]";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
