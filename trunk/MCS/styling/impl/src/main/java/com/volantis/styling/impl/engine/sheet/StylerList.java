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

package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIteratee;
import com.volantis.styling.impl.sheet.StylerIterator;
import com.volantis.styling.debug.Debuggable;

import java.util.Iterator;

/**
 * Container of {@link Styler}.
 *
 * <p>This uses the inhibitor pattern.</p>
 *
 * @mock.generate
 */
public interface StylerList
    extends StylerIterator, Debuggable {

    /**
     * Create an immutable {@link StylerList}.
     *
     * @return An immutable object.
     */
    ImmutableStylerList createImmutableStylerList();

    /**
     * Create a mutable {@link StylerList}.
     *
     * @return A mutable object.
     */
    MutableStylerList createMutableStylerList();

    /**
     * Return an iterator over the list of {@link Styler}s
     *
     * @return An iterator over the list of {@link Styler}s.
     */
    Iterator iterator();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
