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


package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.MCSInputFormatKeywords;

import java.io.IOException;
import java.io.Writer;

/**
 * This abstract renderer provides the ability to skip rendering if the
 * MarinerInputFormat is NONE.
 */
public abstract class AbstractMarinerInputFormatRenderer
        extends PropertyRenderer {


    // javadoc inherited
    public KeywordMapper getKeywordMapper(RendererContext context) {
        return context.getKeywordMapper(StylePropertyDetails.MCS_INPUT_FORMAT);
    }

    // javadoc inherited
    public void render(StyleProperties properties, RendererContext context)
            throws IOException {

        String name = getExternalName();
        if (name == null) {
            return;
        }

        PropertyValue propertyValue = getPropertyValue(properties);
        if (propertyValue == null) {
            return;
        }

        StyleValue value = propertyValue.getValue();
        if (value == MCSInputFormatKeywords.NONE) {
            return;
        }

        Writer writer = context.getWriter();
        writer.write(name);
        writer.write(':');
        renderValue(value, context);
        context.renderPriority(propertyValue.getPriority());
        writer.write(';');
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
