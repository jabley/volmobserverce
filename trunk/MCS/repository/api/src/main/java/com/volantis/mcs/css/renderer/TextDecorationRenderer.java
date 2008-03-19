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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/TextDecorationRenderer.java,v 1.2 2002/05/23 09:17:01 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. A renderer for 
 *                              css textDecoration style properties.
 * 22-May-02    Doug            VBM:2002051701 - Modified the method
 *                              getKeywordMapper() to take a RendererContext
 *                              argument and ensured that the returned 
 *                              KeyordMapper is obtained via a 
 *                              KeywordMapperFactory object.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextOverlineStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * The css2 renderer for the "text-decoration" property.
 */
public class TextDecorationRenderer
        extends PropertyRenderer {

    private static final StyleProperty[] PROPERTIES = new StyleProperty[] {
        StylePropertyDetails.MCS_TEXT_BLINK,
        StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
        StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE,
        StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
    };

    private static final StyleKeyword[] ENUMERATION_VALUES = new StyleKeyword[] {
        MCSTextBlinkKeywords.BLINK,
        MCSTextLineThroughStyleKeywords.SOLID,
        MCSTextOverlineStyleKeywords.SOLID,
        MCSTextUnderlineStyleKeywords.SOLID,
    };

    private static final String[] CSS_FLAGS = new String[] {
        "blink",
        "line-through",
        "overline",
        "underline"
    };

    // javadoc inherited
    public String getName() {
        return "text-decoration";
    }

    public void render(StyleProperties properties, RendererContext context)
            throws IOException {

        boolean important = false;
        boolean anySet = false;
        int bits = 0;
        for (int i = 0; i < PROPERTIES.length; i++) {
            StyleProperty property = PROPERTIES[i];
            PropertyValue propertyValue = properties.getPropertyValue(property);
            if (propertyValue != null) {

                // If any of the properties are important then we should treat
                // them all as important.
                important |= propertyValue.getPriority() == Priority.IMPORTANT;

                StyleValue value = propertyValue.getValue();

                // Remember whether any of them have been explicitly set, even
                // if it is just to none.
                anySet |= isKeyword(value);

                boolean on = value == ENUMERATION_VALUES[i];
                if (on) {
                    bits |= 1 << i;
                }
            }
        }

        // If none of them have been set then there is nothing to do.
        if (!anySet) {
            return;
        }

        PrintWriter writer = context.getPrintWriter();
        writer.print("text-decoration:");

        // If any of them have been explicitly set then write out the
        // appropriate flags, otherwise write out none.
        if (bits == 0) {
            writer.print("none");
        } else {
            boolean separatorNeeded = false;
            for (int i = 0; i < CSS_FLAGS.length; i++) {
                String cssFlag = CSS_FLAGS[i];
                int bit = 1 << i;
                if ((bits & bit) == bit) {
                    if (separatorNeeded) {
                        writer.print(" ");
                    }
                    writer.print(cssFlag);
                    separatorNeeded = true;
                }
            }
        }
        if (important) {
            context.renderPriority(Priority.IMPORTANT);
        }
        writer.print(";");
    }

    private boolean isKeyword(StyleValue value) {
        return (value instanceof StyleKeyword);
    }

    // Javadoc inherited.
    public KeywordMapper getKeywordMapper(RendererContext context) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public StyleValue getValue(StyleProperties properties) {
        throw new UnsupportedOperationException();
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 13-Sep-05	9496/2	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
