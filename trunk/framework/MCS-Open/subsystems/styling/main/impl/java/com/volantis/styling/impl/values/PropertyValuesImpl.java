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
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

/**
 * Base implementation of {@link com.volantis.styling.values.PropertyValues}.
 */
abstract class PropertyValuesImpl
        implements PropertyValues {

    /**
     * The definitions of the properties supported by this object.
     */
    protected final StylePropertyDefinitions definitions;

    /**
     * The array, possibly null, of the specified values.
     */
    protected StyleValue[] specifiedValues;

    /**
     * The array, possibly null, of the computed values.
     */
    protected StyleValue[] computedValues;

    /**
     * Copy constructor.
     *
     * @param values    The object to copy.
     * @param copyArray If true then the array from the implementation should
     *                  be copied, otherwise it should be
     */
    protected PropertyValuesImpl(PropertyValues values, boolean copyArray) {
        this.definitions = values.getStylePropertyDefinitions();

        if (values instanceof PropertyValuesImpl) {
            PropertyValuesImpl impl = (PropertyValuesImpl) values;
            if (copyArray) {
                this.specifiedValues = copyArray(impl.specifiedValues);
                this.computedValues = copyArray(impl.computedValues);
            } else {
                this.specifiedValues = impl.specifiedValues;
                this.computedValues = impl.computedValues;
            }
        } else {
            this.specifiedValues = new StyleValue[definitions.count()];
            this.computedValues = new StyleValue[definitions.count()];
            for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
                StyleProperty property = (StyleProperty) i.next();
                final int index = property.getIndex();
                specifiedValues[index] =
                        values.getSpecifiedValue(property);
                computedValues[index] =
                        values.getComputedValue(property);
            }
        }
    }

    /**
     * Protected constructor for sub classes.
     */
    protected PropertyValuesImpl(StylePropertyDefinitions definitions) {
        if (definitions == null) {
            throw new IllegalArgumentException("definitions cannot be null");
        }

        this.definitions = definitions;
    }

    protected StyleValue[] copyArray(StyleValue[] arrayToCopy) {
        if (arrayToCopy == null) {
            return null;
        }

        StyleValue[] array = new StyleValue[arrayToCopy.length];
        System.arraycopy(arrayToCopy, 0, array, 0, arrayToCopy.length);
        return array;
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutablePropertyValues createMutablePropertyValues() {
        return new MutablePropertyValuesImpl(this);
    }

    public IterationAction iterateStyleProperties(StylePropertyIteratee iteratee) {
        return definitions.iterateStyleProperties(iteratee);
    }

    public Iterator stylePropertyIterator() {
        return definitions.stylePropertyIterator();
    }

    public StylePropertyDefinitions getStylePropertyDefinitions() {
        return definitions;
    }

    public MutablePropertyValues createMutableExtendedPropertyValues() {
        return new MutablePropertyValuesImpl(this);
    }

    // Javadoc inherited.
    public StyleValue getSpecifiedValue(StyleProperty property) {
        if (specifiedValues != null) {
            return specifiedValues[property.getIndex()];
        }
        return null;
    }

    public boolean wasExplicitlySpecified(StyleProperty property) {
        return getSpecifiedValue(property) != null;
    }

    // Javadoc inherited.
    public StyleValue getComputedValue(StyleProperty property) {
        if (computedValues != null) {
            return computedValues[property.getIndex()];
        }
        return null;
    }

    // Javadoc inherited.
    public boolean isEmpty() {
        boolean empty = true;
        if (specifiedValues != null || computedValues != null) {
            int count = definitions.count();
            for (int i = 0; empty && i < count; i++) {
                if (specifiedValues != null && specifiedValues[i] != null) {
                    empty = false;
                }
                if (computedValues != null && computedValues[i] != null) {
                    empty = false;
                }
            }
        }
        return empty;
    }

    // Javadoc inherited.
    public String getStandardCSS() {
        StringBuffer buffer = new StringBuffer();
        if (computedValues != null) {
            String separator = "";
            for (int i = 0; i < computedValues.length; i++) {
                StyleValue propertyValue = computedValues[i];
                if (propertyValue != null) {
                    StyleProperty property = definitions.getStyleProperty(i);
                    buffer.append(separator)
                            .append(property.getName())
                            .append(":")
                            .append(propertyValue.getStandardCSS());
                    separator = ";";
                }
            }
        }
        return buffer.toString();
    }

    public String toString() {
        return getStandardCSS();
    }

    // Javadoc inherited.
    public boolean hasExplicitlySpecified() {
        return specifiedValues != null && specifiedValues.length > 0;
    }

    // Javadoc inherited.
    public StyleValue getStyleValue(StyleProperty property) {
        return getComputedValue(property);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10730/1	emma	VBM:2005112516 Forward port: Vertical alignment style ignored on panes

 08-Dec-05	10716/1	emma	VBM:2005112516 Vertical alignment style ignored on panes

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/5	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Corrected issue with styling

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
