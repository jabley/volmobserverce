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
 * A constraint on the maximum value of the size of an iterator format.
 *
 * @mock.generate
 */
public interface IteratorSizeConstraint {

    /**
     * Given a value that may exceed the constraint return the maximum value
     * that does not.
     *
     * @param value The value to be constrained.
     *
     * @return The constrained value.
     */
    int getConstrained(int value);

    /**
     * Check whether the constraint is fixed.
     *
     * @return True if the constraint is fixed.
     */
    boolean isFixed();

    /**
     * Get the fixed value of the constraint.
     *
     * @return The fixed value of the constraint.
     */
    int getMaximumValue();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
