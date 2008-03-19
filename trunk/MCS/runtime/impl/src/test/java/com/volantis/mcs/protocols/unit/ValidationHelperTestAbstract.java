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

package com.volantis.mcs.protocols.unit;

import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for {@link ValidationHelper} tests.
 */
public abstract class ValidationHelperTestAbstract
        extends TestCaseAbstract {

    /**
     * The validation helper to test.
     */
    protected ValidationHelper validationHelper;

    /**
     * Override to set the validation helper.
     */
    protected void setUp() throws Exception {
        super.setUp();

        validationHelper = createValidationHelper();
    }

    /**
     * Factory method to allow derived classes to construct the testable
     * object.
     *
     * @return The testable object.
     */
    protected abstract ValidationHelper createValidationHelper();

    /**
     * Perform a validation test.
     *
     * @param pattern  The input to the validation helper.
     * @param expected The expected output from the validation helper.
     */
    protected void doTest(String pattern, String expected) {
        String result;
        result = validationHelper.createTextInputFormat(pattern);
        assertEquals("Unexpected pattern transformation.", expected, result);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
