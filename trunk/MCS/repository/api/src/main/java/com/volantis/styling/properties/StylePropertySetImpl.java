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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.shared.iteration.IterationAction;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Base implementation of {@link StylePropertySet}.
 */
abstract class StylePropertySetImpl
        implements StylePropertySet {

    /**
     * The set of contained properties.
     */
    private BitSet containedProperties;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StylePropertySetImpl(StylePropertySet value) {
        if (value instanceof StylePropertySetImpl) {
            StylePropertySetImpl impl = (StylePropertySetImpl) value;
            containedProperties = new BitSet();
            containedProperties.or(impl.containedProperties);
        } else {
            // todo: Implement this
            throw new UnsupportedOperationException("Cannot copy");
        }
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StylePropertySetImpl() {
        containedProperties = new BitSet();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableStylePropertySet createImmutableStylePropertySet() {
        return new ImmutableStylePropertySetImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableStylePropertySet createMutableStylePropertySet() {
        return new MutableStylePropertySetImpl(this);
    }

    // Javadoc inherited.
    public boolean contains(StyleProperty property) {
        return containedProperties.get(property.getIndex());
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void add(StyleProperty property) {
        containedProperties.set(property.getIndex(), true);
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void add(StylePropertySet set) {
        StylePropertySetImpl other = (StylePropertySetImpl) set;
        containedProperties.or(other.containedProperties);
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void addAll() {
        int count = StylePropertyDetails.getDefinitions().count();
        for (int i = 0; i < count; i += 1) {
            containedProperties.set(i);
        }
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void remove(StyleProperty property) {
        containedProperties.clear(property.getIndex());
    }

    public IterationAction iterateStyleProperties(
            StylePropertyIteratee iteratee) {

        final StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        IterationAction action = IterationAction.CONTINUE;
        int count = containedProperties.length();
        for (int i = 0; i < count && action == IterationAction.CONTINUE;
             i += 1) {

            if (containedProperties.get(i)) {
                StyleProperty property = definitions.getStyleProperty(i);
                action = iteratee.next(property);
            }
        }

        return action;
    }

    public Iterator stylePropertyIterator() {
        final StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();

        final int count = containedProperties.length();
        return new Iterator() {

            private StyleProperty next;

            private int index;

            public boolean hasNext() {
                while (next == null && index < count) {
                    if (containedProperties.get(index)) {
                        next = definitions.getStyleProperty(index);
                    }
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

    public StyleProperty next(StyleProperty property) {

        final StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();

        // Calculate the minimum index of the next property.
        int index;
        if (property == null) {
            index = 0;
        } else {
            index = property.getIndex() + 1;
        }

        int count = containedProperties.length();
        for (; index < count; index +=1) {
            if (containedProperties.get(index)) {
                return definitions.getStyleProperty(index);
            }
        }

        return END;
    }

    public String toString() {
        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        int count = definitions.count();
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        String separator = "";
        for (int i = 0; i < count; i += 1) {
            if (containedProperties.get(i)) {
                StyleProperty property = definitions.getStyleProperty(i);

                buffer.append(separator).append(property.getName());
                separator = ", ";
            }
        }
        buffer.append("]");

        return buffer.toString();
    }

    // Javadoc inherited.
    public void clear() {
        containedProperties.clear();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
