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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.functions;

import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.expressions.AbstractStylingFunction;
import com.volantis.styling.expressions.EvaluationContext;

import java.util.List;

public class AttrFunction extends AbstractStylingFunction {
    
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    // Javadoc inherited.
    public StyleValue evaluate(
            EvaluationContext context, String name, List arguments) {

        StyleIdentifier identifier =
                getArgumentAsIdentifier(name, arguments, 0);
        String attributeName = identifier.getName();

        String attributeValue = context.getAttributeValue(null, attributeName);
        if (attributeValue == null) {
            attributeValue = "";
        }

        return STYLE_VALUE_FACTORY.getString(null, attributeValue);
    }
}
