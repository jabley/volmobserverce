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

import com.volantis.mcs.themes.constraints.MatchesLanguage;

/**
 * Test whether the attribute value matches a language name.
 */
public class MatchesLanguageImpl extends ConstraintImpl
        implements MatchesLanguage {

    // Javadoc inherited.
    public boolean evaluate(String actualValue) {
        if (actualValue == null) {
            return false;
        }

        // If it does not start with the value then it fails.
        if (!actualValue.startsWith(value)) {
            return false;
        }

        // If the attribute value is the same length as the value then they
        // are equal so return.
        int length = actualValue.length();
        if (actualValue.length() == length) {
            return true;
        }

        // If the character in the attribute value that immediately follows the
        // prefix that matches the value is a - then it matches, otherwise it
        // does not match.
        return actualValue.charAt(length) == '-';
    }

    // Javadoc inherited.
    public String toString() {
        return "language-match";
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
