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

package com.volantis.schema;

import com.volantis.xml.schema.validator.SchemaValidator;
import org.xml.sax.SAXException;

/**
 * A test for an invalid document.
 *
 * <p>Will fail if either no error is produced, or the produced error does not
 * match the expected error.</p>
 */
public class InvalidDocument
        extends AbstractTestDocument {

    private final ValidationError expectedError;

    /**
     * Initialise.
     *
     * @param testClass       The test class from which the resource is
     *                        relative.
     * @param resource        A test class relative path to the resource.
     * @param expectedError   The expected error that will be produced when
     *                        validating this document.
     * @param schemaValidator The validator to use in the test.
     */
    public InvalidDocument(
            Class testClass, String resource, ValidationError expectedError,
            final SchemaValidator schemaValidator) {
        super(testClass, resource, schemaValidator);
        this.expectedError = expectedError;
    }

    protected void runTest() throws Throwable {
        try {
            super.runTest();
            fail("Did not detect any errors");
        } catch (SAXException e) {
            String message = e.getMessage();
            if (!expectedError.matches(message)) {
                throw new IllegalArgumentException("Expected error '" +
                        expectedError.getDescription() + "' but got '" +
                        message + "'");
            }
        }
    }
}
