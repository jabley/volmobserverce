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
import com.volantis.styling.impl.sheet.StyleSheetInternal;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Implementation of {@link MutableStylerList}.
 */
public final class MutableStylerListImpl
        extends StylerListImpl
        implements MutableStylerList {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public MutableStylerListImpl(StylerList value) {
        super(value);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableStylerListImpl() {
    }

    // Javadoc inherited.
    public ListIterator listIterator() {
        return list.listIterator();
    }

    // Javadoc inherited.
    public void append(Styler styler) {
        list.add(styler);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
