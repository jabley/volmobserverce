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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.type.ListType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumLengthConstraint;
import com.volantis.shared.metadata.value.ListValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of {@link ListType}.
 */
abstract class ListTypeImpl extends CollectionTypeImpl implements ListType {

    /**
     * The unique member constraint associated with this type.
     */
    private ImmutableUniqueMemberConstraint uniqueMemberConstraint;

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    protected ListTypeImpl(ListType type) {
        super(type);
        uniqueMemberConstraint = type.getUniqueMemberConstraint();
    }

    /**
     * Protected constructor for future use by JDO.
     */
    protected ListTypeImpl() {
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableListTypeImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableListTypeImpl(this);
    }


    // Javadoc inherited.
    public ImmutableUniqueMemberConstraint getUniqueMemberConstraint() {
        return uniqueMemberConstraint;
    }

    /**
     * Implementation of mutator declared in
     * {@link com.volantis.shared.metadata.type.mutable.MutableListType}.
     *
     * Set the unique member constraint associated with this type.
     *
     * <p>This object stores an immutable instance of the supplied object.</p>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param constraint The unique member constraint, may be null.
     *
     */
    public void setUniqueMemberConstraint(UniqueMemberConstraint constraint) {
        setUniqueMemberConstraint(
            (ImmutableUniqueMemberConstraint) constraint.createImmutable());
    }

    /**
     * Sets a new unique member constraint to list type.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new constraint
     */
    public void setUniqueMemberConstraint(
            final ImmutableUniqueMemberConstraint constraint) {
        uniqueMemberConstraint = constraint;
    }

    // Javadoc inherited.
    public int hashCode() {
        return 13 * super.hashCode()
                + MetaDataHelper.hashCode(uniqueMemberConstraint);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof ListType) &&
            equalsListType((ListType) other);
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>ListType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>ListType</code> are equal to this one.
     */
    protected boolean equalsListType(ListType other) {
        return equalsCollectionType(other)
                && MetaDataHelper.equals(uniqueMemberConstraint,
                        other.getUniqueMemberConstraint());
    }

    // javadoc inherited
    protected Class getExpectedValueType() {
        return ListValue.class;
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        if (value instanceof ListValue) {
            // check unique member constraint
            if (uniqueMemberConstraint != null) {
                final ListValue list = (ListValue) value;
                final Set values = new HashSet();
                final Iterator iter = list.getContentsAsList().iterator();
                for (int i = 1; iter.hasNext(); i++) {
                    final MetaDataValue child = (MetaDataValue) iter.next();
                    if (!values.add(child)) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION,
                            path + "[" + i + "]", child, uniqueMemberConstraint,
                            "Unique member constraint violation."));
                    }
                }
            }
        }
        return errors;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself and ignore it
        visitor.getNextEntry();
        setMemberTypeConstraint(
            (ImmutableMemberTypeConstraint) visitor.getNextAsInhibitor());
        setMaximumLengthConstraint((ImmutableMaximumLengthConstraint)
            visitor.getNextAsInhibitor());
        setMinimumLengthConstraint((ImmutableMinimumLengthConstraint)
            visitor.getNextAsInhibitor());
        setMemberTypeConstraint((ImmutableMemberTypeConstraint)
            visitor.getNextAsInhibitor());
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == getMemberTypeConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getMemberTypeConstraint())
                .visitInhibitor(visitor);
        }
        if (null == getMaximumLengthConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getMaximumLengthConstraint())
                .visitInhibitor(visitor);
        }
        if (null == getMinimumLengthConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getMinimumLengthConstraint())
                .visitInhibitor(visitor);
        }
        if (null == getMemberTypeConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getMemberTypeConstraint())
                .visitInhibitor(visitor);
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

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
