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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer.shorthand;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import com.volantis.mcs.css.renderer.BorderRadiusHelper;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.StyleProperty;

/**
 * Renderer shorthand and individual properties of mcs-border-radius shorthand
 * and mcs-border-top-right-radius, etc. individual properties Renderer change
 * style property to internal (browser dependent style property) like e.g. for
 * mozilla mcs-border-radius shorthand is change to -moz-border-radius shorthand
 * property
 */
public class MCSBorderRadiusRenderer extends ShorthandPropertyRenderer {

    /**
     * Initialize a new instance.
     */
    public MCSBorderRadiusRenderer() {
        super(StyleShorthands.MCS_BORDER_RADIUS);
    }

    // Javadoc inherited.
    protected boolean renderShorthand(RendererContext context,
            StyleShorthand shorthand, StyleProperties properties)
            throws IOException {
    
        ShorthandValue shorthandValue = properties.getShorthandValue(shorthand);
        if (shorthandValue == null) {
            return false;
        }

        // Read from cssVersion by Helper if we have moz, webkit or css3
        // version of shorthand supported by device
        String externalShorthand = null;
        CSSVersion cssVersion = context.getCSSVersion();
        if (null != cssVersion) {
            externalShorthand = BorderRadiusHelper.supportShorthand(cssVersion);
        }

        if (externalShorthand != null) {
            Writer writer = context.getWriter();
            writer.write(externalShorthand);
            writer.write(':');
            StyleValue value = shorthandValue.getValue(0);
            String borderType = BorderRadiusHelper
                    .getBorderRadiusType(externalShorthand);
            if (borderType != null && borderType.equals("moz")) {
                // write first value of Pair
                writer.write(((StylePair) value).getFirst().getStandardCSS());
            } else {
                // write StylePair
                writer.write(value.getStandardCSS());
            }
            writer.write(';');
        }

        return true;
    }

    // Javadoc inherited.
    public void renderProperties(StyleProperties properties,
            RendererContext context, StyleProperty[] group) throws IOException {

        String browserProperty = null;
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            PropertyValue propertyValue = properties.getPropertyValue(property);
            if (propertyValue != null) {
                StyleValue value = propertyValue.getValue();
                browserProperty = BorderRadiusHelper.supportCorner(context
                        .getCSSVersion(), property);

                if (browserProperty != null) {
                    PrintWriter writer = context.getPrintWriter();
                    writer.print(browserProperty);
                    writer.print(':');

                    String borderType = BorderRadiusHelper
                            .getBorderRadiusType(browserProperty);
                    if (borderType != null && borderType.equals("moz")) {
                        // write first value of Pair
                        context.renderValue(((StylePair) value).getFirst());
                    } else {
                        // write StylePair
                        context.renderValue(value);
                    }
                    writer.print(";");
                }
            }
        }
    }
}
