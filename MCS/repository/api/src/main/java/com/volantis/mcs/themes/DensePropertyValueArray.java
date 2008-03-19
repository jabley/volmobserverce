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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

/**
 * An array of {@link PropertyValue} where the value for a property is at the
 * same position within the array as the index of the property.
 */
public class DensePropertyValueArray
        extends PropertyValueArray {

    /**
     * The number of properties supported.
     */
    private static final int PROPERTY_COUNT =
            StylePropertyDetails.getPropertyCount();

    /**
     * The PropertyValue values (indexed by StyleProperties indices) that are
     * contained in this instance.
     */
    private PropertyValue[] propertyValues;

    /**
     * The hash code of this object.
     *
     * <p>If this is 0 then it has either never been generated, or the object
     * has been modified since it was last generated.</p>
     */
    private int cachedHashCode;

    /**
     * Creates a new instance of this class, creating an empty array of
     * property values
     */
    public DensePropertyValueArray() {

        // Initialise the array
        propertyValues = new PropertyValue[PROPERTY_COUNT];
    }

    /**
     * Create a new instance of this class, initialising for values, followed
     * by population of these with existing values.  If a null value is passed
     * into this constructor then it will throw an
     * {@link IllegalArgumentException}.
     *
     * @param array The property values that should be added into
     *              this object at creation time.  May not be null.
     */
    public DensePropertyValueArray(PropertyValueArray array) {
        if (array == null) {
            throw new IllegalArgumentException("array cannot be null");
        }

        // Initialise the array
        propertyValues = new PropertyValue[PROPERTY_COUNT];

        if (array instanceof DensePropertyValueArray) {
            DensePropertyValueArray dense = (DensePropertyValueArray) array;
            System.arraycopy(dense.propertyValues, 0, propertyValues, 0, dense.propertyValues.length);
        } else {
            Iterator iterator = array.propertyValueIterator();
            while (iterator.hasNext()) {
                PropertyValue value = (PropertyValue) iterator.next();
                setPropertyValue(value);
            }
        }
    }

    // Javadoc inherited.
    public PropertyValue getPropertyValue(StyleProperty property) {
        return propertyValues[property.getIndex()];
    }

    // Javadoc inherited.
    public void setPropertyValue(PropertyValue propertyValue) {
        StyleProperty property = propertyValue.getProperty();
        propertyValues[property.getIndex()] = propertyValue;
    }

    // Javadoc inherited.
    public PropertyValue clearPropertyValue(StyleProperty property) {
        final int index = property.getIndex();
        PropertyValue old = propertyValues[index];
        propertyValues[index] = null;
        return old;
    }

    // Javadoc inherited.
    public IterationAction iteratePropertyValues(
            PropertyValueIteratee iteratee) {

        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                action = iteratee.next(propertyValue);
                if (action == IterationAction.BREAK) {
                    break;
                }
            }
        }

        return action;
    }

    // Javadoc inherited.
    public Iterator propertyValueIterator() {
        return new Iterator() {

            private PropertyValue next;

            private int index;

            public boolean hasNext() {
                while (next == null && index < propertyValues.length) {
                    next = propertyValues[index];
                    index += 1;
                }

                return next != null;
            }

            public Object next() {
                if (hasNext()) {
                    Object object = next;
                    next = null;
                    return object;
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Javadoc inherited.
    public boolean isEmpty() {

        boolean empty = true;
        for (int i = 0; empty && i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                empty = false;
            }
        }
        return empty;
    }

    // Javadoc inherited.
    public PropertyValueArray copyPropertyValueArray() {
        return new DensePropertyValueArray(this);
    }

    // Javadoc inherited.
    public void clear() {
        Arrays.fill(propertyValues, null);
    }

    // Javadoc inherited.
    public PropertyValue[] toPropertyValueArray() {
        List list = new ArrayList();
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                list.add(propertyValue);
            }
        }
        PropertyValue[] array = new PropertyValue[list.size()];
        list.toArray(array);
        return array;
    }

    // Javadoc inherited from super class.
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof DensePropertyValueArray) {
            DensePropertyValueArray other = (DensePropertyValueArray) o;
            return Arrays.equals(propertyValues, other.propertyValues);
        } else if (o instanceof PropertyValueArray) {
            //@todo We should really try to compare these using the interface.
            return false;
        }
        return false;
    }

    // Javadoc inherited.
    public int hashCode() {
        if (cachedHashCode == 0) {

            for (int i = 0; i < propertyValues.length; i++) {
                PropertyValue propertyValue = propertyValues[i];
                if (propertyValue != null) {
                    cachedHashCode += propertyValue.hashCode();
                }
            }

            // Make sure that we do not keep generating the hash code in the
            // very unlikely case where it is 0.
            if (cachedHashCode == 0) {
                cachedHashCode = 1;
            }
        }

        return cachedHashCode;
    }

    // Javadoc inherited.
    public void appendStandardCSS(StringBuffer buffer) {
        int count = 0;
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                if (count > 0) {
                    buffer.append(";");
                }
                buffer.append(propertyValue);
                count += 1;
            }
        }
    }

    // Javadoc inherited.
    public void override(PropertyValueArray array) {
        for (int i = 0; i < propertyValues.length; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (propertyValue != null) {
                array.setPropertyValue(propertyValue);
            }
        }
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
