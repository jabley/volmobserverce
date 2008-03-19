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

import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIteratee;
import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.shared.iteration.IterationAction;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Base implementation of {@link StylerList}.
 */
abstract class StylerListImpl
        implements StylerList {

    /**
     * The underlying list of {@link Styler}s.
     */
    List list;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StylerListImpl(StylerList value) {
        list = new ArrayList();
        Iterator iterator = value.iterator();
        while (iterator.hasNext()) {
            Styler styler = (Styler) iterator.next();
            list.add(styler);
        }
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StylerListImpl() {
        list = new ArrayList();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableStylerList createImmutableStylerList() {
        return new ImmutableStylerListImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableStylerList createMutableStylerList() {
        return new MutableStylerListImpl(this);
    }

    // Javadoc inherited.
    public Iterator iterator() {
        // todo make this safe, wrap it so that remove does not work.
        return list.iterator();
    }

    // Javadoc inherited.
    public IterationAction iterate(StylerIteratee iteratee) {
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Styler styler = (Styler) iterator.next();
            IterationAction action = iteratee.next(styler);
            if (action == IterationAction.BREAK) {
                return action;
            }
        }

        return IterationAction.CONTINUE;
    }

    public void debug(DebugStylingWriter writer) {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Styler styler = (Styler) iterator.next();
            writer.print(styler).newline();
        }
    }
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
