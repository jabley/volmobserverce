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
 * Test whether the attribute value matches a language name.
 */
public class MatchesLanguage
        extends AbstractValueComparator {

    /**
     * Initialise.
     *
     * @param value The value to compare against, may not be null.
     */
    public MatchesLanguage(String value) {
        super(value);
    }

    // Javadoc inherited.
    public boolean satisfied(String value) {
        if (value == null) {
            return false;
        }

        // If it does not start with the value then it fails.
        if (!value.startsWith(constraintValue)) {
            return false;
        }

        // If the attribute value is the same length as the value then they
        // are equal so return.
        int length = constraintValue.length();
        if (value.length() == length) {
            return true;
        }

        // If the character in the attribute value that immediately follows the
        // prefix that matches the value is a - then it matches, otherwise it
        // does not match.
        return value.charAt(length) == '-';
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print("|=").print(constraintValue);
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
