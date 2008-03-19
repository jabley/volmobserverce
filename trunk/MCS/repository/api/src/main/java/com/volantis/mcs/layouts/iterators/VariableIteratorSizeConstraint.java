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

package com.volantis.mcs.layouts.iterators;

/**
 * Constrains the number of cells to be less than or equal to a fixed value.
 */
public class VariableIteratorSizeConstraint
        extends AbstractIteratorSizeConstraint {

    /**
     * A constraint that does not restrict the size at all.
     */
    public static final IteratorSizeConstraint NO_LIMIT =
            new VariableIteratorSizeConstraint(Integer.MAX_VALUE);

    /**
     * Initialise.
     *
     * @param maxInclusive The maximum number of cells (inclusive).
     */
    public VariableIteratorSizeConstraint(int maxInclusive) {
        super(maxInclusive);
    }

    // Javadoc inherited.
    public boolean isFixed() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
