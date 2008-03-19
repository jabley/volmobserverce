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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ValidationHelper;

/**
 * This class provides common methods for those protocols which provide WML
 * form field validation.
 */
public class WMLValidationHelper
        implements ValidationHelper {

    /**
     * The default instance of this class.
     */
    private static final ValidationHelper DEFAULT_INSTANCE =
            new WMLValidationHelper();

    /**
     * Get the default instance of this class.
     */
    public static ValidationHelper getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    public String createTextInputFormat(String pattern) {
        StringBuffer sb = new StringBuffer();
        char c;
        // need to modify pattern for wml
        // first 2 charactes in format represent the format
        // type ie N: indicates number format type
        for (int i = 2; i < pattern.length(); i++) {
            c = pattern.charAt(i);
            switch (c) {
                case '#':
                case 'D':
                case 'Y':
                case 'H':
                case 's':
                    sb.append("N");
                    break;
                case 'S':
                    // Matches a number, symbol, or punctuation mark.
                    sb.append("n");
                    break;
                case 'M':
                case 'm':
                    char firstChar = pattern.charAt(0);
                    if ((firstChar == 'D' || firstChar == 'd') &&
                            pattern.charAt(1) == ':') {
                        sb.append("N");
                    } else {
                        sb.append(c);
                    }
                    break;
                case '.':
                case '-':
                    ;
                case '+':
                case '/':
                    ;
                case ':':
                case ' ':
                    sb.append("\\").append(c);
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/1	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 05-Sep-03	1321/1	adrian	VBM:2003082111 added wcss input validation for xhtmlmobile

 ===========================================================================
*/
