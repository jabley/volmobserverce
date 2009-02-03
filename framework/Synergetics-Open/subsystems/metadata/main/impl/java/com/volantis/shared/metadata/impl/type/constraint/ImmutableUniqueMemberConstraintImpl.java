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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.io.Serializable;

/**
 * Implementation of {@link ImmutableUniqueMemberConstraint}.
 */
public final class ImmutableUniqueMemberConstraintImpl
        extends UniqueMemberConstraintImpl
        implements ImmutableUniqueMemberConstraint, Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = -8399814579974132151L;

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    public ImmutableUniqueMemberConstraintImpl(UniqueMemberConstraint constraint) {
        super(constraint);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableUniqueMemberConstraintImpl() {
    }

    /**
     * Override to return this object rather than create a new one.
     *
     * <p>This is simply a performance optimisation and has no impact on the
     * behaviour.</p>
     */
    public ImmutableInhibitor createImmutable() {
        return this;
    }

    /**
     * Serializer for JiBX.
     *
     * @note Added for JiBX only.
     */
    public static String serializer(
            final ImmutableUniqueMemberConstraint constraint) {
        return "";
    }

    /**
     * Deserializer for JiBX.
     *
     * @note Added for JiBX only.
     */
    public static ImmutableUniqueMemberConstraint deserializer(
            final String constraint) {
        return
            constraint == null? null: new ImmutableUniqueMemberConstraintImpl();
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_UNIQUE_MEMEBER_CONSTRAINT;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
