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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.string;

import com.volantis.mcs.dissection.DissectionException;

/**
 * The purpose of this interface is to allow a dissector to manipulate a
 * string of characters without having to be aware of the underlying
 * implementation but still being able to take into account the cost of the
 * underlying representation of those characters.
 * <p>
 * This interface uses an int to represent a character rather than a Java
 * character as a single Unicode character may be represented as a pair
 * (surrogates) of Java characters. If we used Java characters this would
 * mean that the dissector would either have to have special code for handling
 * surrogates which would greatly complicate the code or we would run the risk
 * of splitting a character. Using an int eliminates all these problems.
 * <p>
 * The underlying implementation could be either plain Java characters,
 * encoded XML text containing character references, bytes or something else.
 * Therefore those methods that relate to underlying implementation count using
 * an abstract implementation unit rather than a specific one such as byte.
 * <h3>Break Points</h3>
 * <p>
 * A break point is a location between two adjacent characters at which the
 * string could be broken. A break point is represented as a zero base integer.
 * The first break point is immediately before the first character and has a
 * value of 0. The break point before character at index <code>i</code> is
 * represented by <code>i</code>, the break point after character at index
 * <code>i</code> is <code>i + 1</code>. The last break point is immediately
 * after the last character and has a value equal to the length of the string.
 * <p>
 * At a minimum there are always two break points in a string, the first and
 * the last.
 * <h4>WML</h4>
 * <p>
 * In WML it is not valid to break in the middle of a variable reference within
 * the body of an element.
 * <h4>WBXML</h4>
 * <p>
 * In WBXML it is not valid to break in the middle of an extension code (which
 * is used in WMLC for variable references). Due to an explicit design decision
 * it is not possible (at the moment) to break in the middle of a string
 * reference.
 */
public interface DissectableString {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    // =========================================================================
    //   Character methods
    // =========================================================================

    /**
     * Get the length in characters of this string.
     */
    public int getLength()
        throws DissectionException;

    /**
     * Get the character at the specified index.
     */
    public int charAt(int index)
        throws DissectionException;

    // =========================================================================
    //   Break point methods
    // =========================================================================

    /**
     * Get the next break point after the specified break point.
     */
    public int getNextBreakPoint(int breakPoint)
        throws DissectionException;

    /**
     * Get the previous break point before the specified break point.
     */
    public int getPreviousBreakPoint(int breakPoint)
        throws DissectionException;

    // =========================================================================
    //   Underlying implementation methods
    // =========================================================================

    /**
     * Get the cost of this string in implementation units.
     */
    public int getCost()
        throws DissectionException;

    /**
     * Check whether the cost of this string is context dependent, i.e. it
     * contains shared references.
     */
    public boolean isCostContextDependent()
        throws DissectionException;

    /**
     * Get the cost of the range of characters in implementation units.
     * <p>
     * The start index is inclusive and the end index is exclusive.
     */
    public int getRangeCost(int startIndex, int endIndex)
        throws DissectionException;

    /**
     * Get the index of the character such that the cost of the range from the
     * specified start index to the index is less than or equal to the specified
     * cost. e.g. the assertion in the following code must never fail.
     * <pre>
     *   int index = getCharacterIndex (startIndex, cost);
     *   int s = getRangeCost (startIndex, index);
     *   assert (s &lt;= cost);
     * </pre>
     * <p>
     * The start index is inclusive and the returned index is exclusive.
     */
    public int getCharacterIndex(int startIndex, int cost)
        throws DissectionException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
