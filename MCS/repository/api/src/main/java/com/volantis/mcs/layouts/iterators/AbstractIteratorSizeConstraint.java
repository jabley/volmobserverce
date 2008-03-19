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
 * Base for those classes that constrain the size of iterators.
 */
public abstract class AbstractIteratorSizeConstraint
        implements IteratorSizeConstraint {
    /**
     * The maximum number of cells (inclusive).
     */
    protected final int maxInclusive;

    /**
     * Initialise.
     *
     * @param maxInclusive The maximum number of cells (inclusive).
     */
    protected AbstractIteratorSizeConstraint(int maxInclusive) {
        if (maxInclusive <= 0) {
            throw new IllegalArgumentException(
                    "maxInclusive of " + maxInclusive +
                    " is invalid, must be > 0");
        }
        this.maxInclusive = maxInclusive;
    }

    // Javadoc inherited.
    public int getMaximumValue() {
        return maxInclusive;
    }

    // Javadoc inherited.
    public int getConstrained(int value) {
        return Math.min(maxInclusive, value);
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final AbstractIteratorSizeConstraint constraint =
            (AbstractIteratorSizeConstraint) other;

        return (maxInclusive == constraint.maxInclusive) &&
            (isFixed() == constraint.isFixed());
    }

    // javadoc inherited
    public int hashCode() {
        return (maxInclusive << 1) + (isFixed() ? 0 : 1);
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
