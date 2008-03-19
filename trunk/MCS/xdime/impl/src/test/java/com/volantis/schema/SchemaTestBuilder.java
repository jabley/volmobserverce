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

import com.volantis.xml.schema.Schemata;
import com.volantis.xml.schema.validator.SchemaValidator;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Builds schema tests.
 */
public class SchemaTestBuilder {

    private final TestSuite suite;
    private Class testClass;
    private final SchemaValidator schemaValidator;

    /**
     * Initialise.
     *
     * @param testClass The class against which all relative resource
     *                  references are resolved.
     * @param schemata  The schemata that is to be tested.
     */
    public SchemaTestBuilder(Class testClass, final Schemata schemata) {
        this.testClass = testClass;
        suite = new TestSuite();
        schemaValidator = new SchemaValidator();
        schemaValidator.addSchemata(schemata);
    }

    /**
     * Add a test for a valid document.
     *
     * @param resource The class relative path to the document resource.
     */
    public void addValidDocument(String resource) {
        Test test = new ValidDocument(testClass, resource, schemaValidator);
        suite.addTest(test);
    }

    /**
     * Add a test for an invalid document.
     *
     * @param resource      The class relative path to the document resource.
     * @param expectedError The error that is expected.
     */
    public void addInvalidDocument(
            String resource,
            ValidationError expectedError) {
        Test test = new InvalidDocument(testClass, resource, expectedError,
                schemaValidator);
        suite.addTest(test);
    }

    /**
     * Get the {@link TestSuite} that has been built.
     *
     * @return The test suite.
     */
    public TestSuite getSuite() {
        return suite;
    }

    /**
     * Call this to change the class against which relative resource references
     * are resolved.
     *
     * @param testClass The class against which all relative resource
     *                  references are resolved.
     */
    public void useClass(Class testClass) {
        this.testClass = testClass;
    }
}
