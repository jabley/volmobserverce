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

package com.volantis.styling.impl.engine.matchers.constraints;

import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Test whether the attribute value when treated as a space separated list
 * contains the specified value as an item in that list.
 */
public class ListContains
        extends AbstractValueComparator {

    /**
     * Initialise.
     *
     * @param value The value to compare against, may not be null and may not
     * contain any white space characters.
     */
    public ListContains(String value) {
        super(value);

        // Ensure that it does not contain any white space characters in the
        // value.
        int length = value.length();
        for (int i = 0; i < length; i += 1) {
            char c = value.charAt(i);
            if (Character.isWhitespace(c)) {
                throw new IllegalArgumentException(
                        "value '" + value + "' contains whitespace");
            }
        }
    }

    // Javadoc inherited.
    public boolean satisfied(String value) {
        if (value == null) {
            return false;
        }

        // Instead of splitting the list into individual words which will
        // take a while and generate garbage, search for the value in the
        // string and then see whether it is surrounded by whitespace
        // characters (or is at the start and/or end of the string).
        int index = value.indexOf(constraintValue);
        if (index == -1) {
            return false;
        }

        // If it is not at the start or it is not preceded by a whitespace
        // character then the test fails.
        if (index != 0 &&
                !Character.isWhitespace(value.charAt(index - 1))) {
            return false;
        }

        // Move to the end of the word.
        index += constraintValue.length();
        int length = value.length();

        // If it is not at the end, or it is not followed by a whitespace
        // character then the test fails.
        if (index < length &&
                !Character.isWhitespace(value.charAt(index))) {
            return false;
        }

        return true;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print("~=").print(constraintValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
