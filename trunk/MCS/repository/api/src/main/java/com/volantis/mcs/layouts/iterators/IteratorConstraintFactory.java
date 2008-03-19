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
 * Creates appropriate constraints on iterator properties.
 *
 * @mock.generate
 */
public class IteratorConstraintFactory {

    /**
     * Create a constraint on the size of an iterator.
     *
     * @param count The maximum number of instances, if this is 0 then the
     * maximum is unlimited.
     *
     * @param fixed Determines whether the size is fixed, or dependent on the
     * actual number of instances created within an instance of the iterator.
     *
     * @return The constraint on the iterator size.
     */
    public IteratorSizeConstraint createConstraint(int count, boolean fixed) {
        if (count == 0) {
            return VariableIteratorSizeConstraint.NO_LIMIT;
        } else if (fixed) {
            return new FixedIteratorSizeConstraint(count);
        } else {
            return new VariableIteratorSizeConstraint(count);
        }
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
