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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.mcs.themes.mappers.KeywordMapper;

import java.io.IOException;

/**
 * A simple property renderer that stores the a {@link StyleProperty}
 * instance.
 *
 * This class intends to reduce the number of XXXRenderer files necessary
 * for rendering properties. See the CSSStyleSheetRenderer file for
 * a list of property<->renderer associations. A number of renderers may be
 * removed if they can be handled generically by this class.
 */
public class SimplePropertyRenderer extends PropertyRenderer {

    private final StyleProperty details;

    private final String name;

    /**
     * Construct this object with the specified {@link StyleProperty}
     * instance.
     */
    public SimplePropertyRenderer(StyleProperty details, String name) {
        if (details == null) {
            throw new IllegalArgumentException(
                    "StylePropertyDetails cannot be null.");
        }
        this.details = details;
        this.name = name;
    }

    public SimplePropertyRenderer(StyleProperty details) {
        this(details, details.getName());
    }

    public void renderValue(StyleValue value, RendererContext context)
            throws IOException {

        if (details == StylePropertyDetails.CONTENT) {
            context.setListSeparator(" ");
        }

        try {
            super.renderValue(value, context);
        } finally {
            context.resetListSeparator();
        }
    }

    // javadoc inherited
    public KeywordMapper getKeywordMapper(RendererContext context) {
        return context.getKeywordMapper(details);
    }

    // javadoc inherited
    public String getName() {
        return name;
    }

    // javadoc inherited
    public StyleValue getValue(StyleProperties styleProperties) {
        return styleProperties.getStyleValue(details);
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        return properties.getPropertyValue(details);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 29-Apr-04	3937/3	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers - fixed review issues

 28-Apr-04	3937/1	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers

 ===========================================================================
*/
