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

public class SpatialFormatIteratorValidationTestCase
        extends FormatValidationTestAbstract {

    /**
     * Ensure that a spatial with missing required attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateSpatialRequired() throws Exception {

        TestValidator validator = new TestValidator();

        // FormatIteratorAttributes
        expectRootError(validator, "rows",
                LayoutMessages.ROWS_UNSPECIFIED);
        expectRootError(validator, "rowCount",
                LayoutMessages.ROW_COUNT_UNSPECIFIED);
        expectRootError(validator, "columns",
                LayoutMessages.COLUMNS_UNSPECIFIED);
        expectRootError(validator, "columnCount",
                LayoutMessages.COLUMN_COUNT_UNSPECIFIED);

        doRead(validator, "testValidateSpatialRequired.xml");
    }

    /**
     * Ensure that a spatial with invalid attributes generates errors.
     *
     * @throws Exception
     */
    public void testValidateSpatialInvalid() throws Exception {

        TestValidator validator = new TestValidator();

        addFormatIteratorInvalidExpectations(validator);
        addOptimizationLevelInvalidExpectations(validator);
        addStyleClassInvalidExpectations(validator);

        expectRootError(validator, "indexingDirection",
                LayoutMessages.INDEXING_DIRECTION_ILLEGAL, "over-the-top");
        expectRootError(validator, "rows",
                LayoutMessages.ROWS_ILLEGAL, "random");
        expectRootError(validator, "rowCount",
                LayoutMessages.ROW_COUNT_ILLEGAL, "-6");
        expectRootError(validator, "rowStyleClasses",
                LayoutMessages.ROW_STYLE_CLASSES_ILLEGAL, "a 1 and a 2");
        expectRootError(validator, "columns",
                LayoutMessages.COLUMNS_ILLEGAL, "wonky");
        expectRootError(validator, "columnCount",
                LayoutMessages.COLUMN_COUNT_ILLEGAL, "-7");
        expectRootError(validator, "columnStyleClasses",
                LayoutMessages.COLUMN_STYLE_CLASSES_ILLEGAL, "a 2 and a 3");
        expectRootError(validator, "alignContent",
                LayoutMessages.ALIGN_CONTENT_ILLEGAL, "maybe");
        expectRootError(validator, "directionality",
                LayoutMessages.DIRECTIONALITY_ILLEGAL, "l 2r");


        doReadAndWrite(validator, "testValidateSpatialInvalid.xml");
    }

    /**
     * Ensure that a spatial with all normal values is valid.
     *
     * @throws Exception
     */
    public void testValidateSpatialValid() throws Exception {

        TestValidator validator = new TestValidator();

        doReadAndWrite(validator, "testValidateSpatialValid.xml");
    }

}
