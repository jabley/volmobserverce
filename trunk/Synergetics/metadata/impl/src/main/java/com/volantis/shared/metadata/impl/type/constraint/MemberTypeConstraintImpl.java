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
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.MetaDataType;
import com.volantis.shared.metadata.type.constraint.MemberTypeConstraint;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;

/**
 * Implementation of {@link MemberTypeConstraint}.
 */
abstract class MemberTypeConstraintImpl extends ConstraintImpl
        implements MemberTypeConstraint {

    /**
     * The member type for this object.
     */
    private ImmutableMetaDataType memberType;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected MemberTypeConstraintImpl(MemberTypeConstraint value) {
        this.memberType = value.getMemberType();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected MemberTypeConstraintImpl() {
    }

    // Javadoc inherited.
    public ImmutableMetaDataType getMemberType() {
        return memberType;
    }

    /**
     * Set the {@link MetaDataType} of the members of this collection.
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
     * @param memberType The type of the members of this collection, if null
     * then memebers of this collection are not constrained to be of any particular
     * type.
     */
    public void setMemberType(MetaDataType memberType)
            throws IllegalArgumentException {
        this.memberType =
                (ImmutableMetaDataType) MetaDataHelper.getImmutableOrNull(memberType);
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableMemberTypeConstraintImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableMemberTypeConstraintImpl(this);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof MemberTypeConstraint)
                ? equalsMemberTypeConstraint((MemberTypeConstraint) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>MemberTypeConstraint</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>MemberTypeConstraint</code> are equal to this one.
     */
    protected boolean equalsMemberTypeConstraint(MemberTypeConstraint other) {
        return MetaDataHelper.equals(memberType,
                other.getMemberType());
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(memberType);
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself
        EntryDAO entry = visitor.getNextEntry();
        // the following line handles null entries
        this.memberType = (ImmutableMetaDataType) visitor.getNextAsInhibitor();

    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == memberType) {
            visitor.add("null", MetadataClassMapper.NULL, true);
        } else {
            ((MetaDataObjectImpl)memberType).visitInhibitor(visitor);
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
