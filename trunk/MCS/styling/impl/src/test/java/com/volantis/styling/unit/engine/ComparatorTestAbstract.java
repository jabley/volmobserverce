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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.unit.engine;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Test cases for {@link Comparator}.
 */
public abstract class ComparatorTestAbstract
        extends TestCaseAbstract {

    /**
     * Test that when the comparator is used to order a list that it results in
     * a list of the correct order.
     *
     * @param comparator The comparator to test.
     * @param inputList The input list.
     * @param expectedList The expected output list.
     */
    public void doTestComparatorOrder(
            final Comparator comparator, final List inputList,
            final List expectedList) {

        List actualList = new ArrayList(inputList);
        Collections.sort(actualList, comparator);

        assertEquals("Sorted list", expectedList, actualList);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
