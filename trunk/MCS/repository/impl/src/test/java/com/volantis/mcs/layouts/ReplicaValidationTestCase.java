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

public class ReplicaValidationTestCase extends FormatValidationTestAbstract {

    /**
     * Ensure that a replica with missing required attributes for generates errors.
     *
     * @throws Exception
     */
    public void testValidateReplicaRequired() throws Exception {

        TestValidator validator = new TestValidator();

        expectRootError(validator, "sourceFormatName",
                LayoutMessages.SOURCE_FORMAT_NAME_UNSPECIFIED);
        expectRootError(validator, "sourceFormatType",
                LayoutMessages.SOURCE_FORMAT_TYPE_UNSPECIFIED);

        addReplicaNotInFragmentExpectations(validator, null);

        doRead(validator, "testValidateReplicaRequired.xml");
    }

    /**
     * Ensure that a Replica with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateReplicaInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameInvalidExpectations(validator);

        expectRootError(validator, "sourceFormatName",
                LayoutMessages.SOURCE_FORMAT_NAME_ILLEGAL, "!name");
        expectRootError(validator, "sourceFormatType",
                LayoutMessages.SOURCE_FORMAT_TYPE_ILLEGAL, "knobbly");

        addReplicaNotInFragmentExpectations(validator, "1format");

        doReadAndWrite(validator, "testValidateReplicaInvalid.xml");
    }

    /**
     * Ensure that a Replica with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateReplicaValid() throws Exception {

        TestValidator validator = new TestValidator();
        addReplicaNotInFragmentExpectations(validator, "format");

        doReadAndWrite(validator, "testValidateReplicaValid.xml");
    }

    private void addReplicaNotInFragmentExpectations(TestValidator validator,
            String name) {
        expectRootError(validator, null, "replica-must-be-in-fragment",
                new Object[]{name});
    }

}
