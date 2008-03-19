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

package com.volantis.mcs.protocols.forms.validation;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link TextInputFormatParser}.
 */
public class TextInputFormatParserTestCase
        extends TestCaseAbstract {

    private void checkValidation(
            TextInputFormatParser parser, String format,
            boolean expectedEmptyOk) {

        TextInputFormat inputFormat = parser.parseFormat("field", format);

        assertEquals(format, inputFormat.getFormat());
        assertEquals(expectedEmptyOk, inputFormat.isEmptyOk());
    }

    /**
     * Ensure that if required the parser will always set empty ok.
     */
    public void testForceEmptyOk() throws Exception {

        TextInputFormatParser parser = new TextInputFormatParser(true);
        checkValidation(parser, "M:M", true);
        checkValidation(parser, "m:M", true);
    }

    /**
     * Ensure that the parser will set empty ok correctly.
     */
    public void testEmptyOk() throws Exception {

        TextInputFormatParser parser = new TextInputFormatParser(false);
        checkValidation(parser, "M:M", false);
        checkValidation(parser, "m:M", true);
    }
}
