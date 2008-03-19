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

package com.volantis.mcs.themes.values;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.ObjectHelper;

/**
 * A special extension of {@link ShorthandValue} that is used to represent
 * the font shorthand value.
 */
public class FontShorthandValue
        extends ShorthandValue {

    /**
     * The properties in the font shorthands.
     */
    public static final StyleProperty[] PROPERTIES =
            PropertyGroups.FONT_PROPERTIES;

    /**
     * The index of the 'font-family' value in the shorthand.
     */
    public static final int FAMILY_INDEX =
            findProperty(StylePropertyDetails.FONT_FAMILY);

    /**
     * The index of the 'line-height' value in the shorthand.
     */
    public static final int LINE_HEIGHT_INDEX =
            findProperty(StylePropertyDetails.LINE_HEIGHT);

    /**
     * The index of the 'font-size' value in the shorthand.
     */
    public static final int SIZE_INDEX =
            findProperty(StylePropertyDetails.FONT_SIZE);

    /**
     * The index of the 'font-style' value in the shorthand.
     */
    public static final int STYLE_INDEX =
            findProperty(StylePropertyDetails.FONT_STYLE);

    /**
     * The index of the 'font-variant' value in the shorthand.
     */
    public static final int VARIANT_INDEX =
            findProperty(StylePropertyDetails.FONT_VARIANT);

    /**
     * The index of the 'font-weight' value in the shorthand.
     */
    public static final int WEIGHT_INDEX =
            findProperty(StylePropertyDetails.FONT_WEIGHT);

    /**
     * Find the index of the property within the array of standard properties
     * of the shorthand.
     *
     * @param property The property to find.
     * @return The index.
     */
    private static int findProperty(StyleProperty property) {
        StyleProperty[] properties = PROPERTIES;
        for (int i = 0; i < properties.length; i++) {
            StyleProperty p = properties[i];
            if (p == property) {
                return i;
            }
        }

        throw new IllegalArgumentException("Cannot find " + property);
    }

    /**
     * The system font value, may be null.
     */
    private final StyleValue systemFont;

    /**
     * Initialise.
     *
     * @param shorthand The shorthand.
     * @param values    The values.
     * @param priority  The priority.
     */
    public FontShorthandValue(
            StyleShorthand shorthand, StyleValue[] values, Priority priority) {
        this(shorthand, null, values, priority);
    }

    /**
     * Initialise.
     *
     * @param shorthand  The shorthand.
     * @param systemFont The system font.
     * @param values     The values.
     * @param priority   The priority.
     */
    public FontShorthandValue(
            StyleShorthand shorthand, StyleValue systemFont,
            StyleValue[] values, Priority priority) {
        super(shorthand, values, priority);
        this.systemFont = systemFont;
    }

    // Javadoc inherited.
    public int getStandardCost() {
        if (systemFont != null) {
            int cost = 0;
            cost += shorthand.getName().length() + 1 +
                    systemFont.getStandardCost() +
                    priority.getStandardCost();

            for (int i = 1; i < PROPERTIES.length; i++) {
                StyleProperty property = PROPERTIES[i];
                StyleValue value = values[i];
                if (value != null) {
                    cost += 1 + property.getName().length() + 1 +
                            value.getStandardCost() +
                            priority.getStandardCost();
                }
            }

            return cost;
        } else {
            return super.getStandardCost();
        }
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        if (systemFont != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(shorthand.getName()).append(':')
                    .append(systemFont.getStandardCSS())
                    .append(priority.getStandardCSS());

            for (int i = 1; i < PROPERTIES.length; i++) {
                StyleProperty property = PROPERTIES[i];
                StyleValue value = values[i];
                if (value != null) {
                    buffer.append(";")
                            .append(property.getName())
                            .append(":")
                            .append(value.getStandardCSS())
                            .append(priority.getStandardCSS());
                }
            }
            return buffer.toString();
        } else {
            return super.getStandardCSS();
        }
    }

    // Javadoc inherited.
    protected String selectSeparator(String separator, int index) {
        if (index == LINE_HEIGHT_INDEX) {
            return "/";
        } else {
            return separator;
        }
    }

    /**
     * Get the system font.
     *
     * @return The system font.
     */
    public StyleValue getSystemFont() {
        return systemFont;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            FontShorthandValue other = (FontShorthandValue) obj;
            return ObjectHelper.equals(systemFont, other.systemFont);
        } else {
            return false;
        }
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + ObjectHelper.hashCode(systemFont);
        return result;
    }
}
