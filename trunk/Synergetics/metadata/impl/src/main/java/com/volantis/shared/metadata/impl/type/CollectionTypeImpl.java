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
import com.volantis.shared.metadata.type.CollectionType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.MemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.MinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.MaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumLengthConstraint;
import com.volantis.shared.metadata.value.CollectionValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implementation of {@link CollectionType}.
 */
abstract class CollectionTypeImpl
        extends CompositeTypeImpl
        implements CollectionType {

    /**
     * The member type constraint for this object.
     */
    private ImmutableMemberTypeConstraint memberTypeConstraint;

    /**
     * The minimum length constraint for this object.
     */
    private ImmutableMinimumLengthConstraint minimumLengthConstraint;

    /**
     * The maximum length constraint for this object.
     */
    private ImmutableMaximumLengthConstraint maximumLengthConstraint;

    /**
     * Protected constructor for future use by JDO.
     */
    protected CollectionTypeImpl() {
    }

    /**
     * Copy constructor.
     * @param type The object to copy.
     */
    protected CollectionTypeImpl(CollectionType type) {
        memberTypeConstraint = type.getMemberTypeConstraint();
        minimumLengthConstraint = type.getMinimumLengthConstraint();
        maximumLengthConstraint = type.getMaximumLengthConstraint();
    }

    // Javadoc inherited.
    public ImmutableMemberTypeConstraint getMemberTypeConstraint() {
        return memberTypeConstraint;
    }

    // Javadoc inherited.
    public ImmutableMinimumLengthConstraint getMinimumLengthConstraint() {
        return minimumLengthConstraint;
    }

    // Javadoc inherited.
    public ImmutableMaximumLengthConstraint getMaximumLengthConstraint() {
        return maximumLengthConstraint;
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
    public void setMemberTypeConstraint(MemberTypeConstraint constraint) {
        setMemberTypeConstraint((ImmutableMemberTypeConstraint)
            MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable member type constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new member type constraint
     */
    public void setMemberTypeConstraint(
            final ImmutableMemberTypeConstraint constraint) {
        memberTypeConstraint = constraint;
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
    public void setMinimumLengthConstraint(
            final MinimumLengthConstraint constraint) {
        setMinimumLengthConstraint((ImmutableMinimumLengthConstraint)
            MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable minimum length constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new minimum length constraint
     */
    public void setMinimumLengthConstraint(
            final ImmutableMinimumLengthConstraint constraint) {
        minimumLengthConstraint = constraint;
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
    public void setMaximumLengthConstraint(
            final MaximumLengthConstraint constraint) {
        setMaximumLengthConstraint((ImmutableMaximumLengthConstraint)
            MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable maximum length constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new minimum length constraint
     */
    public void setMaximumLengthConstraint(
            final ImmutableMaximumLengthConstraint constraint) {
        maximumLengthConstraint = constraint;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = MetaDataHelper.hashCode(memberTypeConstraint);
        result = 29 * result + MetaDataHelper.hashCode(minimumLengthConstraint);
        return 29 * result + MetaDataHelper.hashCode(maximumLengthConstraint);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof CollectionType) &&
            equalsCollectionType((CollectionType) other);
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>CollectionType/code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>CollectionType</code> are equal to this one.
     */
    protected boolean equalsCollectionType(CollectionType other) {
        return MetaDataHelper.equals(memberTypeConstraint,
                other.getMemberTypeConstraint()) &&
            MetaDataHelper.equals(minimumLengthConstraint,
                other.getMinimumLengthConstraint()) &&
            MetaDataHelper.equals(maximumLengthConstraint,
                other.getMaximumLengthConstraint());
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors =  super.verify(value, path);
        if (value instanceof CollectionValue) {
            final CollectionValue collectionValue = (CollectionValue) value;
            final Collection contents =
                collectionValue.getContentsAsCollection();
            // check member type constraint
            if (memberTypeConstraint != null) {
                final MetaDataTypeImpl memberType =
                    (MetaDataTypeImpl) memberTypeConstraint.getMemberType();
                final Iterator iter = contents.iterator();
                for (int i = 1; iter.hasNext(); i++) {
                    final MetaDataValue child = (MetaDataValue) iter.next();
                    final String childPath = path + "[" + i + "]";
                    final Collection childErrors =
                        memberType.verify(child, childPath);
                    if (childErrors.size() > 0) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION,
                            childPath, child, memberTypeConstraint,
                            "Member type constraint violation."));
                        errors.addAll(childErrors);
                    }
                }
            }
            // check minimum length constraint
            if (minimumLengthConstraint != null) {
                if (contents.size() < minimumLengthConstraint.getLimit()) {
                    errors.add(new VerificationError(
                        VerificationError.TYPE_CONSTRAINT_VIOLATION, path, value,
                        minimumLengthConstraint, "Minimum length constraint " +
                            "violation. Minimum length is " +
                            minimumLengthConstraint.getLimit() +
                            ", but this collection has only " + contents.size() +
                            " elements."));
                }
            }
            // check minimum length constraint
            if (maximumLengthConstraint != null) {
                if (contents.size() > maximumLengthConstraint.getLimit()) {
                    errors.add(new VerificationError(
                        VerificationError.TYPE_CONSTRAINT_VIOLATION, path, value,
                        maximumLengthConstraint, "Maximum length constraint " +
                            "violation. Maximum length is " +
                            maximumLengthConstraint.getLimit() +
                            ", but this collection has " + contents.size() +
                            " elements."));
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
        visitor.pop();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/5	tom	VBM:2004122401 Changed Javadoc

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
