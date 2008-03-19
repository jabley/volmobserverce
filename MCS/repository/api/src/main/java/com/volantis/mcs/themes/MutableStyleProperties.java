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
package com.volantis.mcs.themes;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.mcs.themes.values.ShorthandValue;

/**
 * @mock.generate
 */
public interface MutableStyleProperties extends ThemeVisitorAcceptor, StyleProperties {
    // JavaDoc inherited
    void setStyleValue(StyleProperty property, StyleValue value);

    /**
     * Set the property value for the specified property.
     *
     * @param propertyValue The property value to set.
     */
    void setPropertyValue(PropertyValue propertyValue);

    /**
     * Clear the style value for the property provided.
     *
     * @param property the property who's style value should be cleared.
     */
    PropertyValue clearPropertyValue(StyleProperty property);

    /**
     * Set the shorthand value.
     *
     * @param shorthandValue The shorthand value.
     */
    void setShorthandValue(ShorthandValue shorthandValue);

    /**
     * Clear the shorthand value for the specified shorthand.
     *
     * @param shorthand The shorthand whose value must be cleared.
     */
    void clearShorthandValue(StyleShorthand shorthand);
}
