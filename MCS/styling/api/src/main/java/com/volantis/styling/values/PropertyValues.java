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

package com.volantis.styling.values;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIterator;

/**
 * Encapsulates a set of property values for a single styleable entity.
 *
 * @mock.generate
 */
public interface PropertyValues
        extends StylePropertyIterator, StyleValues {

    /**
     * Get the definitions of the style properties that can be stored within
     * this collection.
     *
     * @return The definitions of the style properties.
     */
    StylePropertyDefinitions getStylePropertyDefinitions();

    /**
     * Get the computed value for the property.
     *
     * @param property The property whose value should be returned.
     * @return The computed value of the property, may be null.
     */
    StyleValue getComputedValue(StyleProperty property);

    /**
     * Create an immutable {@link PropertyValues}.
     *
     * <p>See com.volantis.shared.inhibitor.Inhibitor#createImmutable</p>
     */
    public ImmutablePropertyValues createImmutablePropertyValues();

    /**
     * Create a mutable {@link PropertyValues}.
     *
     * <p>See com.volantis.shared.inhibitor.Inhibitor#createImmutable</p>
     *
     * @return A mutable object.
     */
    public MutablePropertyValues createMutablePropertyValues();

    /**
     * <p>See com.volantis.shared.inhibitor.Inhibitor#createImmutable</p>
     */
    public boolean equals(Object other);

    /**
     * <p>See com.volantis.shared.inhibitor.Inhibitor#createImmutable</p>
     */
    public int hashCode();

    /**
     * Returns true if none of the specified, or computed values are set.
     *
     * @return True if this is empty and false otherwise.
     */
    boolean isEmpty();

    /**
     * Get the specified value for the property.
     *
     * <p>This is the value resulting from applying the cascade, if it results
     * in a value then that is the specified value, otherwise the specified
     * value is null.</li>
     *
     * @param property The property whose value should be returned.
     * @return The specified value of the property, may be null.
     */
    StyleValue getSpecifiedValue(StyleProperty property);

    /**
     * Checks whether the property was explicitly specified.
     *
     * <p>A property was explicitly specified if it has a value after the
     * cascade.</p>
     *
     * @return True if the property was explicitly specified, false if it was
     *         not.
     */
    boolean wasExplicitlySpecified(StyleProperty property);

    /**
     * Checks if there are any explicitly specified property values, and
     * returns true if so, and false otherwise.
     *
     * @return true if there are any explicitly specified property values and
     * false otherwise.
     */
    boolean hasExplicitlySpecified();

    /**
     * Get the standard CSS representation of the properties.
     *
     * @return The standard CSS representation of the properties.
     */
    String getStandardCSS();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10730/1	emma	VBM:2005112516 Forward port: Vertical alignment style ignored on panes

 08-Dec-05	10716/1	emma	VBM:2005112516 Vertical alignment style ignored on panes

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
