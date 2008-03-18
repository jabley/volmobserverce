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

import com.volantis.shared.metadata.type.constraint.MemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMemberTypeConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link MutableMemberTypeConstraint}.
 */
final class MutableMemberTypeConstraintImpl extends MemberTypeConstraintImpl
        implements MutableMemberTypeConstraint {

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    public MutableMemberTypeConstraintImpl(MemberTypeConstraint constraint) {
        super(constraint);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableMemberTypeConstraintImpl() {
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_MEMBERTYPE_CONSTRAINT;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
