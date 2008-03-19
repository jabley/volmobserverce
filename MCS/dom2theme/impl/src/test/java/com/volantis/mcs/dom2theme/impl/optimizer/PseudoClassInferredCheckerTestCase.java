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

import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.StyleKeywords;

/**
 * Test cases for {@link PseudoClassInferredValueChecker}.
 */
public class PseudoClassInferredCheckerTestCase
        extends InferredValueCheckerTestAbstract {

    protected void setUp() throws Exception {
        super.setUp();

        checker = new PseudoClassInferredValueChecker(initialValueFinderMock);
        checker.prepare(parentValuesMock);
    }

    /**
     * Ensure that when checking the status for an individual property that the
     * pseudo class checker always returns required.
     */
    public void testStatusForUseByPropertyAlwaysRequired() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PropertyStatus status = checker.checkInferred(StatusUsage.INDIVIDUAL,
                inputValuesMock, detailsMock, StyleKeywords.NONE, true);
        assertEquals(PropertyStatus.REQUIRED, status);
    }

    /**
     * Ensure that when checking the status for a shorthand that the pseudo
     * class checker checks the initial values.
     */
    public void testStatusForUseByShorthand()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        doTestForCheckingInitialValue(StatusUsage.SHORTHAND,
                BorderWidthKeywords.THICK, 
                BorderWidthKeywords.THIN);
    }
}
