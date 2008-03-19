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
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An array of {@link PropertyValue} where the values are stored in order by
 * the index of the property and will be searched for using a binary search.
 */
public class SparsePropertyValueArray
        extends PropertyValueArray {

    /**
     * An empty array.
     */
    private static final PropertyValue[] EMPTY_ARRAY = new PropertyValue[0];

    /**
     * The comparator to use for the binary search.
     */
    private static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
            StyleProperty p1;
            StyleProperty p2;
            if (o1 instanceof PropertyValue) {
                PropertyValue propertyValue = (PropertyValue) o1;
                p1 = propertyValue.getProperty();
                p2 = (StyleProperty) o2;
            } else if (o2 instanceof PropertyValue) {
                PropertyValue propertyValue = (PropertyValue) o2;
                p1 = (StyleProperty) o1;
                p2 = propertyValue.getProperty();
            } else if (o1 == null) {
                return +1;
            } else if (o2 == null) {
                return -1;
            } else {
                throw new IllegalStateException();
            }

            return p1.getIndex() - p2.getIndex();
        }
    };

    /**
     * The initial array size.
     */
    private static final int INITIAL_ARRAY_SIZE = 8;

    /**
     * The size to increase the array by when it needs expanding.
     */
    private static final int ADDITIONAL_SPACE = 8;

    /**
     * An array of property values sorted by property index.
     */
    private PropertyValue[] propertyValues;

    /**
     * The number of property values that have been set.
     */
    private int count;

    /**
     * The hash code of this object.
     *
     * <p>If this is 0 then it has either never been generated, or the object
     * has been modified since it was last generated.</p>
     */
    private int cachedHashCode;

    /**
     * Initialise.
     */
    public SparsePropertyValueArray() {
    }

    /**
     * Initialise.
     *
     * @param array The array to copy.
     */
    public SparsePropertyValueArray(PropertyValueArray array) {
        if (array == null) {
            throw new IllegalArgumentException("array cannot be null");
        }

        if (array instanceof SparsePropertyValueArray) {
            SparsePropertyValueArray sparse =
                    (SparsePropertyValueArray) array;
            PropertyValue[] other = sparse.propertyValues;
            if (other != null) {
                propertyValues = new PropertyValue[other.length];
                System.arraycopy(other, 0, propertyValues, 0, other.length);
                count = sparse.count;
            }
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
        if (propertyValues == null) {
            return null;
        } else {
            int index = findPropertyIndex(property);
            if (index >= 0) {
                return propertyValues[index];
            } else {
                return null;
            }
        }
    }

    /**
     * Find the index of the specified property in the array.
     *
     * @param property The property.
     * @return The index, see {@link Arrays#binarySearch}.
     */
    private final int findPropertyIndex(StyleProperty property) {
        return Arrays.binarySearch(propertyValues, property, COMPARATOR);
    }

    // Javadoc inherited.
    public IterationAction iteratePropertyValues(
            PropertyValueIteratee iteratee) {
        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0; i < count && action == IterationAction.CONTINUE; i++) {
            PropertyValue propertyValue = propertyValues[i];
            action = iteratee.next(propertyValue);
        }

        return action;
    }

    // Javadoc inherited.
    public Iterator propertyValueIterator() {
        return new Iterator() {

            private int index;

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return index < count;
            }

            public Object next() {
                if (hasNext()) {
                    PropertyValue propertyValue = propertyValues[index];
                    index += 1;
                    return propertyValue;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    // Javadoc inherited.
    public void setPropertyValue(PropertyValue propertyValue) {
        updatePropertyValue(propertyValue, true);
    }

    /**
     * Update the property value.
     *
     * @param propertyValue The value to set.
     * @param updateIfSet   True if the value should be changed if it is
     *                      already set.
     */
    private void updatePropertyValue(
            PropertyValue propertyValue, boolean updateIfSet) {
        if (propertyValues == null) {
            propertyValues = new PropertyValue[INITIAL_ARRAY_SIZE];
            propertyValues[0] = propertyValue;
            count = 1;
        } else {
            StyleProperty property = propertyValue.getProperty();
            int index = findPropertyIndex(property);
            boolean update;
            if (index < 0) {
                index = -index - 1;
                if (index == propertyValues.length
                        || count == propertyValues.length) {

                    // Expand the array.
                    expandArray(index);

                } else if (index < count) {

                    // The new value needs inserting before some properties but
                    // there is enough space in the current array so we just
                    // need to move the properties up one space to make space
                    // for the new value.
                    System.arraycopy(propertyValues, index,
                            propertyValues, index + 1, count - index);
                }

                // Number of properties have been increased.
                count += 1;

                // The property was not set so always update it.
                update = true;
            } else {
                // The property was already set so only update it if requested.
                update = updateIfSet;
            }

            if (update) {
                propertyValues[index] = propertyValue;
            }
        }

        // Remember that this object was modified.
        modified();
    }

    /**
     * Invoked if the array has been modified.
     */
    private void modified() {
        cachedHashCode = 0;
    }

    /**
     * Expand the array leaving a gap at the specified index.
     *
     * @param index The index at which the gap should be left.
     */
    private void expandArray(int index) {
        // Add a new block to the array.
        int newSize = propertyValues.length + ADDITIONAL_SPACE;

        PropertyValue[] newArray = new PropertyValue[newSize];

        // Copy all the properties up to but not including the index into
        // the new array.
        if (index > 0) {
            System.arraycopy(propertyValues, 0, newArray, 0, index);
        }

        // Copy all the properties after and including the array to the
        // new array but one slot higher.
        if (index < propertyValues.length) {
            System.arraycopy(propertyValues, index, newArray, index + 1,
                    propertyValues.length - index);
        }

        // Use the new array.
        propertyValues = newArray;
    }

    // Javadoc inherited.
    public PropertyValue clearPropertyValue(StyleProperty property) {

        if (propertyValues == null) {
            return null;
        }

        PropertyValue old = null;
        int index = findPropertyIndex(property);
        if (index >= 0) {
            // Update the count of the number of properties.
            count -= 1;

            // Copy any properties above the index to remove down one place.
            if (index < count) {
                System.arraycopy(propertyValues, index + 1,
                        propertyValues, index, count - index);
            }

            // Clear the top property.
            old = propertyValues[count];
            propertyValues[count] = null;

            // Remember that this object was modified.
            modified();
        }

        return old;
    }

    // Javadoc inherited.
    public boolean isEmpty() {
        return count == 0;
    }

    // Javadoc inherited.
    public PropertyValueArray copyPropertyValueArray() {
        return new SparsePropertyValueArray(this);
    }

    // Javadoc inherited.
    public void clear() {
        if (propertyValues != null) {
            Arrays.fill(propertyValues, null);
        }
        count = 0;
    }

    // Javadoc inherited.
    public PropertyValue[] toPropertyValueArray() {
        if (count == 0) {
            return EMPTY_ARRAY;
        } else {
            PropertyValue[] array = new PropertyValue[count];
            System.arraycopy(propertyValues, 0, array, 0, count);
            return array;
        }
    }

    // Javadoc inherited.
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof SparsePropertyValueArray) {
            SparsePropertyValueArray sparse =
                    (SparsePropertyValueArray) o;

            if (sparse.count == count) {
                for (int i = 0; i < count; i++) {
                    PropertyValue p1 = propertyValues[i];
                    PropertyValue p2 = sparse.propertyValues[i];

                    if (!p1.equals(p2)) {
                        return false;
                    }
                }

                return true;
            } else {
                return false;
            }
        } else if (o instanceof PropertyValueArray) {
            //@todo We should really try to compare these using the interface.
            return false;
        } else {
            return false;
        }
    }

    // Javadoc inherited.
    public int hashCode() {
        if (cachedHashCode == 0) {

            for (int i = 0; i < count; i++) {
                PropertyValue propertyValue = propertyValues[i];
                cachedHashCode += propertyValue.hashCode();
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
        for (int i = 0; i < count; i++) {
            PropertyValue propertyValue = propertyValues[i];
            if (i > 0) {
                buffer.append(";");
            }
            buffer.append(propertyValue);
        }
    }

    // Javadoc inherited.
    public void override(PropertyValueArray array) {
        for (int i = 0; i < count; i++) {
            PropertyValue propertyValue = propertyValues[i];
            array.setPropertyValue(propertyValue);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10617/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10589/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
