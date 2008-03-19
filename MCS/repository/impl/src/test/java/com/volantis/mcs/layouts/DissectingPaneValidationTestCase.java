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

public class DissectingPaneValidationTestCase
        extends FormatValidationTestAbstract {

    /**
     * Ensure that all missing required attributes for pane generate errors.
     *
     * @throws Exception
     */
    public void testValidateDissectingPaneRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameRequiredExpectations(validator);
        addDissectingPaneNoParentFragmentExpectations(validator, null);

        doRead(validator, "testValidateDissectingPaneRequired.xml");
    }

    /**
     * Ensure that all invalid checked attributes for dissecting pane generate
     * errors.
     *
     * @throws Exception
     */
    public void testValidateDissectingPaneInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addAllPaneInvalidExpectations(validator);

        expectRootError(validator, "nextLinkStyleClass",
                LayoutMessages.NEXT_LINK_STYLE_CLASS_ILLEGAL, "12345");
        expectRootError(validator, "previousLinkStyleClass",
                LayoutMessages.PREVIOUS_LINK_STYLE_CLASS_ILLEGAL, "23456");
        expectRootError(validator, "maxContentSize",
                LayoutMessages.MAXIMUM_CONTENT_SIZE_ILLEGAL, "-1");
        expectRootError(validator, "shardLinkOrder",
                LayoutMessages.SHARD_LINK_ORDER_ILLEGAL, "pseudo-randomish");

        addDissectingPaneNoParentFragmentExpectations(validator, "1format");

        doReadAndWrite(validator, "testValidateDissectingPaneInvalid.xml");
    }

    /**
     * Ensure that a dissecting pane with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateDissectingPaneValid() throws Exception {

        TestValidator validator = new TestValidator();

        addDissectingPaneNoParentFragmentExpectations(validator, "pane");

        doReadAndWrite(validator, "testValidateDissectingPaneValid.xml");
    }

    private void addDissectingPaneNoParentFragmentExpectations(
            TestValidator validator, final Object name) {
        expectRootError(validator, null,
                "dissecting-pane-must-be-in-fragment", new Object[] {name});
    }

}
