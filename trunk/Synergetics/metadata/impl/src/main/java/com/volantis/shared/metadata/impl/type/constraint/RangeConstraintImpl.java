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

import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.NumberHelper;
import com.volantis.shared.metadata.impl.value.jibx.MutableNumber;
import com.volantis.shared.metadata.impl.value.jibx.MutableNumberFactory;
import com.volantis.shared.metadata.type.constraint.RangeConstraint;

/**
 * Implementation of {@link RangeConstraint}.
 */
abstract class RangeConstraintImpl
        extends ConstraintImpl
        implements RangeConstraint {

    /**
     * Default value to return as limit if no limit was set.
     */
    private static final Number DEFAULT_VALUE = new Integer(1);

    /**
     * Determines whether this constraint is inclusive, or exclusive.
     */
    private boolean inclusive;

    /**
     * the limit of the constraint of this object.
     */
    private Number limit;

    /**
     * True, iff the limit is initialised.
     */
    private boolean initialized;

    /**
     * Copy constructor.
     *
     * @param constraint The object to copy.
     */
    protected RangeConstraintImpl(RangeConstraint constraint) {
        inclusive = constraint.isInclusive();
        limit = constraint.getLimitAsNumber();
        initialized = true;
    }

    /**
     * Protected constructor for sub classes.
     */
    protected RangeConstraintImpl() {
        limit = null;
        initialized = false;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself
        visitor.getNextEntry();
        EntryDAO entry = visitor.getNextEntry();
        this.inclusive = Boolean.getBoolean(entry.getName());
        // get the number
        entry = visitor.getNextEntry();
        if (entry.isNull()) {
            limit = null;
        } else {
            limit = NumberHelper.setFromString(entry.getName());
        }
        // get the number
        entry = visitor.getNextEntry();
        this.initialized = Boolean.getBoolean(entry.getName());
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        // push myself
        visitor.push(getClassMapper().toString(), getClassMapper());
        visitor.add(Boolean.toString(inclusive),
                    MetadataClassMapper.NULL, false);
        if (null == limit) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            visitor.add(NumberHelper.getAsString(limit),
                        MetadataClassMapper.NULL, false);
        }
        visitor.add(Boolean.toString(initialized),
                    MetadataClassMapper.NULL, false);
        visitor.pop();
    }

    /**
     * Get the limit of the constraint as a {@link Number}.
     *
     * <p>Returns Integer 1 as default value, if no limit was set.</p>
     *
     * @return The {@link Number} limit of the constraint, may not be null.
     */
    public Number getLimitAsNumber() {
        return initialized ? limit : DEFAULT_VALUE;
    }

    /**
     * Determines whether this constraint is inclusive, or exclusive.
     *
     * @return True if this constraint is inclusive, false otherwise.
     */
    public boolean isInclusive() {
        return inclusive;
    }

    /**
     * Set the limit of the constraint as a {@link Number}.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param number The {@link Number} limit of the constraint.
     */
    public void setLimit(final Number number) {
        this.limit = number;
        initialized = true;
    }

    /**
     * Sets the limit to the value of a mutable number.
     *
     * @note Added for JiBX only.
     *
     * @param number the mutable number that contains the value for the new
     * limit.
     */
    public void setLimitAsMutableNumber(MutableNumber number) {
        setLimit(number.getNumber());
    }

    /**
     * Returns the limit as mutable number.
     *
     * <p>Returns null, if no limit value was set before.</p>
     *
     * @note Added for JiBX only.
     *
     * @return the limit as mutable number or null
     */
    public MutableNumber getLimitAsMutableNumber() {
        return limit == null?
            null: MutableNumberFactory.createMutableNumber(limit);
    }

    /**
     * Set whether the constraint is inclusive, or exclusive.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param inclusive If true then the constraint is inclusive, otherwise it
     * is exclusive.
     */
    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    // Javadoc inherited.
    public int hashCode() {
        return getLimitAsNumber().hashCode() + MetaDataHelper.hashCode(inclusive);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof RangeConstraint)
                ? equalsRangeConstraint((RangeConstraint) other)
                : false;
    }

    protected boolean equalsRangeConstraint(RangeConstraint other) {
        return (inclusive == other.isInclusive()
                && getLimitAsNumber().equals(other.getLimitAsNumber()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/3	tom	VBM:2004122401 Changed Javadoc

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
