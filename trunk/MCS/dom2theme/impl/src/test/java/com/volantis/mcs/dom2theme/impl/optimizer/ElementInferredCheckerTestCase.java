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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test cases for {@link ElementInferredValueChecker}.
 */
public class ElementInferredCheckerTestCase
        extends InferredValueCheckerTestAbstract {

    protected void setUp() throws Exception {
        super.setUp();

        checker = new ElementInferredValueChecker(initialValueFinderMock);
        checker.prepare(parentValuesMock);
    }

    /**
     * Ensure that when checking the status for an individual property that the
     * element checker will check inherited first.
     */
    public void testStatusForUseByPropertyChecksInheritance()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        StyleProperty property = StylePropertyDetails.FONT_SIZE;

        detailsMock.expects.isInherited().returns(true).any();
        detailsMock.expects.getProperty().returns(property).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doTestForCheckingInheritedValue(StatusUsage.INDIVIDUAL, property,
                FontSizeKeywords.LARGE, FontSizeKeywords.MEDIUM);
    }

    private void doTestForCheckingInheritedValue(
            final StatusUsage statusUsage, StyleProperty property,
            final StyleKeyword parentValue, final StyleKeyword notParentValue) {

        assertNotEquals(parentValue, notParentValue);

        parentValuesMock.expects.getStyleValue(property)
                .returns(parentValue).any();

        PropertyStatus status;

        // When the supplied value matches the value from the parent then it is
        // INHERITED.
        status = checker.checkInferred(statusUsage, inputValuesMock,
                detailsMock, parentValue, true);
        assertEquals("Should be clearable", PropertyStatus.CLEARABLE, status);

        // When the supplied value does not match the value from the parent
        // then it is REQUIRED.
        status = checker.checkInferred(StatusUsage.INDIVIDUAL, inputValuesMock,
                detailsMock, notParentValue, true);
        assertEquals(PropertyStatus.REQUIRED, status);
    }

    /**
     * Ensure that when checking the status for an individual property that the
     * element checker will check inherited first.
     */
    public void testStatusForUseByPropertyChecksInitial()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        detailsMock.expects.isInherited().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        doTestForCheckingInitialValue(StatusUsage.INDIVIDUAL,
                BorderStyleKeywords.RIDGE,
                BorderStyleKeywords.SOLID);
    }

    /**
     * Ensure that when checking the status for a shorthand that the element
     * class checker checks both the inherited and initial values.
     */
    public void testStatusForUseByShorthand()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        StyleProperty property = StylePropertyDetails.FONT_SIZE;

        detailsMock.expects.isInherited().returns(true).any();
        detailsMock.expects.getProperty().returns(property).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // When the supplied value matches the value from the parent but does
        // not match the initial value then it is required for shorthand but
        // not for property.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.LARGE,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.LARGE,
                PropertyStatus.REQUIRED_FOR_SHORTHAND, true);

        // When the supplied value does not match the value from the parent
        // but does match the initial value then it is required for property
        // but not for shorthand.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.LARGE,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                PropertyStatus.REQUIRED_FOR_INDIVIDUAL, true);

        // When the supplied value matches both the value from the parent
        // and the initial value then it is clearable.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                PropertyStatus.CLEARABLE, true);

        // When the supplied value matches neither the value from the parent
        // or the initial value then it is required for both.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.LARGE,
                PropertyStatus.REQUIRED, true);

        // When the supplied value does not match the value from the parent,
        // does match the initial value but has been told not to check
        // initial values then it is required for both.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.LARGE,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                PropertyStatus.REQUIRED, false);

        // When the supplied value matches both the value from the parent
        // and the initial value but has been told not to check
        // initial values then it is required for shorthand but not property.
        doTestCheckInferred(StatusUsage.SHORTHAND, true, true, property,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                FontSizeKeywords.MEDIUM,
                PropertyStatus.REQUIRED_FOR_SHORTHAND, false);
    }
}
