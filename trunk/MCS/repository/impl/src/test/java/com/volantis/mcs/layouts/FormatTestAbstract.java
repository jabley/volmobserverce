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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatTestAbstract.java,v 1.7 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Allan           VBM:2003021803 - A testcase for Format.
 * 04-Mar-03    Allan           VBM:2003021802 - Modified
 *                              testHashCodeNonEqual... methods to use
 *                              ObjectTestHelper.
 * 10-Mar-03    Allan           VBM:2003021801 - Modify
 *                              testHashCodeNonEqualNameAttribute() to use
 *                              HASHCODE_ACCEPTABLILITY_CONSTANT for the for
 *                              loop where previously this was objects.size() -
 *                              i.e. 0.
 * 26-Mar-03    Allan           VBM:2003021803 - Improved javadoc on
 *                              createTestableFormat().
 * 27-Mar-03    Allan           VBM:2003030603 - Added testClone() and
 *                              testCloneModifiedNameNotEquals().
 * 01-Apr-03    Chris W         VBM:2003031106 - Ported testGetChildAt() from
 *                              the soon to be removed FormatTestCase class 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;

/**
 * This class unit test the Formatclass.
 */
public abstract class FormatTestAbstract
        extends TestCaseAbstract {

//    /**
//     * Test that the hashCode() method returns the same value for two
//     * equal Formats.
//     */
//    public void testHashCodeEqual() {
//        Format one = createTestableFormat();
//        Format two = createTestableFormat();
//
//        assertEquals(one, two);
//
//        assertEquals(one.hashCode(), two.hashCode());
//    }

//    /**
//     * Test the hashCode() method on Formats with different names.
//     */
//    public void testHashCodeNonEqualNameAttribute() {
//        ArrayList objects =
//                new ArrayList(UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT);
//
//        for(int i=0; i<UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT;
//            i++) {
//            Format one = createTestableFormat();
//            one.setName(String.valueOf(Math.random() + i));
//            objects.add(one);
//        }
//
//        assertTrue("Hashcode reliability has degraded to below 1/" +
//                   UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT,
//                   ObjectTestHelper.testHashCodesNonEqual(objects));
//    }

//    /**
//     * Test the equals() method on two equal Formats.
//     */
//    public void testEqualsEqual() {
//        Format one = createTestableFormat();
//        Format two = createTestableFormat();
//
//        assertEquals(one, two);
//    }

//    /**
//     * Test the equals() method on two Formats with different names.
//     */
//    public void testEqualsNonEqualNameAttribute() {
//        Format one = createTestableFormat();
//        Format two = createTestableFormat();
//        one.setName("One");
//        two.setName("Two");
//        assertTrue("Format name of \"" + one.getName() +
//                   "\" is different from \"" + two.getName() + "\"",
//                   !(one.equals(two)));
//    }

//    /**
//     * Test the equals() method on two Formats with different numbers of
//     * children.
//     */
//    public void testEqualsNonEqualNumChildren() {
//        Format one = createTestableFormat();
//        Format two = createTestableFormat();
//        one.setNumChildren(1);
//        two.setNumChildren(2);
//        assertTrue("Number of format children is different: " +
//                   one.getNumChildren() + " != " +
//                   two.getNumChildren(), !(one.equals(two)));
//    }

    /**
     * This method tests the method public void setName ( String )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testSetName()
            throws Exception {

        Format format = createTestableFormat();
        format.setName("name");
        assertEquals("name", format.getName());
    }

    /**
     * This method tests the method public String getName ( )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testGetName()
            throws Exception {

        Format format = createTestableFormat();
        format.setName("name");
        assertEquals("name", format.getName());
    }

    /**
     * This method tests the method public Format getChildAt ( int )
     * for the com.volantis.mcs.layouts.Format class.
     */
    public void testGetChildAt()
            throws Exception {
                
        Format parent = createTestableFormat();
        Format child = new Pane(new CanvasLayout());
        
        parent.setChildAt(child, 0);

        /* Most formats can take one child so we test
         * -1, a negative number
         *  0, should return the child
         *  1, test the newly introduced equals sign in the getChildAt()
         *     i.e. when children.length equals index
         *     which is the real point of this vbm.
         *  2, tests when index is strictly greater than children.length  
         */
        assertNull("child -1 should be null", parent.getChildAt(-1));                
        assertSame("wrong child", child, parent.getChildAt(0));
        assertNull("child 1 should be null", parent.getChildAt(1));
        assertNull("child 2 should be null", parent.getChildAt(2));
    }

    /**
     * This tests the default value of the widthUnits attribute using
     * Format#getWidthUnits.
     */
    public void testGetWidthUnitsDefault() throws Exception {
        Format format = createTestableFormat();

        // Remove the width units.
        format.setWidthUnits(null);

        assertNull("widthUnits attribute should be null",
                format.getAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE));

        assertEquals("Width units should default to percent",
                FormatConstants.WIDTH_UNITS_VALUE_PERCENT,
                format.getWidthUnits());
    }

    /**
     * Create Format that can be used for testing general Format methods.
     * This method must always produce Formats that are equal but not the
     * same e.g. two calls to Grid.createTestableFormat() will produce two
     * separate Grid instances that when compared with equals() return true. 
     * @return A Format.
     */
    protected abstract Format createTestableFormat() throws LayoutException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 21-Feb-05	7037/4	pcameron	VBM:2005021704 Width units default to percent if not present

 18-Feb-05	7037/2	pcameron	VBM:2005021704 Width units default to percent if not present

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
