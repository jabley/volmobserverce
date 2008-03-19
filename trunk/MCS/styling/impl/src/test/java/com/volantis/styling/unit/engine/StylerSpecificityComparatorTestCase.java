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

import com.volantis.styling.compiler.SpecificityMock;
import com.volantis.mcs.themes.Priority;
import com.volantis.styling.impl.engine.StylerSpecificityComparator;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.mcs.themes.Priority;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Test cases for {@link StylerSpecificityComparator}.
 */
public class StylerSpecificityComparatorTestCase
        extends ComparatorTestAbstract {

    protected StylerMock normalPriorityHighSpecificityMock;
    protected StylerMock normalPriorityLowSpecificityMock;
    protected StylerMock importantPriorityHighSpecificityMock;
    protected StylerMock importantPriorityLowSpecificityMock;
    protected SpecificityMock highSpecificityMock;
    protected SpecificityMock lowSpecificityMock;
    private static final int GREATER_THAN = 1;
    private static final int LESS_THAN = -1;

    // Javadoc inherited.
    public Comparator createComparator() {
        return new StylerSpecificityComparator();
    }

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        highSpecificityMock = new SpecificityMock(
                "highSpecificityMock", expectations);
        lowSpecificityMock = new SpecificityMock(
                "lowSpecificityMock", expectations);

        normalPriorityHighSpecificityMock = createStylerMock(
                "normalPriorityHighSpecificityMock",
                highSpecificityMock, Priority.NORMAL);

        normalPriorityLowSpecificityMock = createStylerMock(
                "normalPriorityLowSpecificityMock",
                lowSpecificityMock, Priority.NORMAL);

        importantPriorityHighSpecificityMock = createStylerMock(
                "importantPriorityHighSpecificityMock",
                highSpecificityMock, Priority.IMPORTANT);

        importantPriorityLowSpecificityMock = createStylerMock(
                "importantPriorityLowSpecificityMock",
                lowSpecificityMock, Priority.IMPORTANT);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        highSpecificityMock.expects.compareTo(lowSpecificityMock)
                .returns(GREATER_THAN).any();
        lowSpecificityMock.expects.compareTo(highSpecificityMock)
                .returns(LESS_THAN).any();
    }

    private StylerMock createStylerMock(
            final String identifier, final SpecificityMock specificityMock,
            final Priority priority) {

        StylerMock stylerMock = new StylerMock(
                identifier, expectations);
        stylerMock.expects.getSpecificity()
                .returns(specificityMock).any();
        stylerMock.expects.getPriority()
                .returns(priority).any();

        return stylerMock;
    }

    /**
     * Test that the comparator works when priorities are equal.
     */
    public void testComparatorPrioritiesEqual() {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Comparator comparator = createComparator();
        int result = comparator.compare(normalPriorityHighSpecificityMock,
                                        normalPriorityLowSpecificityMock);
        assertEquals("Comparator result", GREATER_THAN, result);
    }

    /**
     * Test that the comparator works when priorities are different.
     */
    public void testComparatorPrioritiesDiffer() {

        // Calculate the expected result.
        int expectedResult = Priority.IMPORTANT.compareTo(Priority.NORMAL);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        Comparator comparator = createComparator();
        int result = comparator.compare(importantPriorityLowSpecificityMock, 
                normalPriorityLowSpecificityMock);
        assertEquals("Comparator result", expectedResult, result);
    }

    /**
     * Test that the ordering works properly.
     */
    public void testOrdering() {
        List inputList = Arrays.asList(new Styler[]{
            importantPriorityHighSpecificityMock,
            importantPriorityLowSpecificityMock,
            normalPriorityHighSpecificityMock,
            normalPriorityLowSpecificityMock,
        });

        List expectedList = Arrays.asList(new Styler[] {
            normalPriorityLowSpecificityMock,
            normalPriorityHighSpecificityMock,
            importantPriorityLowSpecificityMock,
            importantPriorityHighSpecificityMock,
        });

        doTestComparatorOrder(createComparator(), inputList, expectedList);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
