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
/*
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/XFImplicitAttributesTestCase.java,v 1.2 2003/04/17 17:20:51 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-2003  Chris W         VBM:2003031909 - Created
 * 02-Apr-2003  Chris W         VBM:2003031909 - Removed setUp and tearDown.
 *                              attributes changed from an object attribute
 *                              into a local variable.
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.protocols;

import junit.framework.TestCase;

/**
 * This class tests the client variable name methods of
 * com.volantis.mcs.protocols.XFImplicitAttributes
 */
public class XFImplicitAttributesTestCase extends TestCase {

    /**
     * Default JUnit constructor
     * @param name
     */
    public XFImplicitAttributesTestCase(String name) {
        super(name);        
    }
    
    /**
     * Test the getClientVariableName method
     */
    public void testGetClientVariableName() {
        XFImplicitAttributes attributes = new XFImplicitAttributes();
        assertNull("getClientVariableName should be null to start with", 
                    attributes.getClientVariableName());
        attributes.setClientVariableName("clientVariableName");
        assertEquals("getClientVariableName returned wrong value",
                    "clientVariableName",
                    attributes.getClientVariableName());
    }
    
    /**
     * Test the resetAttributes method
     */
    public void testResetAttributes() {
        XFImplicitAttributes attributes = new XFImplicitAttributes();
        attributes.setClientVariableName("clientVariableName");        
        assertEquals("getClientVariableName returned wrong value", 
                     "clientVariableName",
                      attributes.getClientVariableName());

        attributes.resetAttributes();
        assertNull("getClientVariableName should be null after call to" +                   " resetAttributes", attributes.getClientVariableName());
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
