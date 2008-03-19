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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormTestCase.java,v 1.6 2003/04/01 16:35:09 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Feb-03    Allan           VBM:2003021803 - A testcase for Form.
 * 04-Mar-03    Allan           VBM:2003021802 - Modified
 *                              testHashCodeNonEqual... methods to use
 *                              ObjectTestHelper.
 * 18-Feb-03    Allan           VBM:2003021803 - A testcase for Form. 
 * 04-Mar-03    Allan           VBM:2003021802 - Modified 
 *                              testHashCodeNonEqual... methods to use 
 *                              ObjectTestHelper. 
 * 25-Mar-03    Allan           VBM:2003021803 - Removed suite() and main().
 * 27-Mar-03    Allan           VBM:2003030603 - Removed
 *                              testEqualsNonEqualFormFragments(). Added clone
 *                              tests for defaultPane and formFragments.
 * 01-Apr-03    Chris W         VBM:2003031106 - Override testGetChildAt() as
 *                              createTestableFormat() creates a Form with more
 *                              than one child. 
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.synergetics.ObjectHelper;

import java.util.ArrayList;

/**
 * This class unit test the Formatclass.
 */
public class FormTestCase
    extends FormatTestAbstract {

    // javadoc inherited
    protected Format createTestableFormat() {
        Form form = new Form(new CanvasLayout());

        Pane pane1 = new Pane(new CanvasLayout());
        Pane pane2 = new Pane(new CanvasLayout());

        form.insertChildAt(pane1, 0);
        form.insertChildAt(pane2, 1);

        FormFragment formFrag1 = new FormFragment(new CanvasLayout());
        FormFragment formFrag2 = new FormFragment(new CanvasLayout());

        form.addFormFragment(formFrag1);
        form.addFormFragment(formFrag2);

        return form;
    }

//    /**
//     * Test hashCode() on two Forms with different default Panes - the
//     * hashCode results should be different.
//     */
//    public void testHashCodeNonEqualDefaultPane() {
//        ArrayList objects =
//                new ArrayList(UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT);
//        for(int i=0; i<UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT;i++) {
//            Form form = (Form) createTestableFormat();
//            Pane pane = new Pane(new CanvasLayout());
//            pane.setName(String.valueOf(Math.random()));
//            form.insertChildAt(pane, 0);
//            objects.add(form);
//        }
//
//        assertTrue("Hashcode reliability has degraded to below 1/" +
//                   UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT,
//                   ObjectTestHelper.testHashCodesNonEqual(objects));
//    }

//    /**
//     * Test equals() on two Forms with different default Panes.
//     */
//    public void testEqualsNonEqualDefaultPane() {
//        Form form1 = (Form) createTestableFormat();
//        Form form2 = (Form) createTestableFormat();
//
//        Pane pane1 = new Pane(new CanvasLayout());
//        pane1.setName("A Pane");
//        Pane pane2 = new Pane(new CanvasLayout());
//        pane2.setName("Another Pane");
//
//        form1.insertChildAt(pane1, 0);
//
//        assertTrue("Forms have different children yet they appear the same",
//                   !(form1.equals(form2)));
//
//        form2.insertChildAt(pane2, 0);
//
//        assertTrue("Forms have different defaultPane yet they appear the same",
//                   !(ObjectHelper.equals(form1.getDefaultPane(),
//                                         form2.getDefaultPane())));
//
//        assertTrue("Forms have different defaultPane yet they appear the same",
//                   !(form1.equals(form2)));
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
        
        // the children have to be set again, as the original children were
        // created in createTestableFormat() and the local variables used in
        // createTestableFormat() are not accessible here. 
        parent.setChildAt(child1, 0);
        parent.setChildAt(child2, 1);
        
        assertNull("child -1 should be null", parent.getChildAt(-1));                
        assertSame("wrong child", child1, parent.getChildAt(0));
        assertSame("wrong child", child2, parent.getChildAt(1));
        assertNull("child 2 should be null", parent.getChildAt(2));
        assertNull("child 3 should be null", parent.getChildAt(3));
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
