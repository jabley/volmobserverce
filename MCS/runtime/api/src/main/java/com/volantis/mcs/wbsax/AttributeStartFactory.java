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
 *                              creating AttributeStartCodes.
 * 22-May-03    Mat             VBM:2003042907 - Added getAttributePrefixes()
 * 29-May-03    Geoff           VBM:2003042905 - Remove getAttributePrefixes()
 *                              and implement features which replace it.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * A factory for creating {@link AttributeStartCode}s.
 * <p>
 * Utilises/implements the Flyweight pattern to avoid excess garbage.
 * 
 * @todo the contract for create should be modified to allow us to detect 
 * unregistered attribute starts without triggering an exception. This would
 * then allow us to create literal named attributes as a fall back much more
 * conveniently than is now possible.
 */ 
public class AttributeStartFactory implements AttributeStartRegistrar {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Map of Map of {@link AttributeStartCode}s with value prefix, by name
     * and then value prefix.
     */ 
    private Map codesByNameValue = new HashMap();

    public AttributeStartFactory() {
    }

    /**
     * Create an {@link AttributeStartCode} from an attribute name and value.
     * <p>
     * This may return a start code with the value prefix equal to null. If
     * so, this means that no value prefix was found. If not, it means that
     * a value prefix was found, and the client should avoid submitting the 
     * first value prefix length characters of the value as the attribute
     * value in the call to {@link WBSAXContentHandler#addAttributeValue}. 
     * 
     * @param name the name of the attribute.
     * @param value the prefix of the attribute value, may be null if the 
     *      client wishes to avoid creating a value prefix.
     * 
     * @return the created attribute start code.
     * @throws IllegalArgumentException if the attribute name is not known; 
     *      see to do in class comment for more info.
     */ 
    public AttributeStartCode create(String name, String value) {
        Map codesByValuePrefix = (Map) codesByNameValue.get(name);
        if (codesByValuePrefix == null) {
            throw new IllegalArgumentException(
                    "Unregistered attribute name " + name);
        }
        AttributeStartCode code = null;
        // If they provided a value 
        if (value != null) {
            // Loop over the keys, looking to see if the value starts with any.
            // Note this would fail if any key contains another!
            Iterator iterator = codesByValuePrefix.keySet().iterator();
            while (iterator.hasNext() && code == null) {
                String key = (String) iterator.next();
                if (key != null) {
                    if (value.startsWith(key)) {
                        code = (AttributeStartCode) 
                                codesByValuePrefix.get(key);
                    }
                }
            }
        }
        // If we couldn't find a matching value prefix, they try and find a
        // default code just for the name.
        if (code == null) {
            code = (AttributeStartCode) codesByValuePrefix.get(null);
        }
        if (code == null) {
            throw new IllegalArgumentException(
                    "No attribute start code available for name " + name + 
                    ", value " + value);
        }
        return code;
    }


    // Inherit Javadoc.
    public void registerAttributeStart(int token, String name) {
        registerAttributeStart(token, name, null);
    }
    
    // Inherit Javadoc.
    public void registerAttributeStart(int token, String name, String valuePrefix) {
        AttributeStartCode attr = 
                new AttributeStartCode(token, name, valuePrefix);
        Map attrsByValue = (Map) codesByNameValue.get(name);
        if (attrsByValue == null) {
            attrsByValue = new HashMap();
            codesByNameValue.put(name, attrsByValue);
        }
        attrsByValue.put(valuePrefix, attr);
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

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 14-Jul-03	790/2	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
