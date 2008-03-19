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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMEValidationTestAbstract;

/**
 * Some integration level tests for the schema validation.
 */
public class XHTML2TableValidationTestCase
        extends XDIMEValidationTestAbstract {

    public void testTableWithNoRowsFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-no-rows-fails.xml",
                "validation-error-missing-content", null);
    }

    public void testTableWithMultipleCaptionsFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-multiple-captions-fails.xml",
                "validation-error-invalid-content", null);
    }

    public void testTableWithCaptionAfterRowFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-caption-after-row-fails.xml",
                "validation-error-invalid-content", null);
    }

    public void testTableWithPCDATAFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-pcdata-fails.xml",
                "validation-error-invalid-content", null);
    }

    public void testTableWithRowsButNotCaptionOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/table-with-rows-but-no-caption-ok.xml");
    }

    public void testTableWithRowsAndCaptionOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/table-with-rows-and-caption-ok.xml");
    }

    public void testTableWithSectionsOk() throws Exception {
        checkValidationFromFile(
                "validation-xml/table-with-sections-ok.xml");
    }

    public void testTableWithHeadButNoBodyFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-head-but-no-body-fails.xml",
                "validation-error-missing-content", null);
    }

    public void testTableWithFootButNoBodyFails() throws Exception {
        checkValidationFailsFromFile(
                "validation-xml/table-with-foot-but-no-body-fails.xml",
                "validation-error-missing-content", null);
    }
}
