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

package com.volantis.mcs.css.impl.parser.functions;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

public class CountersFunctionParser
        extends ValidatingFunctionParser {

    // Javadoc inherited.
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments) {

        expectArguments(context, name, arguments, 1, 3);
        expectsIdentifier(context, name, arguments, 0);
        int count = arguments.size();
        if (count >= 2) {
            expectsStringExpression(context, name, arguments, 1);
        }
        if (count == 3) {
            StyleKeyword keyword = expectsKeyword(context, name, arguments, 2,
                    ListStyleTypeKeywords.getDefaultInstance());
            arguments.set(2, keyword);
        }


        return super.parse(context, location, name, arguments);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 ===========================================================================
*/
