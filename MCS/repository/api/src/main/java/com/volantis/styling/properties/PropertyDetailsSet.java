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

package com.volantis.styling.properties;

import com.volantis.mcs.themes.StyleValues;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface PropertyDetailsSet
        extends StylePropertyIterator {

    /**
     * Get the property details for the specified property.
     *
     * @param property The property whose details are requested.
     * @return The details for the property.
     */
    PropertyDetails getPropertyDetails(StyleProperty property);

    /**
     * Get the style properties for the root element.
     *
     * @return The style properties for the root element.
     */
    StyleValues getRootStyleValues();

    /**
     * Get the set of properties supported by this.
     *
     * @return The set of supported properties.
     */
    ImmutableStylePropertySet getSupportedProperties();

    /**
     * Get an ordered array of the style properties supported by this set
     * including those properties in the provided set.
     *
     * <p>The returned array is in order such that a property whose initial
     * value is dependent on another property comes before that property. e.g.
     * border-top-color comes before color because border-top-color's initial
     * value is dependent on color.</p>
     *
     * @param properties The properties to include from the array.
     * @return An array of properties.
     */
    StyleProperty[] getOrderedPropertyArray(StylePropertySet properties);
}
