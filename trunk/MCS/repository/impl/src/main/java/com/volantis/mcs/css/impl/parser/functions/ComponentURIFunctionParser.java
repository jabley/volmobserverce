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
import com.volantis.mcs.model.validation.SourceLocation;

/**
 * A function parser that converts a function call of the form
 * mcs-component-url() into a StyleComponentURI value.
 */
public class ComponentURIFunctionParser
        extends AbstractURIFunctionParser {

    // Javadoc inherited.
    protected StyleValue createStyleValue(SourceLocation location, String uri) {
        return STYLE_VALUE_FACTORY.getComponentURI(location, uri);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
