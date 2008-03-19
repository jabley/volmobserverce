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

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.values.InitialValueSource;

import java.util.Set;

/**
 * Contains potentially device specific details about a style property.
 *
 * @mock.generate 
 */
public interface PropertyDetails {

    /**
     * Get the style property to which this information relates.
     *
     * @return The style property.
     */
    StyleProperty getProperty();

    /**
     * Get the initial value of the property.
     *
     * <p>In some cases the specification does not define what the initial
     * value should be, leaving it up to the user agent to determine. In this
     * case this method returns null.</p>
     *
     * <p>This only contains the fixed initial value and is not
     * sufficient to deal with the cases where a property's initial value
     * depends on the computed value of another property. In this case the
     * {@link #getInitialValueSource initial value source} should be used
     * instead.</p>
     *
     * @return The initial property value.
     */
    PropertyValue getInitialPropertyValue();

    /**
     * Get the initial value of the property.
     *
     * <p>Helper method that returns the value embedded within the initial
     * {@link PropertyValue} returned by {@link #getInitialPropertyValue()},
     * or null if there is none.</p>
     *
     * @return The initial value.
     */
    StyleValue getInitialValue();

    /**
     * Indicates whether the property value should be inherited from its
     * parent if the cascade does not result in a value.
     *
     * @return True if the propery's value should be inherited and false
     * otherwise.
     */
    boolean isInherited();

    /**
     * Indicates that the property value can be generated from another
     * style property value.
     *
     * @return if the value can be computed from another style
     */
    boolean isComputable();

    /**
     * Get the source of the initial value for the property.
     *
     * <p>The initial value can be defined in a number of different ways.</p>
     *
     * <ul>
     *
     * <li>A fixed value indicated by the specification.</li>
     * <li>
     *
     * </ul>
     * <p>In some cases the specification does not define what the initial
     * value should be, leaving it up to the user agent to determine. In this
     * case this method returns null.</p>
     *
     * @return The initial value.
     */
    InitialValueSource getInitialValueSource();

    AllowableKeywords getAllowableKeywords();

    /**
     * Get the unmodifiable set of supported property types.
     *
     * @return An unmodifiable set of supported property types.
     */
    Set getSupportedTypes();

    /**
     * Get the supported structure for this style property.
     *
     * @return A StyleType which defines the structure.
     */
    StyleType getSupportedStructure();

    /**
     * Get an indication of the accuracy of the initial value returned by
     * {@link #getInitialValueSource()}
     *
     * @return {@link InitialValueAccuracy#ASSUMED} if is is assumed to be correct
     * and {@link InitialValueAccuracy#KNOWN} if it is known to be correct.
     */
    InitialValueAccuracy getInitialValueAccuracy();
}

