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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/Attic/NDimensionalContainerTestCase.java,v 1.1.2.1 2002/11/21 11:36:08 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-2002  Chris W         VBM:2002110404 - Created
 * 13-Jan-2003  Chris W         VBM:2003011311 - Added tests for
 *                              getNumCellsInDimension() throughout the scenario
 *                              test methods i.e. testOneD, testTwoD, testFiveD
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * This class unit tests the NDimensionalContainer. It's easiest to test the
 * container by using scenarios rather than individually exercising the
 * methods.
 */
public class NDimensionalContainerTestCase extends TestCase
{
//    static {
//        org.apache.log4j.BasicConfigurator.configure();
//    }

    private NDimensionalContainer container;

    /**
     * Constructor for NDimensionalContainerTestCase.
     * @param name
     */
    public NDimensionalContainerTestCase(String name)
    {
        super(name);
    }

    /**
     * Sets up each test case 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp()
    {
        container = new NDimensionalContainer();
    }

    /**
     * Tears down each test case
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown()
    {
        container = null;
    }

    /**
     * Tests that elements can be set, retrieved and removed for a 1D container
     */
    public void testOneD()
    {
        // Set and get them one at a time.
        NDimensionalIndex index =
            new NDimensionalIndex(new int[] { 0 });
        container.set(index, "a");
        assertEquals("failed with 1d index ", "a", container.get(index));

        index = new NDimensionalIndex(new int[] { 1 });
        container.set(index, "b");
        assertEquals("failed with 1d index ", "b", container.get(index));

        index = new NDimensionalIndex(new int[] { 2 });
        container.set(index, "c");
        assertEquals("failed with 1d index ", "c", container.get(index));

        // Now retrieve all of them
        index = new NDimensionalIndex(new int[] { 0 });
        assertEquals("failed with 1d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1 });
        assertEquals("failed with 1d index ", "b", container.get(index));
        index = new NDimensionalIndex(new int[] { 2 });
        assertEquals("failed with 1d index ", "c", container.get(index));
        assertEquals("should be 3 elements ", 3, container.getNumCellsInDimension(index));

        // Retrieve something that doesn't exist
        index = new NDimensionalIndex(new int[] { 3 });
        assertNull("failed with 1d index ", container.get(index));

        // Amend an element that does exist and check all
        index = new NDimensionalIndex(new int[] { 1 });
        container.set(index, "d");
        assertEquals("failed with 1d index ", "d", container.get(index));
        index = new NDimensionalIndex(new int[] { 0 });
        assertEquals("failed with 1d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 2 });
        assertEquals("failed with 1d index ", "c", container.get(index));
        assertEquals("should be 3 elements ", 3, container.getNumCellsInDimension(index));

        // Remove an element and check all
        index = new NDimensionalIndex(new int[] { 1 });
        container.remove(index);
        assertNull("failed with 1d index ", container.get(index));
        index = new NDimensionalIndex(new int[] { 0 });
        assertEquals("failed with 1d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 2 });
        assertEquals("failed with 1d index ", "c", container.get(index));
        assertEquals("should be 3 elements ", 3, container.getNumCellsInDimension(index));
    }

    /**
     * Tests that elements can be set, retrieved and removed for a 2D container
     */
    public void testTwoD()
    {
        // Set and get them one at a time.
        NDimensionalIndex index =
            new NDimensionalIndex(new int[] { 1, 1 });
        container.set(index, "a");
        assertEquals("failed with 2d index ", "a", container.get(index));

        index = new NDimensionalIndex(new int[] { 0, 1 });
        container.set(index, "b");
        assertEquals("failed with 2d index ", "b", container.get(index));

        index = new NDimensionalIndex(new int[] { 1, 0 });
        container.set(index, "c");
        assertEquals("failed with 2d index ", "c", container.get(index));

        index = new NDimensionalIndex(new int[] { 0, 0 });
        container.set(index, "d");
        assertEquals("failed with 2d index ", "d", container.get(index));

        // Now retrieve all of them
        index = new NDimensionalIndex(new int[] { 1, 1 });
        assertEquals("failed with 2d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 1 });
        assertEquals("failed with 2d index ", "b", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 0 });
        assertEquals("failed with 2d index ", "c", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 0 });
        assertEquals("failed with 2d index ", "d", container.get(index));
        assertEquals("should be 2 elements in dim 1", 2, 
                     container.getNumCellsInDimension(index));
        assertEquals("should be 2 elements in dim 0", 2, 
                     container.getNumCellsInDimension(
                     new NDimensionalIndex(new int[] {0})));


        // Retrieve something that doesn't exist
        index = new NDimensionalIndex(new int[] { 1, 2 });
        assertNull("failed with 2d index ", container.get(index));
        index = new NDimensionalIndex(new int[] { 2, 1 });
        assertNull("failed with 2d index ", container.get(index));

        // Amend an element that does exist and check all
        index = new NDimensionalIndex(new int[] { 0, 1 });
        container.set(index, "e");
        assertEquals("failed with 2d index ", "e", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 1 });
        assertEquals("failed with 2d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 0 });
        assertEquals("failed with 2d index ", "c", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 0 });
        assertEquals("failed with 2d index ", "d", container.get(index));

        // Remove an element and check all
        index = new NDimensionalIndex(new int[] { 0, 1 });
        container.remove(index);
        assertNull("failed with 2d index ", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 1 });
        assertEquals("failed with 2d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 0 });
        assertEquals("failed with 2d index ", "c", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 0 });
        assertEquals("failed with 2d index ", "d", container.get(index));
        assertEquals("should be 1 element in row 0", 1, 
                     container.getNumCellsInDimension(index));
        assertEquals("should be 2 rows", 2, 
                     container.getNumCellsInDimension(
                     new NDimensionalIndex(new int[] {0})));        
    }

