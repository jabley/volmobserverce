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

/**
 * A test for a valid document.
 */
public class ValidDocument
        extends AbstractTestDocument {

    /**
     * Initialise.
     *
     * @param testClass       The test class from which the resource is
     *                        relative.
     * @param resource        A test class relative path to the resource.
     * @param schemaValidator The validator to use in the test.
     */
    public ValidDocument(
            Class testClass, String resource,
            final SchemaValidator schemaValidator) {

        super(testClass, resource, schemaValidator);
    }
}
