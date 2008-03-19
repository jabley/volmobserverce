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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/GridTestCase.java,v 1.5 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Feb-03    Allan           VBM:2003021803 - A testcase for Grid. 
 * 04-Mar-03    Allan           VBM:2003021802 - Modified 
 *                              testHashCodeNonEqual... methods to use 
 *                              ObjectTestHelper. 
 * 25-Mar-03    Allan           VBM:2003021803 - Removed suite() and main(). 
 * 01-Apr-03    Chris W         VBM:2003031106 - Override testGetChildAt() as
 *                              createTestableFormat() creates a Grid with more
 *                              than one child. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * This class unit test the Format class.
 */
public abstract class AbstractGridTestAbstract
    extends FormatTestAbstract {

    /**
     * Setup the Grid that is provided by createTestableFormat(). This
     * method can used and/or overriden by sub-clases of this test case so
     * that their Grid will contain the right properties that the Grid
     * testcase methods require to work.
     * @param grid
     */ 
    protected void setupGrid(AbstractGrid grid) {
        grid.setRows(2);
        grid.setColumns(2);
        grid.attributesHaveBeenSet();
        
        Pane pane1 = new Pane(new CanvasLayout());
        Pane pane2 = new Pane(new CanvasLayout());
        Pane pane3 = new Pane(new CanvasLayout());
        Pane pane4 = new Pane(new CanvasLayout());
                
        try {
            grid.setChildAt(pane1, 0);
            grid.setChildAt(pane2, 1);
            grid.setChildAt(pane3, 2);
            grid.setChildAt(pane4, 3);
        }
        catch(Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }        
    }

//    /**
//     * Test the equals() method on two Grids with different SubComponentInfos.
//     */
//    public void testEqualsNonEqualSubComponentInfo() {
//        AbstractGrid one = (AbstractGrid) createTestableFormat();
//        AbstractGrid two = (AbstractGrid) createTestableFormat();
//
//        two.insertRow(2);
//
//        assertTrue("Grid \"one\" is different from grid \"two\" yet",
//                   !(one.equals(two)));
//
//        Enumeration oneSubComponents = one.getSubComponentInfo();
//        Enumeration twoSubComponents = two.getSubComponentInfo();
//
//        boolean equals = oneSubComponents == null ?
//                twoSubComponents == null :
//                twoSubComponents != null;
//
//        assertTrue(equals);
//
//        while(equals && oneSubComponents!=null &&
//                oneSubComponents.hasMoreElements()) {
//            equals = twoSubComponents.hasMoreElements();
//            if(equals) {
//                Format.SubComponentInfo oneInfo = (Format.SubComponentInfo)
//                        oneSubComponents.nextElement();
//                Format.SubComponentInfo twoInfo = (Format.SubComponentInfo)
//                        twoSubComponents.nextElement();
//                equals = oneInfo.equals(twoInfo);
//            }
//        }
//
//        assertTrue("SubComponentInfo are different yet they appear the same",
//                   !equals);
//    }
       
//    /**
//     * Test the hashCode() method on two Grids with different SubComponentInfos
//     * - the hashCode results should be different.
//     */
//    public void testHashCodeNonEqualSubComponentInfo() {
//        ArrayList objects = new ArrayList(10);
//
//        // Don't use HASHCODE_ACCEPTABILITY_CONSTANT because the test will
//        // take too long. We really only want to make sure that nothing
//        // dumb has happened to hashCode().
//        for(int i=0; i<10; i++) {
//            AbstractGrid one = (AbstractGrid) createTestableFormat();
//            for(int rows=0; rows<i; rows++) {
//                one.insertRow(1);
//            }
//            objects.add(one);
//        }
//
//        assertTrue("Hashcode reliability has degraded to below 1/10",
//                   ObjectTestHelper.testHashCodesNonEqual(objects));
//    }
     
    /**
     * This method tests the method public Format getChildAt ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testGetChildAt()
            throws Exception {
                
        Format parent = createTestableFormat();
        Format child1 = new Pane(new CanvasLayout());
        Format child2 = new Pane(new CanvasLayout());
        Format child3 = new Pane(new CanvasLayout());
        Format child4 = new Pane(new CanvasLayout());
        
        // the children have to be set again, as the original children were
        // created in createTestableFormat() and the local variables used in
        // createTestableFormat() are not accessible here. 
        parent.setChildAt(child1, 0);
        parent.setChildAt(child2, 1);
        parent.setChildAt(child3, 2);
        parent.setChildAt(child4, 3);

        
        assertNull("child -1 should be null", parent.getChildAt(-1));                
        assertSame("wrong child", child1, parent.getChildAt(0));
        assertSame("wrong child", child2, parent.getChildAt(1));
        assertSame("wrong child", child3, parent.getChildAt(2));
        assertSame("wrong child", child4, parent.getChildAt(3));
        assertNull("child 4 should be null", parent.getChildAt(4));
        assertNull("child 5 should be null", parent.getChildAt(5));
    }

    /**
     * This tests the default value of the widthUnits attribute using
     * AbstractGrid.Column#getWidthUnits.
     */
    public void testGetWidthUnitsDefault() throws Exception {
        AbstractGrid gridFormat = (AbstractGrid) createTestableFormat();
        Column column = gridFormat.getColumn(0);

        // Remove the width units.
        column.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE, null);

        assertNull("widthUnits attribute should be null",
                column.getAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE));

        assertEquals("Width units should default to percent",
                FormatConstants.WIDTH_UNITS_VALUE_PERCENT,
                column.getDeprecatedWidthUnits());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 21-Feb-05	7037/3	pcameron	VBM:2005021704 Width units default to percent if not present

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
