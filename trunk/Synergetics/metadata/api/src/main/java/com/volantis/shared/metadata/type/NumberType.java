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

package com.volantis.shared.metadata.type;

import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumValueConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A number type.
 *
 * <p>This type represents any type of number supported within Java, e.g.
 * {@link BigDecimal}, {@link BigInteger}, {@link Long},
 * ... Most uses of this will want to restrict the allowable values to a much
 * smaller set, e.g. just those values that are representable using an
 * {@link Integer}. To do that simply set a {@link NumberSubTypeConstraint}
 * with the number sub type set to <code>Integer.class</code>.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface NumberType
        extends SimpleType {

    /**
     * Get the constraint on the subtype of {@link Number}.
     *
     * @return The constraint on the subtype of {@link Number}, may be null.
     */
    public ImmutableNumberSubTypeConstraint getNumberSubTypeConstraint();

    /**
     * Get the constraint on the minimum value of this type.
     *
     * @return The constraint on the minimum value of this type, may be null.
     */
    public ImmutableMinimumValueConstraint getMinimumValueConstraint();

    /**
     * Get the constraint on the maximum value of this type.
     *
     * @return The constraint on the maximum value of this type, may be null.
     */
    public ImmutableMaximumValueConstraint getMaximumValueConstraint();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
