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
 * 14-May-03    Geoff           VBM:2003042904 - Created; a factory for 
 *                              creating AttributeValueCodes. 
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating {@link AttributeValueCode}s.
 * <p>
 * Utilises/implements the Flyweight pattern to avoid excess garbage.
 */ 
public class AttributeValueFactory implements AttributeValueRegistrar {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Map of {@link AttributeValueCode}s, by name.
     */ 
    private Map attrValues = new HashMap();

    public AttributeValueFactory() {
    }

    /**
     * Create an {@link AttributeValueCode} from an attribute value part.
     * 
     * @param valuePart part of an attribute value.
     * @return the created attribute value code. 
     */ 
    public AttributeValueCode create(String valuePart) {
        AttributeValueCode attr = 
                (AttributeValueCode) attrValues.get(valuePart);
        if (attr == null) {
            throw new IllegalArgumentException(
                    "Unregistered attribute value " + valuePart);
        }
        return attr;
    }

    // Inherit Javadoc.
    public void registerAttributeValue(int token, String valuePart) {
        AttributeValueCode attr = new AttributeValueCode(token, valuePart);
        attrValues.put(valuePart, attr);
    }
    
    // There should really be byte[] versions of the above methods as well?
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/2	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
