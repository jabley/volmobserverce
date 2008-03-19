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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.type.SimpleType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.EnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableEnumeratedConstraint;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.SimpleType}.
 */
abstract class SimpleTypeImpl extends MetaDataTypeImpl implements SimpleType {

    /**
     * The constraint for this object
     */
    private ImmutableEnumeratedConstraint constraint;

    /**
     * Protected constructor for sub classes.
     */
    protected SimpleTypeImpl() {
    }

    /**
     * Copy constructor
     * @param type
     */
    protected SimpleTypeImpl(SimpleType type) {
        this.constraint = type.getEnumeratedConstraint();
    }

    // Javadoc inherited
    public ImmutableEnumeratedConstraint getEnumeratedConstraint() {
        return constraint;
    }

    /**
     * Implementation of mutator.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     */
    public void setEnumeratedConstraint(EnumeratedConstraint constraint) {
        if (constraint != null) {
            setEnumeratedConstraint(
                (ImmutableEnumeratedConstraint) constraint.createImmutable());
        } else {
            this.constraint = null;
        }
    }

    /**
     * Sets the constraint to an immutable enumerated constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new enumerated constraint
     */
    public void setEnumeratedConstraint(
            final ImmutableEnumeratedConstraint constraint) {
        this.constraint = constraint;
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof SimpleType) &&
            equalsSimpleType((SimpleType) other);
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>SimpleType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>SimpleType</code> are equal to this one.
     */
    protected boolean equalsSimpleType(SimpleType other) {
        return MetaDataHelper.equals(constraint,
                other.getEnumeratedConstraint());
    }

    // Javadoc inherited.
    public int hashCode() {
        return 3 + MetaDataHelper.hashCode(constraint);
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        // if there is an enumeration constraint, check if the value is among
        // the accepted values
        if (value != null && constraint != null) {
            final List enumeratedValues = constraint.getEnumeratedValues();
            boolean found = false;
            for (Iterator iter = enumeratedValues.iterator();
                 iter.hasNext() && !found; ) {
                if (iter.next().equals(value)) {
                    found = true;
                }
            }
            if (!found) {
                final VerificationError error = new VerificationError(
                    VerificationError.TYPE_CONSTRAINT_VIOLATION, path, value,
                    constraint, "Enumeration constraint violation: Value " +
                    value.getAsString() + " is not among the accepted values.");
                errors.add(error);
            }
        }
        return errors;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself and ignore it
        visitor.getNextEntry();
        constraint = (ImmutableEnumeratedConstraint)visitor.getNextAsInhibitor();
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == constraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl)constraint).visitInhibitor(visitor);
        }
        visitor.pop();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
