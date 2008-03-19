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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.ShorthandValueIteratee;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;

/**
 * This purpose of this convenience class is to allow existing code which
 * requires {@link StyleProperties} to work with the new styling framework
 * which requires {@link PropertyValues}.
 * <p/>
 * It will no longer be required and should be removed when all code is ported
 * to use PropertyValues.
 */
public class StylePropertiesConvertor {

    /**
     * Wraps a PropertyValues in a StyleProperties.
     *
     * <p>Changes to the underlying property values will affect the values
     * returned from the style properties.</p>
     *
     * @param propertyValues the PropertyValues to wrap into a StyleProperties
     * @return StyleProperties that wraps the properties.
     */
    public static StyleProperties wrap(
            final PropertyValues propertyValues) {

        return new StyleProperties() {

            // Javadoc inherited.
            public boolean isEmpty() {
                return propertyValues.isEmpty();
            }

            // Javadoc inherited.
            public StyleValue getStyleValue(StyleProperty property) {
                return propertyValues.getComputedValue(property);
            }

            public PropertyValue getPropertyValue(StyleProperty property) {
                throw new UnsupportedOperationException();
            }

            public IterationAction iteratePropertyValues(
                    PropertyValueIteratee iteratee) {
                throw new UnsupportedOperationException();
            }

            public Iterator propertyValueIterator() {
                throw new UnsupportedOperationException();
            }

            public ShorthandValue getShorthandValue(StyleShorthand shorthand) {
                throw new UnsupportedOperationException();
            }

            public IterationAction iterateShorthandValues(
                    ShorthandValueIteratee iteratee) {
                throw new UnsupportedOperationException();
            }

            public Iterator shorthandValueIterator() {
                throw new UnsupportedOperationException();
            }

            public String getStandardCSS() {
                throw new UnsupportedOperationException();
            }
        };
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/3	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 ===========================================================================
*/
