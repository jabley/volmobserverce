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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleValue;

/**
 * Instances of this are responsible for handling a single value from within
 * a pair of values.
 */
public interface PairComponentValueHandler {

    /**
     * Convert the next value in the iterator into a valid value for a
     * component of a pair.
     * <p/>
     * <p>If the value can be converted then this may or may not consume the
     * value depending on the implementation.</p>
     *
     * @param context  The context within which the conversion is being
     *                 performed.
     * @param iterator The iterator over the sequence of values.
     * @return The converted value, or null if it could not be converted.
     */
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator);

    /**
     * Get the initial value.
     *
     * @param other The value of the other component of the pair. Typically
     *              this is null for the first component but some pairs do not
     *              care about the ordering in which case this may be non null
     *              for the first component.
     *
     * @return The initial value, or null.
     */
    StyleValue initial(StyleValue other);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
