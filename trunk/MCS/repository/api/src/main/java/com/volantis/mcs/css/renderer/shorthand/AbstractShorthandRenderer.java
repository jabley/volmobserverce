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

package com.volantis.mcs.css.renderer.shorthand;

import com.volantis.mcs.css.renderer.PropertyRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.CSSPropertyNameMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.Writer;

/**
 * Base class for all renderers of shorthands.
 */
public class AbstractShorthandRenderer
        extends PropertyRenderer {

    // Javadoc inherited.
    public String getName() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public KeywordMapper getKeywordMapper(RendererContext context) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public StyleValue getValue(StyleProperties properties) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public PropertyValue getPropertyValue(StyleProperties properties) {
        throw new UnsupportedOperationException();
    }

    /**
     * Render the shorthand.
     *
     * @param context    The context within which the shorthand will be
     *                   rendered.
     * @param shorthand  The shorthand to render.
     * @param properties The properties containing the shorthand.
     * @return True if the shorthand was rendered false otherwise.
     * @throws IOException If there was a problem rendering the shorthand.
     */
    protected boolean renderShorthand(
            RendererContext context, StyleShorthand shorthand,
            StyleProperties properties) throws IOException {

        Writer writer = context.getWriter();

        ShorthandValue shorthandValue = properties.getShorthandValue(shorthand);
        if (shorthandValue == null) {
            return false;
        }

        final String name = CSSPropertyNameMapper.getDefaultInstance().
                getExternalString(shorthand);
        writer.write(name);
        writer.write(':');
        final int count = shorthandValue.getCount();
        String separator = "";
        for (int i = 0; i < count; i += 1) {
            StyleValue value = shorthandValue.getValue(i);
            if (value != null) {
                writer.write(selectSeparator(separator, i));
                separator = " ";
                renderValue(value, context);
            }
        }
        context.renderPriority(shorthandValue.getPriority());
        writer.write(';');

        return true;
    }

    /**
     * Select the separator to use between the <code>index - 1</code> value of
     * the shorthand and <code>index</code>.
     *
     * <p>By default this returns the separator passed in.</p>
     *
     * @param separator The default separator.
     * @param index     The index.
     * @return The selected separator.
     */
    protected String selectSeparator(String separator, int index) {
        return separator;
    }

    /**
     * Render the individual properties associated with the shorthand.
     *
     * @param properties The properties to render.
     * @param context    The context within which the properties will be
     *                   rendered.
     * @param group      The group of properties to render.
     * @throws IOException If there was a problem rendering the properties.
     */
    protected void renderProperties(
            StyleProperties properties, RendererContext context,
            final StyleProperty[] group) throws IOException {

        Writer writer = context.getWriter();
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            PropertyValue propertyValue = properties.getPropertyValue(property);
            if (propertyValue != null) {
                final String name = CSSPropertyNameMapper.getDefaultInstance().
                        getExternalString(property);
                writer.write(name);
                writer.write(':');

                StyleValue value = propertyValue.getValue();
                renderValue(value, context);
                context.renderPriority(propertyValue.getPriority());
                writer.write(';');
            }
        }
    }

    // Javadoc inherited.
    public void renderValue(StyleValue value, RendererContext context)
            throws IOException {
        context.renderValue(value);
    }
}
