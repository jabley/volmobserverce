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

import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.themes.StyleValue;

/**
 * An iterator over a sequence of style values.
 */
public interface StyleValueIterator {

    /**
     * Get the current style value.
     *
     * @return The style value.
     */
    StyleValue value();

    /**
     * Make sure that the current value is the specified separator.
     *
     * @param separator The expected separator.
     */
    void separator(Separator separator);

    /**
     * Is the next item in the sequence the specified separator.
     *
     * @param separator The expected separator.
     *
     * @return True if it is the separator and false if it is not.
     */
    boolean isSeparator(Separator separator);

    /**
     * Move onto the next style value.
     */
    void consume();

    /**
     * Get the number of values remaining with the iterator.
     *
     * @return The number of values remaining in the iterator.
     */
    int remaining();

    /**
     * Check to see if there are any more values left to iterator over.
     *
     * @return True if the iterator has more and false if it does not.
     */
    boolean hasMore();
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
