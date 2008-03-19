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
package com.volantis.mcs.themes.impl.constraints;

import com.volantis.mcs.themes.constraints.ContainsWord;

/**
 * Test whether the attribute value when treated as list of space separated
 * words contains the specified value as a word in that list.
 */
public class ContainsWordImpl extends ConstraintImpl implements ContainsWord {

    // Javadoc inherited.
    public boolean evaluate(String actualValue) {
        if (actualValue == null) {
            return false;
        }

        // Instead of splitting the list into individual words which will
        // take a while and generate garbage, search for the value in the
        // string and then see whether it is surrounded by whitespace
        // characters (or is at the start and/or end of the string).
        int index = actualValue.indexOf(value);
        if (index == -1) {
            return false;
        }

        // If it is not at the start or it is not preceded by a whitespace
        // character then the test fails.
        if (index != 0 &&
                !Character.isWhitespace(actualValue.charAt(index - 1))) {
            return false;
        }

        // Move to the end of the word.
        index += value.length();
        int length = actualValue.length();

        // If it is not at the end, or it is not followed by a whitespace
        // character then the test fails.
        return index >= length ||
            Character.isWhitespace(actualValue.charAt(index));

    }

    // Javadoc inherited.
    public String toString() {
        return "contains-word";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 ===========================================================================
*/
