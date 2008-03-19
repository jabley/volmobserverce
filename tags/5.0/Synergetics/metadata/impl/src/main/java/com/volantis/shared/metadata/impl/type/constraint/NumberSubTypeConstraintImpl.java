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
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;

/**
 * Implementation of {@link NumberSubTypeConstraint}.
 */
abstract class NumberSubTypeConstraintImpl
        extends ConstraintImpl
        implements NumberSubTypeConstraint {

    /**
     * The sub type of {@link Number} that is allowed within a
     * {@link com.volantis.shared.metadata.type.NumberType}. The default value of this
     * field is the {@link Number} class.
     */
    private Class subType;

    /**
     * Protected constructor for future use by JDO.
     */
    protected NumberSubTypeConstraintImpl() {
        // The Number class is the default value for this object.
        subType = Number.class;
    }

    /**
     * Constructor which takes a <code>Class</code> which should be a subtype of
     * <code>Number</code>.
     * @param numberSubType The <code>Number</code> sub type. Can not be null.
     */
    protected NumberSubTypeConstraintImpl(Class numberSubType) {
        if (numberSubType == null) {
            throw new IllegalArgumentException("Can not be null: numberSubType");
        }
        subType = numberSubType;
    }

    /**
     * Copy constructor.
     * @param numberSubTypeConstraint The object to copy.
     */
    protected NumberSubTypeConstraintImpl(
            NumberSubTypeConstraint numberSubTypeConstraint) {
        this.subType = numberSubTypeConstraint.getNumberSubType();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableNumberSubTypeConstraintImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableNumberSubTypeConstraintImpl(this);
    }

    // Javadoc inherited.
    public Class getNumberSubType() {
        return subType;
    }

    /**
     * Set the sub type of {@link Number} that is allowed within a
     * {@link com.volantis.shared.metadata.type.NumberType}.
     *
     * <p>The specified <code>subType</code> must satisfy the following
     * expression, otherwise a {@link IllegalArgumentException} is thrown.</p>
     * <pre>
     *     Number.class.isAssignableFrom(subType)
     * </pre>
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param subType The sub type of {@link Number}.
     * @throws IllegalArgumentException If the specified class is not assignable to
     *         {@link Number}.
     */
    public void setNumberSubType(Class subType) {
        this.subType = subType;
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof NumberSubTypeConstraint)
                ? equalsNumberSubTypeConstraint((NumberSubTypeConstraint) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>NumberSubTypeConstraint</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>NumberSubTypeConstraint</code> are equal to this one.
     */
    protected boolean equalsNumberSubTypeConstraint(
            NumberSubTypeConstraint other) {
        return MetaDataHelper.equals(getNumberSubType(),
                other.getNumberSubType());
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(getNumberSubType());
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself
        EntryDAO entry = visitor.getNextEntry();
        entry = visitor.getNextEntry();
        if (!entry.isNull()) {
            try {
                subType = Class.forName(entry.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (subType == null) {
            visitor.add("null",
                        MetadataClassMapper.NULL, true);
        } else {
            visitor.add(subType.getName(),
                        MetadataClassMapper.NULL, false);
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

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
