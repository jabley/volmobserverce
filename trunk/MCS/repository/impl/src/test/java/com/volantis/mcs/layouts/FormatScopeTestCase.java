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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/layouts/FormatScopeTestCase.java,v 1.3 2003/03/11 08:54:28 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Oct-02    Allan           VBM:2002103107 - A testcase for
 *                              FormatRegister.
 * 10-Dec-02    Allan           VBM:2002110102 - FormatRegister has been
 *                              renamed to FormatScope and this testcase
 *                              name has changed accordingly. Also some
 *                              new tests have been added.
 * 04-Mar-03    Allan           VBM:2003021802 - Added test methods for 
 *                              equals() and hashCode(). 
 * 10-Mar-03    Allan           VBM:2003021801 - Changed 
 *                              testHashCodeNotEqualsNameAttribute to use i in 
 *                              the name to ensure uniqeness. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * This class unit test the FormatRegisterclass.
 */
public class FormatScopeTestCase
        extends TestCase {

    protected CanvasLayout canvasLayout;
    protected MontageLayout montageLayout;
    protected Pane format;
    protected FormatScope formatScope;

    /**
     * Set up the layout for this testcase.
     */
    public void setUp() {
        canvasLayout = new CanvasLayout();
        montageLayout = new MontageLayout();
        format = new Pane(canvasLayout);
        format.setName("Pane");
        formatScope = new FormatScope();
    }

    /**
     * Tear down everything that was set up.
     */
    public void tearDown() {
        format = null;
        canvasLayout = null;
        formatScope = null;
    }

    public FormatScopeTestCase(String name) {
        super(name);
    }

    /**
     * This method tests FormatScopes that have excluded FormatTypes.
     */
    public void testExcludedFormatTypes() {
        FormatType types1 [] = { FormatType.FRAGMENT };

        formatScope.setExcludedFormatTypes(types1);
        formatScope.addFormat(format);
        try {
            Fragment fragment = new Fragment(canvasLayout);
            fragment.setName("fragment");
            formatScope.addFormat(fragment);
            fail("Successfully added excluded FormatType: " + fragment);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        FormatType types2 [] = {FormatType.COLUMN_ITERATOR_PANE,
                                FormatType.DISSECTING_PANE,
                                FormatType.FORM,
                                FormatType.FORM_FRAGMENT,
                                FormatType.FRAGMENT,
                                FormatType.GRID,
                                FormatType.PANE,
                                FormatType.REGION,
                                FormatType.REPLICA,
                                FormatType.ROW_ITERATOR_PANE,
                                FormatType.SEGMENT,
                                FormatType.SEGMENT_GRID,
                                FormatType.SPATIAL_FORMAT_ITERATOR,
                                FormatType.TEMPORAL_FORMAT_ITERATOR
        };

        formatScope.setExcludedFormatTypes(types2);

        Format format = new ColumnIteratorPane(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new Form(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new FormFragment(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new Fragment(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new Grid(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new Region(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }
        format = new Replica(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }
        format = new Segment(montageLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new SegmentGrid(montageLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new SpatialFormatIterator(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }

        format = new TemporalFormatIterator(canvasLayout);
        format.setName("name");
        try {
            formatScope.addFormat(format);
            fail("Successfully added excluded FormatType: " + format);
        }
        catch(IllegalArgumentException e) {
            // If we are here the test was successful.
        }
    }

    /**
     * This method tests FormatScopes that share name->Format maps.
     */
    public void testSharedFormatScope() {
        FormatType types [] = { FormatType.FRAGMENT };
        FormatScope sharedScope = new FormatScope(formatScope, types);

        Fragment fragment = new Fragment(canvasLayout);
        fragment.setName("fragment");

        Pane pane = new Pane(canvasLayout);
        pane.setName("pane");

        formatScope.addFormat(fragment);
        formatScope.addFormat(pane);
        sharedScope.addFormat(pane);

        // Commented out when we removed validation on load for format type.
//        try {
//            sharedScope.addFormat(fragment);
//            fail("Added Format " + fragment + " to shared name->FormatMap " +
//                 "that should have already contained this Format");
//
//        }
//        catch(IllegalArgumentException e) {
//            // If we are here the test passed.
//        }

        FormatType multiTypes [] = { FormatType.FRAGMENT, FormatType.PANE,
                                     FormatType.COLUMN_ITERATOR_PANE,
                                     FormatType.DISSECTING_PANE,
                                     FormatType.FORM
        };

        FormatScope sharedScope2 = new FormatScope(sharedScope, multiTypes);

        assertSame(pane, sharedScope2.removeFormat(pane));
        assertNull(sharedScope.removeFormat(pane));
        assertSame(fragment, sharedScope2.removeFormat(fragment));
        assertNull(sharedScope.removeFormat(fragment));
        assertNull(formatScope.removeFormat(fragment));

        sharedScope.addFormat(fragment);
        assertSame(fragment,
                     formatScope.retrieveFormat("fragment",
                                                FormatType.FRAGMENT));
        assertSame(fragment,
                     sharedScope2.retrieveFormat("fragment",
                                                   FormatType.FRAGMENT));
        sharedScope2.addFormat(pane);

        Format cip = new ColumnIteratorPane(canvasLayout);
        cip.setName("cip");

        Format dp = new DissectingPane(canvasLayout);
        dp.setName("dp");

        Format form = new Form(canvasLayout);
        form.setName("form");

        sharedScope2.addFormat(cip);
        sharedScope2.addFormat(dp);
        sharedScope2.addFormat(form);

        assertSame(cip,
                     sharedScope.retrieveFormat("cip",
                                                FormatType.COLUMN_ITERATOR_PANE));
        assertSame(dp,
                         sharedScope.retrieveFormat("dp",
                                                    FormatType.DISSECTING_PANE));
        assertSame(form,
                         sharedScope.retrieveFormat("form",
                                                    FormatType.FORM));


    }

    /**
     * This method tests the replaceFormatPositive() method of FormatScope.
     * This test will pass if there are no problems replacing formats.
     */
    public void testReplaceFormatPositive() {
        try {
            format.setNumChildren(2);
            Pane child1 = new Pane(canvasLayout);
            child1.setName("child1");
            // todo: use addFormat() if setName() stops doing addFormat().
            format.setChildAt(child1, 0);

            Pane child1a = new Pane(canvasLayout);
            child1a.setName("child1a");
            format.setChildAt(child1a, 1);

            CanvasLayout layout2 = new CanvasLayout();
            Format grid = new Grid(layout2);
            grid.setNumChildren(2);
            Pane child2 = new Pane(layout2);
            child2.setName("child1");
            grid.setChildAt(child2, 0);
            Pane child3 = new Pane(layout2);
            child3.setName("child1a");
            grid.setChildAt(child3, 1);
            grid.setName("grid");

            formatScope.addFormatRecursive(format);
            formatScope.replaceFormat(format, grid);

            assertNull(formatScope.
                       retrieveFormat(format.getName(),
                                      format.getFormatType()));
            assertSame(grid,
                         formatScope.retrieveFormat("grid",
                                                    FormatType.GRID));
            assertSame(child2,
                         formatScope.retrieveFormat("child1",
                                                    FormatType.PANE));
            assertSame(child3,
                         formatScope.retrieveFormat("child1a",
                                                    FormatType.PANE));
        }
        catch(LayoutException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * This method tests the replaceFormatPositive() method of FormatType.
     * This test will fail if there are no problems replacing formats.
     */
    public void testReplaceFormatNegative() {
        try {
            format.setNumChildren(2);
            Pane child1 = new Pane(canvasLayout);
            child1.setName("child1");
            format.setChildAt(child1, 0);

            Pane child1a = new Pane(canvasLayout);
            child1a.setName("child2");
            format.setChildAt(child1a, 1);

            CanvasLayout layout2 = new CanvasLayout();
            Format grid = new Grid(layout2);
            grid.setNumChildren(2);
            Pane child2 = new Pane(layout2);
            child2.setName("child2");
            grid.setChildAt(child2, 0);
            Pane child3 = new Pane(layout2);
            child3.setName("child3");
            grid.setChildAt(child3, 1);
            grid.setName("grid");

            // Replace a format that is not in formatScope
            try {
                formatScope.replaceFormat(format, grid);
                fail("Managed to replace a format that was not in " +
                     "the formatScope");
            }
            catch(IllegalArgumentException e) {
                // If we are here, the test was successfull.
            }

            // Commented out when we removed validation on load for format type.
//            // Replace a format containing a duplicate Pane
//            try {
//                formatScope.addFormatRecursive(format);
//                formatScope.replaceFormat(child1, grid);
//                fail("Managed to replace a format with another format" +
//                     " that contained a duplicate format");
//            }
//            catch(IllegalArgumentException e) {
//                // If we are here then the replace failed as it should
//                // However, we need to make sure that the operation
//                // has not affected the FormatType.
//                assertSame(child1,
//                             formatScope.retrieveFormat("child1",
//                                                        FormatType.PANE));
//                assertSame(child1a,
//                             formatScope.retrieveFormat("child2",
//                                                        FormatType.PANE));
//                assertSame(format,
//                             formatScope.
//                             retrieveFormat(format.getName(),
//                                            format.getFormatType()));
//
//                assertNull(formatScope.retrieveFormat("grid",
//                                                      FormatType.GRID));
//                assertNull(formatScope.retrieveFormat("child3",
//                                                      FormatType.PANE));
//            }
        }
        catch(LayoutException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * This method tests the addFormatRecusive() method of FormatType. This
     * test will pass if there are no problems adding a format with children
     * using the addFormatRecursive() method.
     */
    public void testAddFormatRecusivePositive() {
        try {
            format.setNumChildren(2);
            Pane child1 = new Pane(canvasLayout);
            child1.setName("child1");
            // todo: use addFormat() if setName() stops doing addFormat().

            format.setChildAt(child1, 0);
            Format grid = new Grid(canvasLayout);
            grid.setNumChildren(2);
            Pane child2 = new Pane(canvasLayout);
            child2.setName("child2");
            grid.setChildAt(child2, 0);
            Pane child3 = new Pane(canvasLayout);
            child3.setName("child3");
            grid.setChildAt(child3, 1);
            grid.setName("grid");
            format.setChildAt(grid, 1);

            formatScope.addFormatRecursive(format);

            assertSame(child1,
                         formatScope.retrieveFormat("child1",
                                                    FormatType.PANE));
            assertSame(grid,
                         formatScope.retrieveFormat("grid",
                                                    FormatType.GRID));
            assertSame(child2,
                         formatScope.retrieveFormat("child2",
                                                    FormatType.PANE));
            assertSame(child3,
                         formatScope.retrieveFormat("child3",
                                                    FormatType.PANE));
        }
        catch(LayoutException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // Commented out when we removed validation on load for format type.
//    /**
//     * The method tests the addFormatRecusiveMethod() of FormatType. This
//     * test will fail if there are no problems adding a format with children
//     * using the addFormatRecursive() method.
//     */
//    public void testAddFormatRecusiveNegative() {
//        try {
//            format.setNumChildren(2);
//            Pane child1 = new Pane(canvasLayout);
//            child1.setName("child1");
//            formatScope.addFormat(child1);
//
//            // todo: use addFormat() if setName() stops doing addFormat().
//
//            format.setChildAt(child1, 0);
//            Format grid = new Grid(canvasLayout);
//            grid.setNumChildren(2);
//            Pane child2 = new Pane(canvasLayout);
//            child2.setName("child2");
//            grid.setChildAt(child2, 0);
//            Pane child3 = new Pane(canvasLayout);
//            child3.setName("child2"); // duplicate name should cause a failure
//            grid.setChildAt(child3, 1);
//            grid.setName("grid");
//            format.setChildAt(grid, 1);
//
//            formatScope.addFormatRecursive(format);
//            fail("Managed to add a format with a duplicate name!");
//        }
//        catch(IllegalArgumentException e) {
//            // If we are here then the test passed.
//        }
//        catch(LayoutException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }

    /**
     * This method tests the method public void addFormat ( Format )
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testAddFormat()
            throws Exception {

        formatScope.addFormat(format);
        Format retrieved =
                formatScope.retrieveFormat(format.getName(),
                                           format.getFormatType());

        assertSame(format, retrieved);

        // Commented out when we removed validation on load for format type.
//        try {
//            formatScope.addFormat(format);
//            fail("Added a duplicate format to FormatScope");
//        }
//        catch(IllegalArgumentException e) {
//            // Success if we are here.
//        }

        try {
            format.setName(null);
            formatScope.addFormat(format);
            fail("Added a format with a null name");
        }
        catch(IllegalArgumentException e) {
            // Success if we are here.
        }
    }

    /**
     * This method tests the method public void removeFormat ( Format )
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testRemoveFormat()
            throws Exception {

        Pane unknown = new Pane(canvasLayout);
        unknown.setName("unknown");

        assertNull(formatScope.removeFormat(unknown));
        
        formatScope.addFormat(format);
        assertNotNull(formatScope.removeFormat(format));
        Format retrieved =
                formatScope.retrieveFormat(format.getName(),
                                           format.getFormatType());

        assertNull(retrieved);

        try {
            format.setNumChildren(2);
            Pane child1 = new Pane(canvasLayout);
            child1.setName("child1");

            format.setChildAt(child1, 0);
            Format grid = new Grid(canvasLayout);
            grid.setNumChildren(2);
            Pane child2 = new Pane(canvasLayout);
            child2.setName("child2");
            grid.setChildAt(child2, 0);
            Pane child3 = new Pane(canvasLayout);
            child3.setName("child3");
            grid.setChildAt(child3, 1);
            grid.setName("grid");
            format.setChildAt(grid, 1);

            formatScope.addFormatRecursive(format);
            assertNotNull(formatScope.removeFormat(format));

            // Ensure that all the formats have been removed.
            assertNull(formatScope.retrieveFormat("child1",
                                                  FormatType.PANE));
            assertNull(formatScope.retrieveFormat("grid",
                                                  FormatType.GRID));
            assertNull(formatScope.retrieveFormat("child2",
                                                  FormatType.PANE));
            assertNull(formatScope.retrieveFormat("child3",
                                                  FormatType.PANE));
            assertNull(formatScope.
                       retrieveFormat(format.getName(),
                                      format.getFormatType()));
        }
        catch(LayoutException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * This method tests the method public Format retrieveFormat (Class,String)
     * for the com.volantis.mcs.layouts.FormatRegister class.
     */
    public void testRetrieveFormat()
            throws Exception {

        formatScope.addFormat(format);

        Format retrieved =
                formatScope.retrieveFormat(format.getName(),
                                           format.getFormatType());

        assertSame(format, retrieved);

        retrieved =
                formatScope.retrieveFormat(format.getName(),
                                           format.getFormatType());

        assertSame(format, retrieved);
    }

//    /**
//     * Test equals() with two equal FormatScopes
//     */
//    public void testEqualsEqual() {
//        FormatScope scope1 = new FormatScope();
//        FormatScope scope2 = new FormatScope();
//
//        assertSame(scope1, scope2);
//
//        CanvasLayout layout = new CanvasLayout();
//        Pane pane1 = new Pane(layout);
//        Pane pane2 = new Pane(layout);
//        pane1.setName("Pane1");
//        pane2.setName("Pane2");
//
//        scope1.addFormat(pane1);
//        scope1.addFormat(pane2);
//
//        scope2.addFormat(pane1);
//        scope2.addFormat(pane2);
//
//        assertSame(scope1, scope2);
//    }

//    /**
//     * Test equals() with two non-equal FormatScopes
//     */
//    public void testEqualsNotEqual() {
//        FormatScope scope1 = new FormatScope();
//        FormatScope scope2 = new FormatScope();
//
//        assertSame(scope1, scope2);
//
//        CanvasLayout layout = new CanvasLayout();
//        Pane pane1 = new Pane(layout);
//        Pane pane2 = new Pane(layout);
//        pane1.setName("Pane1");
//        pane2.setName("Pane2");
//
//        scope1.addFormat(pane1);
//
//        scope2.addFormat(pane2);
//
//        assertTrue("FormatScope <" + scope1 + "> appears equal to " +
//                   "FormatScope <" + scope2 + "> when they are not equal",
//                   !(scope1.equals(scope2)));
//    }
    
//    /**
//     * Test hashCode() with two equal FormatScopes
//     */
//    public void testHashCodeEqual() {
//        FormatScope scope1 = new FormatScope();
//        FormatScope scope2 = new FormatScope();
//
//        assertEquals(scope1.hashCode(), scope2.hashCode());
//
//        CanvasLayout layout = new CanvasLayout();
//        Pane pane1 = new Pane(layout);
//        Pane pane2 = new Pane(layout);
//        pane1.setName("Pane1");
//        pane2.setName("Pane2");
//
//        scope1.addFormat(pane1);
//        scope1.addFormat(pane2);
//
//        scope2.addFormat(pane1);
//        scope2.addFormat(pane2);
//
//        assertEquals(scope1.hashCode(), scope2.hashCode());
//    }

//    /**
//     * Test hashCode() with two non-equal FormatScopes
//     */
//    public void testHashCodeNotEqualNamesAttribute() {
//
//        CanvasLayout layout = new CanvasLayout();
//        Pane pane = new Pane(layout);
//
//
//        ArrayList objects =
//                new ArrayList(UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT);
//        for(int i=0; i<UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT;i++) {
//            FormatScope scope = new FormatScope();
//            pane.setName(String.valueOf(Math.random()) + String.valueOf(i));
//            scope.addFormat(pane);
//            objects.add(scope);
//        }
//
//        assertTrue("Hashcode reliability has degraded to below 1/" +
//                   UnitTestConstants.HASHCODE_ACCEPTABILITY_CONSTANT,
//                   ObjectTestHelper.testHashCodesNonEqual(objects));
//
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
