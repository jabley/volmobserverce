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

/**
 * Represents a validation error where an element is unexpected as the content
 * of an element.
 */
public class InvalidContentStartingWith
        extends ValidationError {

    private final String expected;

    /**
     * Initialise.
     *
     * @param element The unexpected element.
     */
    public InvalidContentStartingWith(String element) {
        expected = "error - cvc-complex-type.2.4.a: Invalid content was found " +
                "starting with element '" + element + "'.";
    }

    // Javadoc inherited.
    public boolean matches(String message) {
        return message.startsWith(expected);
    }

    // Javadoc inherited.
    public String getDescription() {
        return "starts-with(" + expected + ")";
    }
}
