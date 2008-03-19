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

public class PaneValidationTestCase extends FormatValidationTestAbstract {

    /**
     * Ensure that all missing required attributes for pane generate errors.
     *
     * @throws Exception
     */
    public void testValidatePaneRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameRequiredExpectations(validator);

        doRead(validator, "testValidatePaneRequired.xml");

    }

    /**
     * Ensure that all invalid checked attributes for pane generate errors.
     *
     * @throws Exception
     */
    public void testValidatePaneInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addNonDissectingPaneInvalidExpectations(validator);

        doReadAndWrite(validator, "testValidatePaneInvalid.xml");

    }

    /**
     * Ensure that a pane with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidatePaneValid() throws Exception {

        TestValidator validator = new TestValidator();
        
        doReadAndWrite(validator, "testValidatePaneValid.xml");

    }

    private void addNonDissectingPaneInvalidExpectations(
            TestValidator validator) {

        addAllPaneInvalidExpectations(validator);

        addAdditionalNonDissectablePaneAndGridInvalidExpectations(validator);

        // destination area attributes do not require validation
    }

}
