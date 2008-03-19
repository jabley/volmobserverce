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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.values.InitialValueSource;

import java.util.Set;

/**
 * @see com.volantis.shared.inhibitor.Inhibitor
 */
public interface StylePropertySet
        extends StylePropertyIterator {

    public static final StyleProperty END = new StyleProperty() {
        public String getName() {
            throw new IllegalStateException();
        }

        public int getIndex() {
            throw new IllegalStateException();
        }

        public PropertyDetails getStandardDetails() {
            throw new IllegalStateException();
        }
    };

    /**
     * Create an immutable {@link StylePropertySet}.
     *
     * @see com.volantis.shared.inhibitor.Inhibitor#createImmutable
     */
    public ImmutableStylePropertySet createImmutableStylePropertySet();

    /**
     * Create a mutable {@link StylePropertySet}.
     *
     * @return A mutable object.
     * @see com.volantis.shared.inhibitor.Inhibitor#createMutable
     */
    public MutableStylePropertySet createMutableStylePropertySet();

    /**
     * @see com.volantis.shared.inhibitor.Inhibitor#equals
     */
    public boolean equals(Object other);

    /**
     * @see com.volantis.shared.inhibitor.Inhibitor#hashCode
     */
    public int hashCode();

    /**
     * Check whether the set contains the specified property.
     *
     * @param property The property for which the set should be checked.
     *
     * @return True if the set contained the property and false otherwise.
     */
    boolean contains(StyleProperty property);

    /**
     * Get the next style property after the specified one.
     *
     * @param property The last property that was returned by this method, or
     * null if this is the first time the method is called.
     *
     * @return The next method, or {@link #END} if there are no more
     * properties.
     */
    StyleProperty next(StyleProperty property);
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
