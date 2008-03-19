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
import com.volantis.mcs.themes.StyleValuesMock;
import com.volantis.styling.properties.PropertyDetailsMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.InitialValueFinderMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link InferredValueChecker}.
 */
public abstract class InferredValueCheckerTestAbstract
        extends TestCaseAbstract {

    protected InitialValueFinderMock initialValueFinderMock;
    protected StyleValuesMock inputValuesMock;
    protected StyleValuesMock parentValuesMock;
    protected PropertyDetailsMock detailsMock;
    protected InferredValueChecker checker;

    protected void setUp() throws Exception {
        super.setUp();

        initialValueFinderMock = new InitialValueFinderMock(
                "initialValueFinderMock", expectations);

        inputValuesMock = new StyleValuesMock("inputValuesMock", expectations);

        parentValuesMock = new StyleValuesMock("parentValuesMock", expectations);

        detailsMock = new PropertyDetailsMock("detailsMock", expectations);

    }

    protected void doTestForCheckingInitialValue(
            final StatusUsage statusUsage, final StyleKeyword initialValue,
            final StyleKeyword notInitialValue) {

        assertNotEquals(initialValue, notInitialValue);

        // If the supplied value matches the initial value then should be
        // INITIAL.
        doTestCheckInferred(statusUsage, false, true, null, null,
                initialValue, initialValue, PropertyStatus.CLEARABLE, true);

        // If the supplied value does not match the initial value then should
        // be REQUIRED.
        doTestCheckInferred(statusUsage, false, true, null, null,
                initialValue, notInitialValue, PropertyStatus.REQUIRED, true);

        // If the checker is told not to check for initial value then it should
        // return REQUIRED.
        doTestCheckInferred(statusUsage, false, false, null, null,
                initialValue, initialValue, PropertyStatus.REQUIRED, false);
    }

    protected void doTestCheckInferred(
            final StatusUsage statusUsage,
            boolean willCheckParent, boolean willCheckInitial,
            StyleProperty property,
            final StyleKeyword parentValue,
            final StyleKeyword initialValue,
            final StyleKeyword value,
            final PropertyStatus expectedStatus,
            final boolean checkInitialValue) {

        if (willCheckParent) {
            parentValuesMock.expects.getStyleValue(property)
                    .returns(parentValue);
        }

        if (willCheckInitial & checkInitialValue) {
            initialValueFinderMock.expects
                    .getInitialValue(inputValuesMock, detailsMock)
                    .returns(initialValue);
        }

        PropertyStatus status = checker.checkInferred(statusUsage,
                inputValuesMock, detailsMock, value, checkInitialValue);
        assertEquals(expectedStatus, status);

        expectations.verify();
    }
}