    /**
     * Tests that elements can be set, retrieved and removed for a 5D container
     */
    public void testFiveD()
    {
        // Set and get them one at a time.
        NDimensionalIndex index =
            new NDimensionalIndex(new int[] { 0, 0, 0, 0, 0 });
        container.set(index, "a");
        assertEquals("failed with 5d index ", "a", container.get(index));

        index = new NDimensionalIndex(new int[] { 1, 2, 3, 4, 5 });
        container.set(index, "b");
        assertEquals("failed with 5d index ", "b", container.get(index));

        index = new NDimensionalIndex(new int[] { 1, 2, 1, 0, 7 });
        container.set(index, "c");
        assertEquals("failed with 5d index ", "c", container.get(index));

        index = new NDimensionalIndex(new int[] { 10, 12, 1, 6, 23 });
        container.set(index, "d");
        assertEquals("failed with 5d index ", "d", container.get(index));

        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 23 });
        container.set(index, "e");
        assertEquals("failed with 5d index ", "e", container.get(index));

        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 9 });
        container.set(index, "f");
        assertEquals("failed with 5d index ", "f", container.get(index));

        // Now retrieve all of them
        index = new NDimensionalIndex(new int[] { 0, 0, 0, 0, 0 });
        assertEquals("failed with 5d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 2, 3, 4, 5 });
        assertEquals("failed with 5d index ", "b", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 2, 1, 0, 7 });
        assertEquals("failed with 5d index ", "c", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 6, 23 });
        assertEquals("failed with 5d index ", "d", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 23 });
        assertEquals("failed with 5d index ", "e", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 9 });
        assertEquals("failed with 5d index ", "f", container.get(index));
        assertEquals("should be 23 elements in dim 5", 24, 
                     container.getNumCellsInDimension(index));
        assertEquals("wrong no. elements in 3rd dimension ", 4, 
                     container.getNumCellsInDimension(
                     new NDimensionalIndex(new int[] {1, 2, 3})));        

        // Retrieve something that doesn't exist
        index = new NDimensionalIndex(new int[] { 3, 4, 2, 1, 2 });
        assertNull("failed with 5d index ", container.get(index));

        // Amend an element that does exist and check all
        index = new NDimensionalIndex(new int[] { 1, 2, 1, 0, 7 });
        container.set(index, "g");
        assertEquals("failed with 5d index ", "g", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 0, 0, 0, 0 });
        assertEquals("failed with 5d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 2, 3, 4, 5 });
        assertEquals("failed with 5d index ", "b", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 6, 23 });
        assertEquals("failed with 5d index ", "d", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 23 });
        assertEquals("failed with 5d index ", "e", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 9 });
        assertEquals("failed with 5d index ", "f", container.get(index));
        assertEquals("should be 23 elements in dim 5", 24, 
                     container.getNumCellsInDimension(index));
        assertEquals("wrong no. elements in 3rd dimension ", 4, 
                     container.getNumCellsInDimension(
                     new NDimensionalIndex(new int[] {1, 2, 3})));        

        // Remove an element and check all
        index = new NDimensionalIndex(new int[] { 1, 2, 1, 0, 7 });
        container.remove(index);
        assertNull("failed with 5d index ", container.get(index));
        index = new NDimensionalIndex(new int[] { 0, 0, 0, 0, 0 });
        assertEquals("failed with 5d index ", "a", container.get(index));
        index = new NDimensionalIndex(new int[] { 1, 2, 3, 4, 5 });
        assertEquals("failed with 5d index ", "b", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 6, 23 });
        assertEquals("failed with 5d index ", "d", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 23 });
        assertEquals("failed with 5d index ", "e", container.get(index));
        index = new NDimensionalIndex(new int[] { 10, 12, 1, 3, 9 });
        assertEquals("failed with 5d index ", "f", container.get(index));
        assertEquals("should be 23 elements in dim 5", 24, 
                     container.getNumCellsInDimension(index));
        assertEquals("wrong no. elements in 3rd dimension ", 4, 
                     container.getNumCellsInDimension(
                     new NDimensionalIndex(new int[] {1, 2, 3})));                             
    }


    public void testCompleteIteratorWhenContainerIsEmpty() {
        Iterator iter = container.iterator();
        assertFalse(iter.hasNext());
    }


    public void testCompleteIteratorWith0DContainer() {
        container.set(NDimensionalIndex.ZERO_DIMENSIONS, "a");
        Iterator iter = container.iterator();
        assertEquals("Should return a", "a", (String)iter.next());
        try {
            iter.next();
            fail("Should throw NoSuchElementException");
        } catch (NoSuchElementException e) {

        }
    }

    public void testCompleteIteratorWith1DContainer() {

        NDimensionalIndex index =
            new NDimensionalIndex(new int[] { 0 });
        container.set(index, "a");
        index = new NDimensionalIndex(new int[] { 1 });
        container.set(index, "b");
        index = new NDimensionalIndex(new int[] { 2 });
        container.set(index, "c");
        index = new NDimensionalIndex(new int[] { 3 });
        container.set(index, "d");
        index = new NDimensionalIndex(new int[] { 4 });
        container.set(index, "e");
        index = new NDimensionalIndex(new int[] { 5 });
        container.set(index, "f");
        index = new NDimensionalIndex(new int[] { 6 });
        container.set(index, "g");
        index = new NDimensionalIndex(new int[] { 7 });
        container.set(index, "h");

        Iterator iter = container.iterator();
        assertEquals("Should return a", "a", (String)iter.next());
        assertEquals("Should return b", "b", (String)iter.next());
        assertEquals("Should return c", "c", (String)iter.next());
        assertEquals("Should return d", "d", (String)iter.next());
        assertEquals("Should return e", "e", (String)iter.next());
        assertEquals("Should return f", "f", (String)iter.next());
        assertEquals("Should return g", "g", (String)iter.next());
        assertEquals("Should return h", "h", (String)iter.next());

        try {
            iter.next();
            fail("Should throw NoSuchElementException");
        } catch (NoSuchElementException e) {

        }
    }

    public void testCompleteIteratorWith2DContainer() {

        NDimensionalIndex index =
            new NDimensionalIndex(new int[] { 1, 1 });
        container.set(index, "a");
        index = new NDimensionalIndex(new int[] { 0, 2 });
        container.set(index, "b");
        index = new NDimensionalIndex(new int[] { 3, 0 });
        container.set(index, "c");
        index = new NDimensionalIndex(new int[] { 1, 3 });
        container.set(index, "d");


        Iterator iter = container.iterator();
        // Container looks like
        // [[null, null, b], [null, a, null, d], null, [c]]

        assertEquals( "iterator returned wrong object", "b",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "a",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "d",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "c",
            (String) iter.next());

        try {
            iter.next();
            fail("Should throw NoSuchElementException");
        } catch (NoSuchElementException e) {

        }
    }

    public void testCompleteIteratorWith3DContainer() {

        NDimensionalIndex index = new NDimensionalIndex(new int[] { 0, 0, 0 });
        container.set(index, "a");
        index = new NDimensionalIndex(new int[] { 0, 1, 0 });
        container.set(index, "b");
        index = new NDimensionalIndex(new int[] { 1, 0, 2 });
        container.set(index, "c");
        index = new NDimensionalIndex(new int[] { 0, 0, 3 });
        container.set(index, "d");

        //[[[a, null, null, d], [b]], [[null, null, c]]]

        Iterator iter = container.iterator();

        assertEquals( "iterator returned wrong object", "a",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "d",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "b",
            (String) iter.next());
        assertEquals( "iterator returned wrong object", "c",
            (String) iter.next());
        try {
            iter.next();
            fail("Should throw NoSuchElementException");
        } catch (NoSuchElementException e) {

        }
    }

    public void testIteratorHasNext() {

        NDimensionalIndex index = new NDimensionalIndex(new int[] { 0, 0, 0 });
        container.set(index, "a");
        index = new NDimensionalIndex(new int[] { 0, 1, 0 });
        container.set(index, "b");
        index = new NDimensionalIndex(new int[] { 1, 0, 2 });
        container.set(index, "c");
        index = new NDimensionalIndex(new int[] { 0, 0, 3 });
        container.set(index, "d");

        //[[[a, null, null, d], [b]], [[null, null, c]]]
        Iterator iter = container.iterator();

        assertTrue("Should have item(s) to iterate", iter.hasNext());
        iter.next();

        assertTrue("Should have item(s) to iterate", iter.hasNext());
        iter.next();

        assertTrue("Should have item(s) to iterate", iter.hasNext());
        iter.next();

        assertTrue("Should have item(s) to iterate", iter.hasNext());
        iter.next();

        assertFalse("Should not have items to iterate", iter.hasNext());
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
