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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.constraints.Contains;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.MatchesLanguage;
import com.volantis.mcs.themes.constraints.Set;
import com.volantis.mcs.themes.constraints.StartsWith;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * A parser for converting attribute selectors to text.
 */
public class AttributeSelectorParser implements ObjectParser {
    // Javadoc inherited
    public String objectToText(Object object) {
        if (object instanceof AttributeSelector) {
            AttributeSelector selector = (AttributeSelector) object;
            Constraint constraint = selector.getConstraint();
            StringBuffer buffer = new StringBuffer("[");
            buffer.append(selector.getName());
            if (constraint instanceof Equals) {
                buffer.append("=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof MatchesLanguage) {
                buffer.append("|=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof ContainsWord) {
                buffer.append("~=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof Contains) {
                buffer.append("*=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof EndsWith) {
                buffer.append("$=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof StartsWith) {
                buffer.append("^=\"").append(constraint.getValue()).append('"');
            } else if (constraint instanceof Set) {
                // No additional content required
            }
            buffer.append("]");

            return buffer.toString();
        } else {
            throw new IllegalArgumentException("Can only parse attribute " +
                    "selectors, not " + (object == null ? "null values" :
                    String.valueOf(object.getClass())));
        }
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
