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

public class GridValidationTestCase extends FormatValidationTestAbstract {

    /**
     * Ensure that a grid with missing required attributes for generates errors.
     *
     * @throws Exception
     */
    public void testValidateGridRequired() throws Exception {

        TestValidator validator = new TestValidator();

        addGridDimensionRequiredExpectations(validator);

        doRead(validator, "testValidateGridRequired.xml");
    }

    private void addGridDimensionRequiredExpectations(TestValidator validator) {

        expectRootError(validator, "rows", LayoutMessages.ROWS_UNSPECIFIED);
        expectRootError(validator, "columns", LayoutMessages.COLUMNS_UNSPECIFIED);
    }

    /**
     * Ensure that a Grid with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateGridInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatNameInvalidExpectations(validator);

        addGridDimensionInvalidExpectations(validator);

        addAllPaneAndGridAndIteratorInvalidExpectations(validator);
        addAdditionalNonDissectablePaneAndGridInvalidExpectations(validator);
        addStyleClassInvalidExpectations(validator);
        addGridDirectionInvalidExpectations(validator);

        doReadAndWrite(validator, "testValidateGridInvalid.xml");
    }

    private void addGridDirectionInvalidExpectations(TestValidator validator) {
        expectRootError(validator, "directionality",
            LayoutMessages.DIRECTIONALITY_ILLEGAL, "Left-To-Right");
    }

    private void addGridDimensionInvalidExpectations(TestValidator validator) {

        expectRootError(validator, "rows", LayoutMessages.ROWS_ILLEGAL, "-10");
        expectRootError(validator, "columns", LayoutMessages.COLUMNS_ILLEGAL, "-20");
    }

    /**
     * Ensure that a Grid with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateGridValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateGridValid.xml");
    }


    /**
     * Ensure that a Grid Row and Column with invalid attributes generates
     * errors.
     *
     * @throws Exception
     */
    public void testValidateGridRowColumnInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        expectRootError(validator, "columns/0/width",
                LayoutMessages.WIDTH_ILLEGAL, "-1");
        expectRootError(validator, "columns/0/widthUnits",
                LayoutMessages.WIDTH_UNITS_ILLEGAL, "gallons");
        expectRootError(validator, "rows/0/height",
                LayoutMessages.HEIGHT_ILLEGAL, "-2");
        expectRootError(validator, "rows/0/styleClass",
                LayoutMessages.STYLE_CLASS_ILLEGAL, "!style");

        doReadAndWrite(validator, "testValidateGridRowColumnInvalid.xml");
    }

    /**
     * Ensure that a Grid Row and Column with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateGridRowColumnValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateGridRowColumnValid.xml");
    }

}
