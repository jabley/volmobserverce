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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.styling.properties.StyleProperty;

public final class PropertyValueImpl implements PropertyValue {

    private StyleProperty property;

    private StyleValue value;

    private Priority priority;

    /**
     * Initialise for JiBX.
     */
    PropertyValueImpl() {
        // Default the priority to normal.
        // priority = Priority.NORMAL; // doesn't work, JiBX will explicitly
        // set the value back to null
    }

    public PropertyValueImpl(StyleProperty property, StyleValue value) {
        this(property, value, Priority.NORMAL);
    }

    public PropertyValueImpl(
            StyleProperty property, StyleValue value, Priority priority) {

        if (property == null) {
            throw new IllegalArgumentException("property cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("priority cannot be null");
        }

        this.property = property;
        this.value = value;
        this.priority = priority;
    }

    public StyleProperty getProperty() {
        return property;
    }

    public StyleValue getValue() {
        return value;
    }

    public Priority getPriority() {
        if (priority == null) {
            priority = Priority.NORMAL;
        }
        return priority;
    }

    public String toString() {
        return getStandardCSS();
    }

    public String getStandardCSS() {
        return property.getName() + ":" + value + getPriority().getStandardCSS();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PropertyValue)) {
            return false;
        }

        PropertyValue other = (PropertyValue) obj;
        return other.getProperty() == property
                && other.getPriority() == getPriority()
                && other.getValue().equals(value);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = 0;
        result = 37 * result + property.hashCode();
        result = 37 * result + getPriority().hashCode();
        result = 37 * result + value.hashCode();
        return result;
    }

    public void validate(ValidationContext context) {
        Step step = context.pushPropertyStep(property.getName());

        StyleType type = property.getStandardDetails().getSupportedStructure();
        type.validate(context, value);

        context.popStep(step);
    }

    boolean jibxHasPriority() {
        return getPriority() != Priority.NORMAL;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
