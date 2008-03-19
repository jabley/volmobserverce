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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.shared.iteration.IterationAction;

import java.util.Iterator;

/**
 * A collection of style properties.
 */
public interface StyleProperties
        extends PropertyValueIterator, StyleValues {

    /**
     * Return true if this style properties is empty.
     *
     * @return true if empty.
     */
    boolean isEmpty();

    /**
     * Get the property value.
     *
     * @param property The property value to retrieve.
     * @return The property value, or null if it could not be found.
     */
    PropertyValue getPropertyValue(StyleProperty property);

    /**
     * Get the shorthand value.
     *
     * @param shorthand The shorthand whose value is needed.
     *
     * @return The shorthand value, or null if it was not set.
     */
    ShorthandValue getShorthandValue(StyleShorthand shorthand);

    /**
     * Iterate over the contained sequence of {@link ShorthandValue}.
     *
     * <p>This will iterate over the shorthands in a consistent order but the
     * specific order should not be relied upon.</p>
     *
     * @param iteratee The object that will be invoked for each
     * {@link ShorthandValue}.
     */
    IterationAction iterateShorthandValues(ShorthandValueIteratee iteratee);

    /**
     * Get an external iterator over the contained sequence of
     * {@link ShorthandValue}s.
     *
     * @return An iterator over a sequence of {@link ShorthandValue}.
     */
    Iterator shorthandValueIterator();

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

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 11-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
