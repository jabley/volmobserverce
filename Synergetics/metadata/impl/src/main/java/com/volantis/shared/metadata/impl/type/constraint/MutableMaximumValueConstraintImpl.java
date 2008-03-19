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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.metadata.type.constraint.MaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumValueConstraint}.
 */
final class MutableMaximumValueConstraintImpl
        extends MaximumValueConstraintImpl
        implements MutableMaximumValueConstraint {

    /**
     * Copy constructor.
     *
     * @param contraint The object to copy.
     */
    public MutableMaximumValueConstraintImpl(MaximumValueConstraint contraint) {
        super(contraint);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableMaximumValueConstraintImpl() {
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_MAXIMUM_VALUE_CONSTRAINT;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/5	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
