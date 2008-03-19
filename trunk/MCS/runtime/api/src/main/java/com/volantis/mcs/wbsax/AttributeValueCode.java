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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents a part of 
 *                              an attribute value in a WBSAX event stream. 
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a part of an attribute value in a WBSAX event stream. 
 */ 
public class AttributeValueCode extends AttributeCode 
    implements AttributeValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Part of an attribute value.
     */ 
    private String value;

    /**
     * Construct an instance of this class from it's token value and 
     * attribute value.
     * 
     * @param token the token value; must be within the valid range for 
     * attribute value tokens (0x80 - 0xFF). 
     * @param valuePart a part of the attribute value.
     */ 
    public AttributeValueCode(int token, String valuePart) {
        if (token <= ATTRIBUTE_START_MAX) {
            throw new IllegalArgumentException(
                    "Attribute start code " + token + " invalid");
        }
        setInteger(token);
        this.value = valuePart;
    }

    /**
     * Returns the part of an attribute value.
     * 
     * @return the part of an attribute value.
     */ 
    public String getValue() {
        return value;
    }
    
    // Inherit javadoc.
    public String toString() {
        return "[AttributeValueCode:value=" + value + "," + 
                super.toString() + "]";
    }

    // Inherit javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        ((AttributeValueVisitor)visitor).visitValue(this);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
