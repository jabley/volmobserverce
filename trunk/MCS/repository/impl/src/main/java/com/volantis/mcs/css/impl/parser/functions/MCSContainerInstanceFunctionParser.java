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

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

/**
 * A mcs-container-instance function takes one string followed by any number of
 * integers.
 */
public class MCSContainerInstanceFunctionParser
        extends ValidatingFunctionParser {

    // Javadoc inherited.
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments) {

        expectArguments(context, name, arguments, 1, Integer.MAX_VALUE);
        expectsStringExpression(context, name, arguments, 0);
        for (int i = 1; i < arguments.size(); i += 1) {
            StyleValue value = expectsIntegerExpression(
                    context, name, arguments, i);
            arguments.set(i, value);
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

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
