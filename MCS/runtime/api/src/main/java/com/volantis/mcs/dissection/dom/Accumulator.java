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

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;

/**
 * Used to accumulate the cost of sections of a document.
 * <p>
 * It maintains a total that is incremented by the various methods.
 * <p>
 * It also keep tracks of usages of the shared content in order to determine
 * whether or not to add the cost of the shared content as well as the
 * reference.
 * <p>
 * In some situations when adding shared content it is not possible to calculate
 * the total due to a lack of information. In this case the total is variable
 * and it has the value of {@link #VARIABLE}.
 * <p>
 * Adding anything to a variable total does not change the fact that it is
 * variable so it is unnecessary to continue the calculations and hence the
 * {@link #isCalculationFinished} method will return true. The following
 * code shows an example of how to write a calculation that consists of a number
 * of steps, some of which involve (or may involve) adding shared content.
 * <pre>
 *    void calculate (Accumulator accumulator) {
 *      accumulator.addShared(3,2,20);
 *      if (accumulator.isCalculationFinished()) {
 *        return;
 *      }
 *
 *      accumulator.add (20);
 *
 *      accumulator.addShared (9, 2, 24);
 *      if (accumulator.isCalculationFinished()) {
 *        return;
 *      }
 *
 *      accumulator.add (99);
 *    }
 * </pre>
 */
public interface Accumulator
    extends AccumulatorConstants {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Adds the cost of some content to the total.
     * @param cost The cost to add to the total.
     * @return The current total which will be {@link #VARIABLE} if the
     * total could not be calculated.
     * @throws DissectionException If there was a problem updating the total.
     */
    public int add(int cost)
        throws DissectionException;

    /**
     * Adds the cost of some shared content to the total.
     * <p>
     * If the cost of the shared content has already been taken into account
     * in the total then this just adds the reference cost, otherwise it adds
     * both the reference cost and the content cost.
     * @param sharedContentIndex The index that identifies the shared content.
     * @param referenceCost The cost of a reference to the shared content.
     * @param contentCost The cost of the shared content.
     * @return The current total which will be {@link #VARIABLE} if the
     * total could not be calculated.
     * @throws DissectionException If there was a problem updating the total.
     */
    public int addShared(int sharedContentIndex,
                         int referenceCost, int contentCost)
        throws DissectionException;

    /**
     * Get the current total.
     * @return The current total which will be {@link #VARIABLE} if the
     * total could not be calculated.
     */
    public int getTotal();

    /**
     * Checks to see whether there is any point in continuing with the
     * calculation that is being performed with the accumulator.
     * <p>
     * An accumulator can get into a state that means that the add.. methods
     * no longer have any effect. Once it is in this state there is no point
     * in continuing with the calculation and so this method will return true.
     */
    public boolean isCalculationFinished ();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
