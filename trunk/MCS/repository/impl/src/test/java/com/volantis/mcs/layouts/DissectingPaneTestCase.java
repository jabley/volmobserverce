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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/DissectingPaneTestCase.java,v 1.4 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Allan           VBM:2003021803 - A testcase for DissectingPane.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the 
 *                              testIsNextLinkFirst() fixture.
 * 25-Mar-03    Allan           VBM:2003021803 - Removed suite() and main().
 * 01-Apr-03    Chris W         VBM:2003031106 - Changed class hierarchy of
 *                              test cases to be same as layouts.  
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

/**
 * This class unit test the Formatclass.
 */
public class DissectingPaneTestCase 
    extends PaneTestCase {

    // javadoc inherited
    protected Format createTestableFormat() {
        return new DissectingPane(new CanvasLayout());
    }

    /**
     * test for the isNextLinkFirst method of DissectingPanes.
     */ 
    public void testIsNextLinkFirst() {
        DissectingPane dissectingPane = (DissectingPane)createTestableFormat();
        // test to see that false is returned if no attribute is set
        assertEquals("Previous link should be first if no " +
                     "SHARD_LINK_ORDER_ATTRIBUTE is provided", 
                     false, dissectingPane.isNextLinkFirst());
    
        // test to see that false is returned if the SHARD_LINK_ORDER_ATTRIBUTE
        // is set to Format.PREV_FIRST
        dissectingPane.setAttribute(FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE,
                                    FormatConstants.PREV_FIRST);
        assertEquals("Previous link should be first if " +
                     "SHARD_LINK_ORDER_ATTRIBUTE is set to Format.PREV_FIRST", 
                     false, dissectingPane.isNextLinkFirst());
        
        // test to see that false is returned if the SHARD_LINK_ORDER_ATTRIBUTE
        // is set to Format.PREV_FIRST
        dissectingPane.setAttribute(FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE, 
                                    FormatConstants.NEXT_FIRST);
        assertEquals("Next link should be first if " +
                     "SHARD_LINK_ORDER_ATTRIBUTE is set to Format.PREV_FIRST", 
                     true, dissectingPane.isNextLinkFirst());
      
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
