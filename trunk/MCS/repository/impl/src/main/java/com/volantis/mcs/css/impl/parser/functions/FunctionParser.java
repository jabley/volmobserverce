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
 * Parses a CSS function like call.
 *
 * <p>Instances of this are responsible for validating that the function call
 * is correct and mapping it to an appropriate instance of StyleValue.</p>
 */
public interface FunctionParser {

    /**
     * Parse the list of arguments for the specified function and return the
     * instance of style value that represents that information.
     *
     * @param context   The context within which this is occuring.
     * @param location  The source location of the object.
     * @param name      The name of the function being mapped.
     * @param arguments The arguments passed to the function, specified as a
     *                  list of StyleValues.
     * @return The StyleValue to represent the function call, or null if it
     *         was not valid.
     */
    public StyleValue parse(
            ParserContext context, SourceLocation location, String name,
            List arguments);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
