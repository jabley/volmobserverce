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

public class FragmentValidationTestCase
        extends FormatValidationTestAbstract {

    /**
     * Ensure form fragments generate errors for all missing required attributes
     *
     * @throws Exception
     */
    public void testValidateFragmentRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameRequiredExpectations(validator);

        doRead(validator, "testValidateFragmentRequired.xml");
    }

    /**
     * Ensure form fragments generate errors for all invalid attributes.
     *
     * @throws Exception
     */
    public void testValidateFragmentInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameInvalidExpectations(validator);
        expectRootError(validator, "linkStyleClass",
                LayoutMessages.LINK_STYLE_CLASS_ILLEGAL, "1style");
        expectRootError(validator, "showPeerLinks",
                LayoutMessages.SHOW_PEER_LINKS_ILLEGAL, "ifyoulike");
        expectRootError(validator, "fragmentLinkOrder",
                LayoutMessages.FRAGMENT_LINK_ORDER_ILLEGAL, "me-first");

        doReadAndWrite(validator, "testValidateFragmentInvalid.xml");
    }

    /**
     * Ensure that a pane with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateFragmentValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateFragmentValid.xml");
    }

}
