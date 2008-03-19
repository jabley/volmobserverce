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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.styling.properties.StyleProperty;

/**
 * Creates and provides access to the parsers for different properties.
 *
 * <p>Instances of this must be thread safe as must the instances of the
 * parsers and other objects returned by instances of this.</p>
 */
public interface PropertyParserFactory {

    /**
     * Get the property parser for the specified property.
     *
     * <p>If no such property could be found then return null.</p>
     *
     * @param propertyName The name of the property.
     *
     * @return The parser, or null if none could be found.
     */
    PropertyParser getPropertyParser(String propertyName);

    /**
     * Get the value handler for this property that can be used inside a
     * shorthand.
     *
     * <p>If no such property could be found then return null.</p>
     *
     * @param propertyName The property name.
     *
     * @return The value handler, or null if none could be found.
     */
    ShorthandValueHandler getShorthandValueHandler(String propertyName);

    /**
     * Get the value handler for this property that can be used inside a
     * shorthand.
     *
     * <p>If no such property could be found then return null.</p>
     *
     * @param property The property.
     *
     * @return The value handler, or null if none could be found.
     */
    ShorthandValueHandler getShorthandValueHandler(StyleProperty property);

    /**
     * Get the value parser for the specified property.
     *
     * <p>This only returns a value for those properties that have a single
     * value, i.e. are not shorthands.</p>
     *
     * @param property The property.
     *
     * @return The parser, or null if none could be found.
     */
    ValueConverter getValueConverter(StyleProperty property);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
