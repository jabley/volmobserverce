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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.type.constraint;

import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumLengthConstraint;

/**
 * An object for creating instances of {@link Constraint} and related
 * classes.
 * 
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User extensions of this class are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class ConstraintFactory {

    /**
     * Create a new instance of {@link MutableMemberTypeConstraint}.
     *
     * @return A new instance of {@link MutableMemberTypeConstraint}.
     */
    public abstract MutableMemberTypeConstraint createMemberTypeConstraint();

    /**
     * Create a new instance of {@link MutableEnumeratedConstraint}.
     *
     * @return A new instance of {@link MutableEnumeratedConstraint}.
     */
    public abstract MutableEnumeratedConstraint createEnumeratedConstraint();

    /**
     * Create a new instance of {@link MutableMaximumValueConstraint}.
     *
     * @return A new instance of {@link MutableMaximumValueConstraint}.
     */
    public abstract MutableMaximumValueConstraint createMaximumValueConstraint();

    /**
     * Create a new instance of {@link MutableMinimumValueConstraint}.
     *
     * @return A new instance of {@link MutableMinimumValueConstraint}.
     */
    public abstract MutableMinimumValueConstraint createMinimumValueConstraint();

    /**
     * Create a new instance of {@link MutableMinimumLengthConstraint}.
     *
     * @return A new instance of {@link MutableMinimumLengthConstraint}.
     */
    public abstract MutableMinimumLengthConstraint createMinimumLengthConstraint();

    /**
     * Create a new instance of {@link MutableMaximumLengthConstraint}.
     *
     * @return A new instance of {@link MutableMaximumLengthConstraint}.
     */
    public abstract MutableMaximumLengthConstraint createMaximumLengthConstraint();

    /**
     * Get one of the predefined number sub type constraints.
     *
     * <p>The returned object is immutable.</p>
     *
     * @param numberSubType The number sub type to which the constraint should
     * restrict values.
     *
     * @return A predefined {@link NumberSubTypeConstraint}.
     * @throws IllegalArgumentException if the <code>Class</code> is not a sub type of
     *         {@link Number}.
     */
    public abstract
    ImmutableNumberSubTypeConstraint getNumberSubTypeConstraint(
            Class numberSubType);

    /**
     * Get a unique member constraint.
     *
     * <p>All calls to this method may return the same instance.</p>
     *
     * @return A unique member constraint.
     */
    public abstract
    ImmutableUniqueMemberConstraint getUniqueMemberConstraint();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
