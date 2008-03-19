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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/NativeMarkupAttributesTestCase.java,v 1.2 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Ported from metis.
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.protocols;

import junit.framework.TestCase;

/**
 * A test case for NativeMarkupAttributes
 */
public class NativeMarkupAttributesTestCase extends TestCase
{
    private NativeMarkupAttributes attributes;

    /**
     * Default junit constructor
     */
    public NativeMarkupAttributesTestCase(String name)
    {
        super(name);        
    }
    
    /**
     * Set up test fixtures
     */
    public void setUp()
    {
        attributes = new NativeMarkupAttributes();   
    }

    /**
     * Clean up after each test fixture
     */
    public void tearDown()
    {
        attributes = null;
    }
    
    /**
     * Test the getPane method
     */
    public void testGetPane()
    {
        assertNull("getPane should be null to start with", 
                    attributes.getPane());
        attributes.setPane("pane");
        assertEquals("getPane returned wrong value", "pane",
                      attributes.getPane());
    }
    
    /**
     * Test the getLocation method
     */
    public void testGetLocation()
    {
        assertNull("getTargetLocation should be null to start with", 
                    attributes.getTargetLocation());
        attributes.setTargetLocation("targetLocation");
        assertEquals("getTargetLocation returned wrong value", "targetLocation", 
                    attributes.getTargetLocation());
    }

    /**
     * Test the resetAttributes method
     */
    public void testResetAttributes()
    {
        attributes.setPane("pane");
        attributes.setTargetLocation("targetLocation");
        assertEquals("getPane returned wrong value", "pane",
                      attributes.getPane());
        assertEquals("getTargetLocation returned wrong value", "targetLocation", 
                    attributes.getTargetLocation());

        attributes.resetAttributes();
        assertNull("getPane should be null after call to resetAttributes", 
                    attributes.getPane());
        assertNull("getTargetLocation should be null after resetAttributes", 
                    attributes.getTargetLocation());
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
