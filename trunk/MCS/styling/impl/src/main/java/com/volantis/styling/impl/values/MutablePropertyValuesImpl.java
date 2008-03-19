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

package com.volantis.styling.impl.values;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.Arrays;

/**
 * Implementation of {@link com.volantis.styling.values.MutablePropertyValues}.
 */
public final class MutablePropertyValuesImpl
        extends PropertyValuesImpl
        implements MutablePropertyValues {

    /**
     * Indicates whether the underlying array has been shared with another
     * property values object.
     */
    private boolean sharedSpecifiedArray;

    /**
     * Indicates whether the underlying array has been shared with another
     * property values object.
     */
    private boolean sharedComputedArray;

    /**
     * The set of excluded properties.
     */
    private MutableStylePropertySet excludedProperties;

    /**
     * Copy constructor.
     *
     * @param values The object to copy.
     */
    public MutablePropertyValuesImpl(PropertyValues values) {
        super(values, true);

        if (values instanceof MutablePropertyValuesImpl) {
            MutablePropertyValuesImpl other =
                    (MutablePropertyValuesImpl) values;
            if (other.excludedProperties != null) {
                this.excludedProperties = new MutableStylePropertySetImpl(
                        other.excludedProperties);
            }
        }
    }

    /**
     * Public constructor for use by factory.
     */
    public MutablePropertyValuesImpl(StylePropertyDefinitions definitions) {
        super(definitions);
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutablePropertyValues createImmutablePropertyValues() {
        return createImmutableExtendedPropertyValues();
    }

    // Javadoc inherited.
    public void setSpecifiedValue(StyleProperty property, StyleValue value) {
        if (specifiedValues == null) {
            specifiedValues = new StyleValue[definitions.count()];
        } else if (sharedSpecifiedArray) {
            specifiedValues = copyArray(specifiedValues);
            sharedSpecifiedArray = false;
        }
        specifiedValues[property.getIndex()] = value;
    }

    // Javadoc inherited.
    public void setComputedValue(StyleProperty property, StyleValue value) {
        if (computedValues == null) {
            computedValues = new StyleValue[definitions.count()];
        } else if (sharedComputedArray) {
            computedValues = copyArray(computedValues);
            sharedComputedArray = false;
        }
        computedValues[property.getIndex()] = value;
    }

    // Javadoc inherited.
    public void setComputedAndSpecifiedValue(
            StyleProperty property, StyleValue value) {
        setSpecifiedValue(property, value);
        setComputedValue(property, value);
    }

    public void clearPropertyValue(StyleProperty property) {
        setSpecifiedValue(property, null);
        setComputedValue(property, null);
    }

    // Javadoc inherited.
    public void overrideUnlessExplicitlySpecified(
            StyleProperty property, StyleValue value) {

        if (!wasExplicitlySpecified(property)) {
            StyleValue actual = getComputedValue(property);
            if (actual == null ? value != null : !actual.equals(value)) {
                setComputedAndSpecifiedValue(property, value);
            }
        }
    }

    // Javadoc inherited.
    public void markAsUnspecified(StyleProperty property) {
        setSpecifiedValue(property, null);
    }

    // Javadoc inherited.
    public void excludeFromCSS(StyleProperty property) {
        if (excludedProperties == null) {
            excludedProperties = new MutableStylePropertySetImpl();
        }
        excludedProperties.add(property);
    }

    // Javadoc inherited.
    public boolean shouldExcludeFromCSS(StyleProperty property) {
        if (excludedProperties == null) {
            return false;
        } else {
            return excludedProperties.contains(property);
        }
    }

    // Javadoc inherited.
    public void clear() {
        if (specifiedValues != null) {
            if (sharedSpecifiedArray) {
                specifiedValues = null;
            } else {
                Arrays.fill(specifiedValues, null);
            }
        }
        if (computedValues != null) {
            if (sharedComputedArray) {
                computedValues = null;
            } else {
                Arrays.fill(computedValues, null);
            }
        }
    }

    public ImmutablePropertyValues createImmutableExtendedPropertyValues() {
        ImmutablePropertyValuesImpl values =
                new ImmutablePropertyValuesImpl(this);

        // Remember that the array is shared with the immutable values.
        if (specifiedValues != null) {
            sharedSpecifiedArray = true;
        }
        if (computedValues != null) {
            sharedComputedArray = true;
        }

        return values;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/5	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 22-Aug-05	9184/4	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
