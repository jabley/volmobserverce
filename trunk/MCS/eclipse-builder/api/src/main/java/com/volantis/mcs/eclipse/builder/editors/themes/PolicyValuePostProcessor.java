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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * A post-processor for policy values.
 */
public class PolicyValuePostProcessor extends AbstractPostProcessor {
    /**
     * The property being post-processed.
     */
    private StyleProperty property;

    /**
     * Create a policy value post processor for the specified property.
     * @param property
     */
    public PolicyValuePostProcessor(StyleProperty property) {
        this.property = property;
    }

    // Javadoc inherited
    public StyleValue postProcess(StyleValue value) {
        StyleValue processedValue = value;
        if (value instanceof StyleInvalid) {
            String componentised = "mcs-component-url(\"" +
                    ((StyleInvalid) value).getValue() + "\")";
            StyleValue newValue = parseStyleValue(property, componentised);
            if (!(newValue instanceof StyleInvalid)) {
                processedValue = newValue;
            }
        }
        return processedValue;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 ===========================================================================
*/
