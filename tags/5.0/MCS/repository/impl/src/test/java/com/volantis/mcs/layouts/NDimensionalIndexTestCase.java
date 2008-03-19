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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Nov-02    Sumit           VBM:2002111102 - Created to test the
 *                              NDimensionalIndex class
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import junit.framework.TestCase;

/**
 * Tests for {@link NDimensionalIndex}.
 */
public class NDimensionalIndexTestCase
        extends TestCase {
    
    private NDimensionalIndex index;

    public void setUp() {
        int index[] = {1,2,3,4};
        this.index = new NDimensionalIndex(index);
    }
    
//    /**
//     * Test if two FormatInstanceReferences are equal based on their indices
//     */
//
//    public void testEquals() {
//        int index[] = {1,2,3,4};
//        NDimensionalIndex newFir = new NDimensionalIndex(index);
//        assertTrue(this.index.equals(newFir));
//    }
//    /**
//     * Test if two equal FormatInstanceReferences have the same hashcodes
//     */
//
//    public void testHashCode() {
//        int index[] = {1,2,3,4};
//        NDimensionalIndex newFir = new NDimensionalIndex(index);
//        assertTrue(this.index.hashCode()==newFir.hashCode());
//    }
    /**
     * Test if addition of a dimension to a NDimensionalIndex
     */

    public void testaddFormatInstanceReferenceDimension() {
        int index[] = {1,2,3,4,0};
        NDimensionalIndex newFir = new NDimensionalIndex(index);
        assertTrue(this.index.addDimension().equals(newFir));
    }

    public void testStartsWithWhenTrue() {
        int index[] = {1,2,3,4,5};
        NDimensionalIndex fiveDimensionalIndex = new NDimensionalIndex(index);

        int index2[] = {1,2,3,4};
        NDimensionalIndex fourDimensionalIndex = new NDimensionalIndex(index2);

        assertTrue(fiveDimensionalIndex.startsWith(fourDimensionalIndex));
    }

    public void testStartsWithWhenFalse() {
        int index[] = {1,2,3,4,5};
        NDimensionalIndex fiveDimensionalIndex = new NDimensionalIndex(index);

        int index2[] = {1,2,3,4};
        NDimensionalIndex fourDimensionalIndex = new NDimensionalIndex(index2);

        assertFalse(fourDimensionalIndex.startsWith(fiveDimensionalIndex));
    }

    public void testStartsWithWhenFalseWhenIndexLengthsAreEqual() {
        int index[] = {1};
        NDimensionalIndex oneDimensionalIndex = new NDimensionalIndex(index);

        assertTrue(oneDimensionalIndex.startsWith(
                NDimensionalIndex.ZERO_DIMENSIONS));
    }

    public void testStartsWithWhenTrueWhenOtherIsNDimensionalIndex() {
        int index[] = {0};
        NDimensionalIndex oneDimensionalIndex = new NDimensionalIndex(index);

        assertTrue(oneDimensionalIndex.startsWith(
                NDimensionalIndex.ZERO_DIMENSIONS));
    }

    public void testStartsWithWhenComparingTwoNDimensionalIndexes() {
        assertTrue(NDimensionalIndex.ZERO_DIMENSIONS.
                   startsWith(NDimensionalIndex.ZERO_DIMENSIONS));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/2	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
