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
package com.volantis.mcs.layouts;

import com.volantis.mcs.model.TestValidator;

public class TemporalFormatIteratorValidationTestCase
        extends FormatValidationTestAbstract {

    /**
     * Ensure that a temporal with missing required attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateTemporalRequired() throws Exception {

        TestValidator validator = new TestValidator();

        // FormatIteratorAttributes
        expectRootError(validator, "clockValues",
                LayoutMessages.CLOCK_VALUES_UNSPECIFIED);
        expectRootError(validator, "cells",
                LayoutMessages.CELLS_UNSPECIFIED);
        expectRootError(validator, "cellCount",
                LayoutMessages.CELL_COUNT_UNSPECIFIED);

        doRead(validator, "testValidateTemporalRequired.xml");
    }

    /**
     * Ensure that a temporal with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateTemporalInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatIteratorInvalidExpectations(validator);

        expectRootError(validator, "clockValues",
                LayoutMessages.CLOCK_VALUES_ILLEGAL, "not a clock value");
        expectRootError(validator, "cells",
                LayoutMessages.CELLS_ILLEGAL, "wonky"); // can be fixed or variable
        expectRootError(validator, "cellCount",
                LayoutMessages.CELL_COUNT_ILLEGAL, "-6"); // > 0

        doReadAndWrite(validator, "testValidateTemporalInvalid.xml");
    }

    /**
     * Ensure that a temporal with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateTemporalValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateTemporalValid.xml");
    }

}
