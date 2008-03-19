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

public class FormValidationTestCase extends FormatValidationTestAbstract {

    /**
     * Ensure that a form with missing required attributes for generates errors.
     *
     * @throws Exception
     */
    public void testValidateFormRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameRequiredExpectations(validator);

        doRead(validator, "testValidateFormRequired.xml");
    }

    /**
     * Ensure that a form with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateFormInvalid() throws Exception {

        TestValidator validator = new TestValidator();
        addFormatNameInvalidExpectations(validator);

        doReadAndWrite(validator, "testValidateFormInvalid.xml");
    }

    /**
     * Ensure that a form with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateFormValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateFormValid.xml");
    }


}
