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

package com.volantis.mcs.policies.impl;

/**
 * Base for those class hierarchies that support equality.
 */
public class EqualsHashCodeBase
        extends EqualsHashCodeHelper {

    // Javadoc inherited.
    public final boolean equals(Object obj) {
        // Check for sameness first.
        if (obj == this) {
            return true;
        }

        // Then try and cast it and compare.
        return castThenCheckEquality(obj);
    }

    /**
     * Casts the supplied object to an instance of current class (if it can)
     * and then invokes a type safe method to perform the equality check.
     *
     * <p>If the object is not an instance of the current class then this
     * returns false.</p>
     *
     * <p>Derived classes must override this to cast the object to an instance
     * of the derived class (if it can) and then invoke a type safe method
     * to perform the equality check. The type safe method should first invoke
     * the type safe method in the parent and if that works only then perform
     * its own type specific checks. Derived classes must not call
     * <code>super.castThenCheckEquality(obj)</code> as that would unnecessarily
     * check that the object was an instance of the base class, which it always
     * will be if it is an instance of a derived class.</p>
     *
     * @param obj The object to try and cast and check for equality.
     * @return True if the object is equal
     */
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof EqualsHashCodeBase) ?
                equalsSpecific((EqualsHashCodeBase) obj) : false;
    }

    /**
     * Check the supplied object for equality.
     * @param base The supplied object.
     * @return True.
     */
    protected boolean equalsBase(EqualsHashCodeBase base) {
        return true;
    }

    /**
     * Check the supplied object for equality.
     * @param base The supplied object.
     * @return True.
     */
    protected boolean equalsSpecific(EqualsHashCodeBase base) {
        return true;
    }

    // Javadoc inherited.
    public int hashCode() {
        return 0;
    }
}
