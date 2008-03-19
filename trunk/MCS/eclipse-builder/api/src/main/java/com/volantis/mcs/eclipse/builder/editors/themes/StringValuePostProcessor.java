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

import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;

/**
 * A post-processor for string values.
 */
public class StringValuePostProcessor extends AbstractPostProcessor {
    /**
     * The property being post-processed.
     */
    private StyleProperty property;

    /**
     * Create a string value post processor for the specified property.
     *
     * @param property The property to post-process
     */
    public StringValuePostProcessor(StyleProperty property) {
        this.property = property;
    }

    // Javadoc inherited
    public StyleValue postProcess(final StyleValue value) {
        StyleValue postProcessed = value;
        String parseable = null;
        if (value instanceof StyleInvalid) {
            parseable = ((StyleInvalid) value).getValue();
        }
        if (parseable != null) {
            parseable = "\"" + parseable + "\"";
            StyleValue quotedParsed = parseStyleValue(property, parseable);
            if (quotedParsed != null &&
                    !(quotedParsed instanceof StyleInvalid)) {
                postProcessed = quotedParsed;
            }
        }
        return postProcessed;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 25-Nov-05	10459/1	adrianj	VBM:2005112310 Post processing for content style property

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 ===========================================================================
*/
