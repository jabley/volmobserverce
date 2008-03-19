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
 * $Header: /src/voyager/com/volantis/mcs/layouts/NDimensionalIndex.java,v 1.2 2002/11/25 15:23:48 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Oct-02    Chris W         VBM:2002111102 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import java.util.Arrays;

/*
 * NOTE: This class was in the com.volantis.mcs.protocols package and was 
 * cloned rather than moved in Accurev. For previous History please see
 * com.volantis.mcs.protocols.NDimensionalIndex.java
 */

/**
 * The index of an individual entry in an {@link NDimensionalContainer}. 
 * <p>
 * Currently this is used to contain the "tail" of a FormatReference in a
 * 'nice' format. In testPane.1.2.3 everything after the first '.' is the
 * "tail" i.e. "1.2.3", or more precisely an indices array of the values {1, 2,
 * 3}.
 *
 * @mock.generate
 */
public class NDimensionalIndex implements Cloneable {
    /**
     * The copyright statement
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2004. ";

    public static final NDimensionalIndex ZERO_DIMENSIONS = 
            new NDimensionalIndex(new int[0]);
                                
    /**
     * An array of ints storing the reference's indices (or "tail").
     */
    private int[] index;

    /**
     * Indicates how many of the indices were explicitly specified and
     * therefore if any were "defaulted" to zero. Must be less than or equal to
     * the number of indices.
     */
    private int specified;

    /**
     * Initializes the new instance using the given parameters, assuming that
     * all dimensions have been explicitly defined.
     *
     * @param index the indices array. May not be null
     */
    public NDimensionalIndex(int[] index) {
        this(index, (index == null) ? 0 : index.length);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param index     the indices array. May not be null
     * @param specified the number of indices that have been explicitly
     *                  defined
     */
    public NDimensionalIndex(int[] index, int specified) {
        if (index == null) {
            throw new IllegalArgumentException("index may not be null");
        } else if (specified < 0 || specified > index.length) {
            throw new IllegalArgumentException(
                    "failed assertion: 0 <= specified (" + specified +
                    ") <= index.length (" + index.length + ")");
        }

        this.index = index;
        this.specified = specified;
    }

    /**
     * Returns the individual indicies which make up the index. 
     * <p>
     * NOTE: This returns the index's underlying data so any modifications 
     * made to the data within this array will modify the reference.
     *
     * @return the indices for this index.
     */
    public int[] getIndicies() {
        return index;
    }

    /**
     * Returns the dimensions of the index.
     *
     * @return the number of dimensions for the reference
     */
    public int getDimensions() {
        return index.length;
    }

    /**
     * Returns the number of indices that were specified.
     *
     * @return the number of indices specified
     */
    public int getSpecified() {
        return specified;
    }

    /**
     * Permits the number of specified indices to be reset. The value must be
     * 0 &lt;= <code>specified</code> &lt;= {@link #getDimensions}.
     *
     * @param specified the new specified number of indices
     */
    public void setSpecified(int specified) {
        if ((specified < 0) || (specified > index.length)) {
            throw new IllegalArgumentException(
                    "specified (" + specified + ") is out of bounds (0.." +
                    index.length + ")");
        }

        this.specified = specified;
    }

    // javadoc inherited
    public Object clone() throws CloneNotSupportedException {
        NDimensionalIndex clone = (NDimensionalIndex)super.clone();

        clone.index = new int[index.length];
        System.arraycopy(index, 0, clone.index, 0, index.length);

        return clone;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        }

        if (this.getClass().equals(o.getClass())) {
            NDimensionalIndex index = (NDimensionalIndex)o;

            return (specified == index.specified) &&
                    Arrays.equals(this.index, index.index);
        }

        return false;
    }

    /**
     * Generate a hash code representing this object.
     *
     * @return The identity hash code for this object
     */
    public int hashCode() {
        int hashCode = 0;

        if (index.length > 0) {
            hashCode = index[0] + 1;

            for (int i = 1; i < index.length; i++) {
                hashCode *= 10;
                hashCode += index[i] + 1;
            }
        }

        hashCode = 29 * hashCode + specified;

        return hashCode;
    }

    // javadoc inherited
    public String toString() {
        StringBuffer toString = new StringBuffer("[Dimension =");
        toString.append(getDimensions()).append("; Specified = ").
                append(specified).append("; Index = ");

        for (int i = 0; i < index.length; i++) {
            toString.append(index[i]).append(".");
        }
        toString.append("]");

        return toString.toString();
    }
    
