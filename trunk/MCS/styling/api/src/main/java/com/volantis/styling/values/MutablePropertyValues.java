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
import com.volantis.styling.properties.StyleProperty;

/**
 * A mutable {@link PropertyValues}.
 *
 * @mock.generate base="PropertyValues"
 */
public interface MutablePropertyValues
        extends PropertyValues {

    /**
     * Clear all the values.
     */
    void clear();

    /**
     * Set the value that was computed.
     *
     * @param property The property whose value is to be set.
     * @param value The value of the property.
     */
    void setComputedValue(StyleProperty property, StyleValue value);

    /**
     * Set the computed and specified value.
     *
     * <p>Use this instead of {@link #setComputedValue} if the value must be
     * written out to the target CSS even if it could be inferred from either
     * the parent or initial values. i.e. if the target device has, or may have
     * rules that explicitly set the property to a value other than the one
     * specified.</p>
     *
     * <p>This should not be used for the MCS specific properties that are not
     * used to generate the output CSS.</p>
     *
     * @param property The property whose value is to be set.
     * @param value The value of the property.
     */
    void setComputedAndSpecifiedValue(StyleProperty property, StyleValue value);

    /**
     * Clear the property value for the property.
     *
     * @param property The property to clear.
     */
    void clearPropertyValue(StyleProperty property);

    /**
     * Override the property (both specified and computed) if the property
     * was not explicitly specified and did not match the supplied value.
     *
     * @param property The property that may be overridden.
     * @param value The value to override.
     */
    void overrideUnlessExplicitlySpecified(
            StyleProperty property, StyleValue value);

    /**
     * The property is treated as being unspecified and so will be cleared if
     * it can be inferred even if the device information is incomplete.
     *
     * @param property The property to mark.
     */
    void markAsUnspecified(StyleProperty property);

    /**
     * The property is excluded from the generated CSS although it may still
     * be used in determining whether a property is inferred.
     *
     * @param property The property to exclude.
     */
    void excludeFromCSS(StyleProperty property);

    /**
     * Check whether the property should be excluded from the generated CSS.
     *
     * @param property The property to exclude.
     * @return True if it should, false otherwise.
     */
    boolean shouldExcludeFromCSS(StyleProperty property);

    /**
     * Set the value that was specified.
     *
     * @param property The property whose value is to be set.
     * @param value    The value of thxe property.
     */
    void setSpecifiedValue(StyleProperty property, StyleValue value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
