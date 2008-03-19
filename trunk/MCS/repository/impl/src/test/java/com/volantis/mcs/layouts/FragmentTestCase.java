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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FragmentTestCase.java,v 1.3 2003/03/31 09:28:01 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Mar-03    Doug            VBM:2003030409 - Created. TestSuite for 
 *                              Fragments.
 * 26-Mar-03    Allan           VBM:2003021803 - Removed suite() and main(). 
 * 26-Mar-03    Allan           VBM:2003021803 - Removed suite() and main().
 * 28-Mar-03    Allan           VBM:2003030603 - Added clone tests for 
 *                              dissecingPanes, formatScope and 
 *                              orphanedDefaultFragment. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;



/**
 * This class unit tests the Fragment format.
 */
public class FragmentTestCase 
    extends FormatTestAbstract {

    // javadoc inherited from superclass
    protected Format createTestableFormat() {
        return new Fragment(new CanvasLayout());
    }

    /**
     * test the isParentLinkFirst method of Fragments
     */ 
    public void testIsParentLinkFirst() {
        Fragment fragment = (Fragment)createTestableFormat();
        // test to see that false is returned if no attribute is set
        assertEquals("Peer links should be first if no " +
                     "FRAG_LINK_ORDER_ATTRIBUTE is not set", 
                     false, fragment.isParentLinkFirst());
        // test to see that false is returned if the FRAG_LINK_ORDER_ATTRIBUTE
        // is set to Format.PEERS_FIRST
        fragment.setAttribute(FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE,
                              FormatConstants.PEERS_FIRST);
        assertEquals("Peer links should be first if  " +
                     "FRAG_LINK_ORDER_ATTRIBUTE is set to Format.PREERS_FIRST", 
                     false, fragment.isParentLinkFirst());
    
        // test to see that false is returned if the FRAG_LINK_ORDER_ATTRIBUTE
        // is set to Format.PARENT_FIRST
        fragment.setAttribute(FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE,
                              FormatConstants.PARENT_FIRST);
        assertEquals("Parent link should be first if " +
                     "FRAG_LINK_ORDER_ATTRIBUTE is set to Format.PARENT_FIRST", 
                     true, fragment.isParentLinkFirst());
      
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
