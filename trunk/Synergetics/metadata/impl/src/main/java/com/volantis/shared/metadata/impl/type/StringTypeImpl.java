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
import com.volantis.shared.metadata.type.StringType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.MaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.MinimumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.EnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumLengthConstraint;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.StringValue;

import java.util.Collection;

/**
 * Implementation of {@link com.volantis.shared.metadata.type.StringType}.
 */
abstract class StringTypeImpl
        extends SimpleTypeImpl
        implements StringType {

    /**
     * The minimum length constraint for this object.
     */
    private ImmutableMinimumLengthConstraint minimumLengthConstraint;

    /**
     * The maximum length constraint for this object.
     */
    private ImmutableMaximumLengthConstraint maximumLengthConstraint;

    /**
     * Protected constructor for sub classes.
     */
    protected StringTypeImpl() {
    }

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    protected StringTypeImpl(StringType type) {
        super(type);
        minimumLengthConstraint = type.getMinimumLengthConstraint();
        maximumLengthConstraint = type.getMaximumLengthConstraint();
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
    public ImmutableInhibitor createImmutable() {
        return new ImmutableStringTypeImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableStringTypeImpl(this);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + MetaDataHelper.hashCode(minimumLengthConstraint);
        return 29 * result + MetaDataHelper.hashCode(maximumLengthConstraint);
    }

    // Javadoc inherited.
    public boolean equals(final Object other) {
        return (other instanceof StringType) &&
            equalsSimpleType((StringType) other) &&
            equalsStringType((StringType) other);
    }
    /**
     * Helper method for {@link #equals} which compares two objects of this type
     * for equality.
     * @param other The other <code>StringType/code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>StringType</code> are equal to this one.
     */
    protected boolean equalsStringType(final StringType other) {
        return MetaDataHelper.equals(minimumLengthConstraint,
                other.getMinimumLengthConstraint()) &&
            MetaDataHelper.equals(maximumLengthConstraint,
                other.getMaximumLengthConstraint());
    }


    // Javadoc inherited.
    protected Class getExpectedValueType() {
        return StringValue.class;
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        if (value instanceof StringValue) {
            final StringValue stringValue = (StringValue) value;
            final String string = stringValue.getValueAsString();
            if (string == null) {
                errors.add(new VerificationError(
                    VerificationError.TYPE_NULL_VALUE, path, value, null,
                    "Value cannot be null."));
            } else {
                // check minimum length constraint
                if (minimumLengthConstraint != null) {
                    final int minimumLength = minimumLengthConstraint.getLimit();
                    if (minimumLength > string.length()) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION, path,
                            value, minimumLengthConstraint, "Minimum length " +
                                "constraint violation. Minimum length is " +
                                minimumLength + ", but the length of the " +
                                "string is " + string.length() + "."));
                    }
                }
                // check minimum length constraint
                if (maximumLengthConstraint != null) {
                    final int maximumLength = maximumLengthConstraint.getLimit();
                    if (string.length() > maximumLength) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION, path,
                            value, maximumLengthConstraint, "Maximum length " +
                                "constraint violation. Maximum length is " +
                                maximumLength + ", but the length of the " +
                                "string is " + string.length() + "."));
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
        setEnumeratedConstraint(
            (EnumeratedConstraint) visitor.getNextAsInhibitor());
        maximumLengthConstraint = (ImmutableMaximumLengthConstraint)
            visitor.getNextAsInhibitor();
        minimumLengthConstraint = (ImmutableMinimumLengthConstraint)
            visitor.getNextAsInhibitor();
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == getEnumeratedConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getEnumeratedConstraint()).visitInhibitor(visitor);
        }
        if (null == maximumLengthConstraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) maximumLengthConstraint)
                .visitInhibitor(visitor);
        }
        if (null == minimumLengthConstraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) minimumLengthConstraint)
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

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
