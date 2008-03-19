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

import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.TestValidator;

public class FormFragmentValidationTestCase
        extends FormatValidationTestAbstract {

    /**
     * Ensure form fragments generate errors for all missing required attributes
     *
     * @throws Exception
     */
    public void testValidateFormFragmentRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameRequiredExpectations(validator);

        addFormFragmentNotWithinFormExpectation(validator, null);

        doRead(validator, "testValidateFormFragmentRequired.xml");
    }

    /**
     * Ensure form fragments generate errors for all invalid attributes.
     *
     * @throws Exception
     */
    public void testValidateFormFragmentInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameInvalidExpectations(validator);
        expectRootError(validator, "allowReset",
                LayoutMessages.ALLOW_RESET_ILLEGAL, "noway");
        expectRootError(validator, "nextLinkStyleClass",
                LayoutMessages.NEXT_LINK_STYLE_CLASS_ILLEGAL, "1nextstyle");
        expectRootError(validator, "nextLinkPosition",
                LayoutMessages.NEXT_LINK_POSITION_ILLEGAL, "inthemiddle");
        expectRootError(validator, "previousLinkStyleClass",
                LayoutMessages.PREVIOUS_LINK_STYLE_CLASS_ILLEGAL, "1previousstyle");
        expectRootError(validator, "previousLinkPosition",
                LayoutMessages.PREVIOUS_LINK_POSITION_ILLEGAL, "attheend");

        addFormFragmentNotWithinFormExpectation(validator, "1format");

        doReadAndWrite(validator, "testValidateFormFragmentInvalid.xml");
    }

    /**
     * Ensure that a pane with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateFormFragmentValid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormFragmentNotWithinFormExpectation(validator, "format");

        doReadAndWrite(validator, "testValidateFormFragmentValid.xml");
    }

    private void addFormFragmentNotWithinFormExpectation(
            TestValidator validator, String name) {
        expectRootError(validator, null, "form-fragment-must-be-in-form",
                new Object[]{name});
    }

}
