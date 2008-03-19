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

package com.volantis.mcs.themes;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

import java.util.Iterator;

/**
 * An array of {@link PropertyValue}.
 */
public abstract class PropertyValueArray {

    /**
     * Get the style value for the specified property.
     *
     * @param property The property whose value is required.
     * @return The style value, or null if it was not set.
     */
    public StyleValue getStyleValue(StyleProperty property) {
        PropertyValue propertyValue = getPropertyValue(property);
        return propertyValue == null ? null : propertyValue.getValue();
    }

    /**
     * Get the property value for the specified property.
     *
     * @param property The property whose value is required.
     * @return The property value, or null if it was not set.
     */
    public abstract PropertyValue getPropertyValue(StyleProperty property);

    /**
     * Iterate over the property values.
     *
     * <p>This only iterates over the properties that are set and iteration
     * will terminate either when the end of the array is reached, or the
     * iteratee returns {@link IterationAction#BREAK}.</p>
     *
     * @param iteratee The object that is invoked for each property value.
     * @return The result of the last call to the iteratee.
     */
    public abstract IterationAction iteratePropertyValues(
            PropertyValueIteratee iteratee);

    /**
     * Get an iterator over the property values.
     *
     * <p>This only iterates over the properties that are set and iteration
     * will terminate either the end of the array is reached.</p>
     *
     * @return The iterator.
     */
    public abstract Iterator propertyValueIterator();

    /**
     * Set the style value for the specified property.
     *
     * <p>The priority of the value will be {@link Priority#NORMAL}.</p>
     *
     * @param property The property whose value is to be set.
     * @param value    The new value, may be null to clear the property.
     */
    public void setStyleValue(StyleProperty property, StyleValue value) {
        if (value == null) {
            clearPropertyValue(property);
        } else {
            setPropertyValue(
                ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, value));
        }
    }

    /**
     * Set the property value for the specified property.
     *
     * @param propertyValue The property value to set.
     */
    public abstract void setPropertyValue(PropertyValue propertyValue);

    /**
     * Clear the value for the property provided.
     *
     * @param property the property who's value should be cleared.
     *
     * @return The old property value.
     */
    public abstract PropertyValue clearPropertyValue(StyleProperty property);

    /**
     * Check to see whether the array is empty.
     *
     * @return True if the array is empty, false otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * Create a shallow copy of the array.
     *
     * @return A new copy of the array.
     */
    public abstract PropertyValueArray copyPropertyValueArray();

    /**
     * Clear the array.
     */
    public abstract void clear();

    /**
     * Get the contents of this as an array.
     *
     * <p>The returned array contains all the {@link PropertyValue} from this
     * in the order they would be visited by the iterator methods.</p>
     *
     * @return An array of {@link PropertyValue}.
     */
    public abstract PropertyValue[] toPropertyValueArray();

    // Javadoc inherited from super class.
    public abstract boolean equals(Object o);

    // Javadoc inherited from super class.
    public abstract int hashCode();

    // Javadoc inherited.
    public String toString() {
        return super.toString() + " {" + getStandardCSS() + "}";
    }

    /**
     * Get the standard CSS representation of this array.
     *
     * <p>Treated as a number of declarations.</p>
     *
     * @return The CSS representation.
     */
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        appendStandardCSS(buffer);
        return buffer.toString();
    }

    /**
     * Append the standard CSS representation to the buffer.
     *
     * @param buffer The buffer.
     */
    public abstract void appendStandardCSS(final StringBuffer buffer);

    /**
     * Override the values in the specified array by the values in this array.
     *
     * @param array The array to override.
     */
    public abstract void override(PropertyValueArray array);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
