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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/DeviceLayoutReplicatorTestCase.java,v 1.2 2003/03/26 15:32:13 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Mar-2003  Chris W         VBM:2003022706 - Created
 * 26-Mar-03    Allan           VBM:2003021803 - Modified usages of the 
 *                              DeviceLayout no-arg constructor to use the 
 *                              String, String version instead. 
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;

/**
 * Tests the DeviceLayoutReplicator.
 */
public class DeviceLayoutReplicatorTestCase
        extends TestCase {

    private DeviceLayoutReplicator replicator;
    private CanvasLayout layout;
    private Fragment outerFrag;
    private Fragment innerFrag;
    private Pane pane;
        
    /**
     * Setup the test
     */
    public void setUp()
    {
        replicator = new DeviceLayoutReplicator();
        layout = new CanvasLayout();
        
        outerFrag = new Fragment(layout);
        outerFrag.setName("outer");
        
        innerFrag = new Fragment(layout);
        innerFrag.setName("inner");
        innerFrag.setParent(outerFrag);      
        
        pane = new Pane(layout);
        pane.setName("pane");
        pane.setParent(innerFrag);
                
        layout.setRootFormat(outerFrag);
    }

    /**
     * Clean up after tests
     */
    public void tearDown()
    {
        replicator = null;
        layout = null;
        outerFrag = null;
        innerFrag = null;
        pane = null;
    }

    /**
     * Tests that replicator doesn't throw an exception when it deals with a
     * fragment inside another fragment.
     */    
    public void testReplicate()
    {
        // Set the children of each fragment appropriately. This is part of
        // the test setup.
        try
        {
            outerFrag.setChildAt(innerFrag, 0);
            innerFrag.setChildAt(pane, 0);
        }
        catch (LayoutException e)
        {            
            fail(e.getMessage());
        }        
        
        // Execute the unit test.
        try
        {
            replicator.replicate(layout);
        }
        catch (IllegalArgumentException e)
        {
            fail("Should not have thrown an IllegalArgumentException "
                 + e.getMessage());
        }
        
        assertSame("Layout FormatScope should contain outer fragment",
              outerFrag, layout.retrieveFormat("outer", FormatType.FRAGMENT));
        assertSame("Layout FormatScope should contain inner fragment",
              innerFrag, layout.retrieveFormat("inner", FormatType.FRAGMENT));
        assertNull("Layout FormatScope should not contain pane",
                         layout.retrieveFormat("pane", FormatType.PANE));
                               
        assertSame("Outer fragment FormatScope should contain outer fragment",
           outerFrag, outerFrag.retrieveFormat("outer", FormatType.FRAGMENT));
        assertSame("Outer fragment FormatScope should contain inner fragment",
           innerFrag, outerFrag.retrieveFormat("inner", FormatType.FRAGMENT));
        assertNull("Outer fragment FormatScope should not contain pane",
                      outerFrag.retrieveFormat("pane", FormatType.PANE));
    
        assertSame("Inner fragment FormatScope should contain outer fragment",
           outerFrag, innerFrag.retrieveFormat("outer", FormatType.FRAGMENT));
        assertSame("Inner fragment FormatScope should contain inner fragment",
           innerFrag, innerFrag.retrieveFormat("inner", FormatType.FRAGMENT));
        assertSame("Inner fragment FormatScope should contain pane",
                pane, innerFrag.retrieveFormat("pane", FormatType.PANE));
                                        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
