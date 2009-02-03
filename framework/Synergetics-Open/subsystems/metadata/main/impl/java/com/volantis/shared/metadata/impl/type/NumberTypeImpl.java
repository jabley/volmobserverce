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
import com.volantis.shared.metadata.type.NumberType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.MaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.MinimumValueConstraint;
import com.volantis.shared.metadata.type.constraint.NumberSubTypeConstraint;
import com.volantis.shared.metadata.type.constraint.EnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMaximumValueConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableMinimumValueConstraint;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableNumberSubTypeConstraint;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.synergetics.utilities.NumberComparator;

import java.util.Collection;
import java.util.Comparator;

/**
 * Implementation of {@link SimpleTypeImpl}.
 */
abstract class NumberTypeImpl
        extends SimpleTypeImpl
        implements NumberType {

    /**
     * Comparator to check minimum and maximum constraints.
     */
    private static final Comparator NUMBER_COMPARATOR = new NumberComparator();

    /**
     * The constraint on the subtype of {@link Number}.
     */
    private ImmutableNumberSubTypeConstraint numberSubTypeConstraint;

    /**
     * The constraint on the minimum value of this type.
     */
    private ImmutableMinimumValueConstraint minimumValueConstraint;

    /**
     * The constraint on the maximum value of this type.
     */
    private ImmutableMaximumValueConstraint maximumValueConstraint;

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    protected NumberTypeImpl(NumberType type) {
        super(type);
        numberSubTypeConstraint = type.getNumberSubTypeConstraint();
        minimumValueConstraint = type.getMinimumValueConstraint();
        maximumValueConstraint = type.getMaximumValueConstraint();
    }

    /**
     * Protected constructor for future use by JDO.
     */
    protected NumberTypeImpl() {
    }


    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableNumberTypeImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableNumberTypeImpl(this);
    }

    // Javadoc inherited.
    public ImmutableNumberSubTypeConstraint getNumberSubTypeConstraint() {
        return numberSubTypeConstraint;
    }

    // Javadoc inherited.
    public ImmutableMinimumValueConstraint getMinimumValueConstraint() {
        return minimumValueConstraint;
    }

    // Javadoc inherited.
    public ImmutableMaximumValueConstraint getMaximumValueConstraint() {
        return maximumValueConstraint;
    }

    /**
     * Set the constraint on the sub type value of this type.
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
     * @param constraint The constraint on the sub type of {@link Number},
     * may be null.
     */
    public void setNumberSubTypeConstraint(NumberSubTypeConstraint constraint) {
        setNumberSubTypeConstraint((ImmutableNumberSubTypeConstraint)
                MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable number sub type constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new number sub type constraint
     */
    public void setNumberSubTypeConstraint(
            ImmutableNumberSubTypeConstraint constraint) {
        numberSubTypeConstraint = constraint;
    }

    /**
     * Set the constraint on the minimum value of this type.
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
     * @param constraint The constraint on the minimum value of this type,
     * may be null.
     */
    public void setMinimumValueConstraint(MinimumValueConstraint constraint) {
        setMinimumValueConstraint((ImmutableMinimumValueConstraint)
                MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable minimum value constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new minimum value constraint
     */
    public void setMinimumValueConstraint(
            ImmutableMinimumValueConstraint constraint) {
        minimumValueConstraint = constraint;
    }

    /**
     * Set the constraint on the maximum value of this type.
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
     * @param constraint The constraint on the maximum value of this type,
     * may be null.
     */
    public void setMaximumValueConstraint(MaximumValueConstraint constraint) {
        setMaximumValueConstraint((ImmutableMaximumValueConstraint)
                MetaDataHelper.getImmutableOrNull(constraint));
    }

    /**
     * Sets the constraint to an immutable maximum value constraint.
     *
     * @note Added for JiBX only.
     *
     * @param constraint the new maximum value constraint
     */
    public void setMaximumValueConstraint(
            ImmutableMaximumValueConstraint constraint) {
        maximumValueConstraint = constraint;
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof NumberType) &&
            equalsNumberType((NumberType) other);
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>NumberType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>NumberType</code> are equal to this one.
     */
    protected boolean equalsNumberType(NumberType other) {
        return equalsSimpleType(other)
                && MetaDataHelper.equals(numberSubTypeConstraint,
                        other.getNumberSubTypeConstraint())
                && MetaDataHelper.equals(minimumValueConstraint,
                        other.getMinimumValueConstraint())
                && MetaDataHelper.equals(maximumValueConstraint,
                        other.getMaximumValueConstraint());
    }


    // Javadoc inherited.
    public int hashCode() {
        return 17 * super.hashCode()
                + MetaDataHelper.hashCode(numberSubTypeConstraint)
                + MetaDataHelper.hashCode(minimumValueConstraint)
                + MetaDataHelper.hashCode(maximumValueConstraint);
    }

    // Javadoc inherited.
    protected Class getExpectedValueType() {
        return NumberValue.class;
    }

    // Javadoc inherited.
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        // if this is not a NumberValue, a type error is reported by the super
        // class
        if (value instanceof NumberValue) {
            final NumberValue numberValue = (NumberValue) value;
            final Number number = numberValue.getValueAsNumber();
            if (number != null) {
                // check number sub-type constraint
                if (numberSubTypeConstraint != null) {
                    if (!numberSubTypeConstraint.getNumberSubType().equals(
                            number.getClass())) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION, path,
                            value, numberSubTypeConstraint,
                            "Invalid number type. Expected: " +
                                numberSubTypeConstraint.getNumberSubType() +
                                ", found: " + number.getClass()));
                    }
                }
                // check minimum value constraint
                if (minimumValueConstraint != null) {
                    final Number minimum =
                        minimumValueConstraint.getLimitAsNumber();
                    final boolean inclusive =
                        minimumValueConstraint.isInclusive();
                    final int compareResult =
                        NUMBER_COMPARATOR.compare(number, minimum);
                    if (compareResult < 0 || compareResult == 0 && !inclusive) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION, path,
                            value, minimumValueConstraint,
                            "Minimum value constraint violated. " +
                                "Minimum value: " + minimum +
                                (inclusive? " (inclusive)": " (exclusive)") +
                                ", received: " + number));
                    }
                }
                // check maximum value constraint
                if (maximumValueConstraint != null) {
                    final Number maximum =
                        maximumValueConstraint.getLimitAsNumber();
                    final boolean inclusive =
                        maximumValueConstraint.isInclusive();
                    final int compareResult =
                        NUMBER_COMPARATOR.compare(maximum, number);
                    if (compareResult < 0 || compareResult == 0 && !inclusive) {
                        errors.add(new VerificationError(
                            VerificationError.TYPE_CONSTRAINT_VIOLATION, path,
                            value, maximumValueConstraint,
                            "Maximum value constraint violated. " +
                                "Maximum value: " + maximum +
                                (inclusive? " (inclusive)": " (exclusive)") +
                                ", received: " + number));
                    }
                }
            } else {
                errors.add(new VerificationError(
                    VerificationError.TYPE_NULL_VALUE, path, value, null,
                    "Value cannot be null."));
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
        maximumValueConstraint = (ImmutableMaximumValueConstraint)
            visitor.getNextAsInhibitor();
        minimumValueConstraint = (ImmutableMinimumValueConstraint)
            visitor.getNextAsInhibitor();
        numberSubTypeConstraint = (ImmutableNumberSubTypeConstraint)
            visitor.getNextAsInhibitor();
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == getEnumeratedConstraint()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getEnumeratedConstraint())
                .visitInhibitor(visitor);
        }
        if (null == maximumValueConstraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) maximumValueConstraint)
                .visitInhibitor(visitor);
        }
        if (null == minimumValueConstraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) minimumValueConstraint)
                .visitInhibitor(visitor);
        }
        if (null == numberSubTypeConstraint) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) numberSubTypeConstraint)
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
