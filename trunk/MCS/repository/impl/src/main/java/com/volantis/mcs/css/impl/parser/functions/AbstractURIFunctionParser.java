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

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.model.validation.SourceLocation;

import java.util.List;

/**
 * Base of function parsers that handle URI like values.
 */
public abstract class AbstractURIFunctionParser
        implements FunctionParser {

    /**
     * The object to use to create new style values.
     */
    protected static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    // Javadoc inherited.
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments) {
        int argumentCount = arguments.size();
        if (argumentCount != 1) {
            context.addDiagnostic(CSSParserMessages.WRONG_NUMBER_ARGUMENTS,
                             new Object[]{
                                 name,
                                 new Integer(1),
                                 new Integer(argumentCount)
                             });
        }

        // If there are any arguments then take the first one and try and
        // convert it.
        StyleValue convertedValue = null;
        if (argumentCount > 0) {
            StyleValue value = (StyleValue) arguments.get(0);
            if (!(value instanceof StyleString)) {
                context.addDiagnostic(CSSParserMessages.ARGUMENT_TYPE, new Object [] {
                    name,
                    new Integer(1),
                    "string",
                    value.getStyleValueType().getType()
                });
            } else {
                // The value was a string so wrap it in the correct type of
                // value and return it.
                StyleString stringValue = (StyleString) value;
                String uri = stringValue.getString();
                convertedValue = createStyleValue(location, uri);
            }
        }

        return convertedValue;
    }

    /**
     * Create a StyleValue to encapsulate the URI.
     *
     * @param location
     * @param uri The URI to encapsulate.
     *
     * @return The StyleValue encapsulating the URI.
     */
    protected abstract StyleValue createStyleValue(
            SourceLocation location, String uri);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