    /**
     * Permits a new <code>NDimensionalIndex</code> to be created that is
     * based on this one but to which an additional dimension is added.
     *
     * The new dimension's initial value is zero. The number of specified
     * indices is set to the number of dimensions on the newly created index.
     *
     * @return a new index based on this one but with an additional
     *      dimension added
     */
    public NDimensionalIndex addDimension() {

        int newIndicies[] = new int[index.length + 1];

        System.arraycopy(index, 0, newIndicies, 0, index.length);

        newIndicies[index.length] = 0;

        return new NDimensionalIndex(newIndicies);
    }

    /**
     * Increments this index passed to this method. Does NOT change the
     * dimensions of the index. If the specified index is 1.3 and the increment
     * value is 1, it will make it 1.4. Does nothing if the given index has
     * zero dimensions.
     *
     * <p><strong>NOTE:</strong> this does not change the NDimensionalIndex
     * passed in itself, but creates a new instance with the updated
     * values.</p>
     *
     * @param incValue the amount by which the last dimension should be
     *                 incremented
     * @return the new index
     */
    public NDimensionalIndex incrementCurrentFormatIndex(int incValue) {
        NDimensionalIndex newIndex = NDimensionalIndex.ZERO_DIMENSIONS;

        if (!equals(NDimensionalIndex.ZERO_DIMENSIONS)) {
            int[] oldIndices = getIndicies();
            int[] newIndicies = new int[oldIndices.length];

            System.arraycopy(oldIndices, 0,
                             newIndicies, 0, oldIndices.length);

            newIndicies[newIndicies.length - 1] += incValue;

            newIndex = new NDimensionalIndex(newIndicies);
        }

        return newIndex;
    }

    /**
     * Sets the last dimension of the specified index. Does NOT change the
     * dimensions of the index. Does nothing if the given index has zero
     * dimensions.
     *
     * <p><strong>NOTE:</strong> this does not change the NDimensionalIndex
     * passed in itself, but creates a new instance with the updated
     * values.</p>
     *
     * @param value the value to set the last dimension to. If the current
     *              index is 1.3 and the value is 1 then the index becomes 1.1
     * @return the new index
     */
    public NDimensionalIndex setCurrentFormatIndex(int value) {
        NDimensionalIndex newIndex = NDimensionalIndex.ZERO_DIMENSIONS;

        if (!equals(NDimensionalIndex.ZERO_DIMENSIONS)) {
            int[] oldIndices = getIndicies();
            int[] newIndicies = new int[oldIndices.length];

            System.arraycopy(oldIndices, 0,
                             newIndicies, 0, oldIndices.length);

            newIndicies[newIndicies.length - 1] = value;

            newIndex = new NDimensionalIndex(newIndicies);
        }

        return newIndex;
    }

    /**
     * Returns true if this NDimensionalIndex starts with the supplied index.
     * <p>
     * Consider the following example:
     * <br>
     * {1,2,3,4,5} - a <br>
     * {1,2,3} - b <br>
     * <br>
     * a.startsWith(b) == true <br>
     * b.startWith(a) == false <br>
     * </p>
     *
     * @param otherIndex the index to be compared with the starting indices of
     * this NDimensionalIndex
     *
     * @return true if this NDimensionalIndex starts with index; otherwise
     * false.
     */
    public boolean startsWith(NDimensionalIndex otherIndex) {

        boolean startsWith = true;

        // Does the supplied index have more dimensions than this
        // index?
        if (otherIndex.getDimensions() > this.getDimensions()) {
            startsWith = false;
        } else {
            // Comparison against zero dimensions always succeeds,
            // otherwise we need to compare each common dimension
            // index to determine whether this index starts with
            // the given index
            if (!otherIndex.equals(ZERO_DIMENSIONS)) {
                int otherIndexLength = otherIndex.getDimensions();
                int[] otherIndices = otherIndex.getIndicies();

                for (int i = 0; startsWith && (i < otherIndexLength); i++) {
                    startsWith = (otherIndices[i] == index[i]);
                }
            }
        }
        return startsWith;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Nov-04	5882/3	ianw	VBM:2004102008 Rework to make ObjectsCodeGenerator abstract

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 14-Jun-04	4704/4	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 ===========================================================================
*/
