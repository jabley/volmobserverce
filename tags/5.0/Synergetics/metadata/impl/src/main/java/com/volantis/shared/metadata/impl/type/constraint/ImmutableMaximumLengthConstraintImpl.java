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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.type.constraint.MaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumLengthConstraint;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

import java.io.Serializable;

/**
 * Implementation of {@link ImmutableMaximumLengthConstraint}.
 */
public final class ImmutableMaximumLengthConstraintImpl
        extends MaximumLengthConstraintImpl
        implements ImmutableMaximumLengthConstraint, Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = -3135476290669554457L;

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    public ImmutableMaximumLengthConstraintImpl(
            final MaximumLengthConstraint constraint) {
        super(constraint);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected ImmutableMaximumLengthConstraintImpl() {
        super();
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

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.IMMUTABLE_MAXIMUM_LENGTH_CONSTRAINT;
    }
}
