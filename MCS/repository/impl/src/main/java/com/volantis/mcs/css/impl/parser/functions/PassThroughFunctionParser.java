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
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

/**
 * A parser that passes the information through by creaing a StyleFunctionCall
 * that encapsulates the information.
 */
public class PassThroughFunctionParser
        implements FunctionParser {

    /**
     * The object to use to create style values.
     */
    protected static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    // Javadoc inherited.
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments) {
        return STYLE_VALUE_FACTORY.getFunctionCall(location, name, arguments);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
