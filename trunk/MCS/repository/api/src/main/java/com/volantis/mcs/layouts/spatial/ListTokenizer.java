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

package com.volantis.mcs.layouts.spatial;

import java.util.StringTokenizer;

/**
 * Tokenizes a white space separated list into an array of tokens.
 *
 * <p>This supports XML schema style list types.</p>
 *
 * @mock.generate
 */
public class ListTokenizer {

    /**
     * Extract tokens into an array of Strings.
     *
     * @param styleClasses
     *         the string contain space separated list of style
     * @return tokens as an array of Strings.
     */
    public String[] extractTokens(String styleClasses) {
        String[] tokens = null;

        if (styleClasses != null && styleClasses.trim().length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(styleClasses, " ");
            tokens = new String[tokenizer.countTokens()];
            int index = 0;

            while(tokenizer.hasMoreTokens()) {
                tokens[index++] = tokenizer.nextToken();
            }
        }

        return tokens;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
