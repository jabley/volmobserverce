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

import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.content.ContentInput;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.schema.validator.SchemaValidator;

/**
 * Base class for all XML document related schema tests.
 */
public class AbstractTestDocument
        extends TestCaseAbstract {

    protected final Class testClass;
    protected final String resource;
    private final SchemaValidator validator;

    /**
     * Initialise.
     *
     * @param testClass       The test class from which the resource is
     *                        relative.
     * @param resource        A test class relative path to the resource.
     * @param schemaValidator The validator to use in the test.
     */
    public AbstractTestDocument(
            Class testClass, String resource,
            final SchemaValidator schemaValidator) {
        super(resource);

        this.testClass = testClass;
        this.resource = resource;
        validator = schemaValidator;
    }

    /**
     * Run the test.
     */
    protected void runTest() throws Throwable {
        validator.validate(getClassRelativeResourceAsContent());
    }

    /**
     * Get a {@link ContentInput} instance that encapsulates the resource
     * that is relative to the class.
     *
     * @return The {@link ContentInput} instance.
     */
    private ContentInput getClassRelativeResourceAsContent() {
        return new BinaryContentInput(testClass.getResourceAsStream(resource));
    }
}
