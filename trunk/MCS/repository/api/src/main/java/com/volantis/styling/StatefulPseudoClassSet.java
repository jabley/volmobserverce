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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling;

/**
 * A set of {@link StatefulPseudoClass}.
 *
 * <p>This has the following characteristics:</p>
 * <ul>
 * <li>Order of combination is irrelevant.</li>
 * <li>Some pseudo classes cannot be combined.</li>
 * </ul>
 *
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface StatefulPseudoClassSet
        extends PseudoStyleEntity {

    /**
     * Create a new set that is the union of this set and the set containing
     * the single {@link StatefulPseudoClass}.
     *
     * @param pseudoClass The {@link StatefulPseudoClass}
     *
     * @return The result of combining.
     */
    StatefulPseudoClassSet add(StatefulPseudoClass pseudoClass);

    /**
     * Checks to see if this is a subset of the other, i.e. does this contain
     * all the properties that are in the other.
     *
     * @param other The other set to check.
     * @return True if it is, false if it is not.
     */
    boolean isSubSetOf(StatefulPseudoClassSet other);

    /**
     * Combine this set with another set to create a new one.
     *
     * @param other The other set with which this is to be combined.
     * @return The composite set.
     * @throws IllegalArgumentException If the two sets are mutually exclusive.
     */
    StatefulPseudoClassSet combine(StatefulPseudoClassSet other);

    /**
     * Iterate over the contained {@link StatefulPseudoClass}es.
     *
     * @param iteratee will be called back for each nested
     * {@link StatefulPseudoClass}.
     */
    void iterate(StatefulPseudoClassIteratee iteratee);
}
