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

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ValidationHelper;

/**
 * Converts a generic XDIME 1 pattern to an XHTMLFull specific format.
 */
public class XHTMLFullValidationHelper
        implements ValidationHelper {

    /**
     * The default instance of this class.
     */
    private static final ValidationHelper DEFAULT_INSTANCE =
            new XHTMLFullValidationHelper();

    /**
     * Get the default instance of this class.
     */
    public static ValidationHelper getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    public String createTextInputFormat(String format) {

        // Strip off the 2 letter prefix
        String pattern = format.substring(2);
        if(pattern != null && format.startsWith("M:")) {
            // Replace Mm with Zz respectively, as otherwise it clashes with
            // the Mm from dates. The pattern is a proprietary format and is
            // processed by our own regular expression type engine.
            pattern = pattern.replace('M','Z').replace('m','z');
        }
        return pattern;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/2	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/2	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 16-Jun-05	8764/3	pduffin	VBM:2005042901 Updated based on review comments

 15-Jun-05	8764/1	pduffin	VBM:2005042901 Added support for specifying symbols, punctuation marks, or numbers in a text format

 ===========================================================================
*/
