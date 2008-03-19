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

public class RegionValidationTestCase extends FormatValidationTestAbstract {

    // todo: test anonymous layout as well?

    /**
     * Ensure that a region with missing required attributes for generates errors.
     *
     * @throws Exception
     */
    public void testValidateRegionRequired() throws Exception {

        TestValidator validator = new TestValidator();
        addFormatNameRequiredExpectations(validator);

        doRead(validator, "testValidateRegionRequired.xml");
    }

    /**
     * Ensure that a Region with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateRegionInvalid() throws Exception {

        TestValidator validator = new TestValidator();
        addFormatNameInvalidExpectations(validator);

        doReadAndWrite(validator, "testValidateRegionInvalid.xml");
    }

    /**
     * Ensure that a Region with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateRegionValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateRegionValid.xml");
    }

}
