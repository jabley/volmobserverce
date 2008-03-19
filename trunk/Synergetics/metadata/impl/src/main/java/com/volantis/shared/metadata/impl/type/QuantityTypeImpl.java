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
import com.volantis.shared.metadata.type.QuantityType;
import com.volantis.shared.metadata.type.UnitType;
import com.volantis.shared.metadata.type.immutable.ImmutableNumberType;
import com.volantis.shared.metadata.type.immutable.ImmutableUnitType;
import com.volantis.shared.metadata.value.QuantityValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;

/**
 * Implementation of {@link QuantityType}.
 */
class QuantityTypeImpl extends MetaDataTypeImpl implements QuantityType {

    /**
     * The type of the magnitude.
     */
    private ImmutableNumberType magnitudeType;

    /**
     * The type of the unit.
     */
    private ImmutableUnitType unitType;

    /**
     * Protected constructor for sub classes.
     */
    protected QuantityTypeImpl() {
    }

    /**
     * Copy constructor
     * @param type The type to copy.
     */
    protected QuantityTypeImpl(QuantityType type) {
        magnitudeType = type.getMagnitudeType();
        unitType = type.getUnitType();
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableQuantityTypeImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableQuantityTypeImpl(this);
    }

    // Javadoc inherited.
    public ImmutableNumberType getMagnitudeType() {
        return magnitudeType;
    }

    // Javadoc inherited.
    public ImmutableUnitType getUnitType() {
        return unitType;
    }

    /**
     * Set the type of the magnitude.
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
     * @param magnitudeType The type of the magnitude.
     */
    public void setMagnitudeType(NumberType magnitudeType) {
        this.magnitudeType =
                (ImmutableNumberType) MetaDataHelper.getImmutableOrNull(magnitudeType);
    }

    /**
     * Set the type of the unit.
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
     * @param unitType The type of the unit.
     */
    public void setUnitType(UnitType unitType) {
        this.unitType = (ImmutableUnitType) MetaDataHelper.getImmutableOrNull(unitType);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        return obj instanceof QuantityTypeImpl &&
            equalsQuantityType((QuantityType) obj);
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>QuantityType</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>QuantityType</code> are equal to this one.
     */
    private boolean equalsQuantityType(QuantityType other) {
        return MetaDataHelper.equals(magnitudeType,
                other.getMagnitudeType())
                && MetaDataHelper.equals(unitType,
                        other.getUnitType());
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(magnitudeType)
                + MetaDataHelper.hashCode(unitType);
    }

    // javadoc inherited
    protected Class getExpectedValueType() {
        return QuantityValue.class;
    }

    // javadoc inherited
    protected Collection verify(final MetaDataValue value, final String path) {
        final Collection errors = super.verify(value, path);
        if (value instanceof QuantityValue) {
            final QuantityValue quantityValue = (QuantityValue) value;
            // check magnitude
            if (magnitudeType != null) {
                errors.addAll(((MetaDataTypeImpl) magnitudeType).verify(
                    quantityValue.getMagnitudeValue(), path + "[@magnitude]"));
            }
            // check unit
            if (unitType != null) {
                errors.addAll(((MetaDataTypeImpl) unitType).verify(
                    quantityValue.getUnitValue(), path + "[@unit]"));
            }
        }
        return errors;
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself and ignore it
        visitor.getNextEntry();
        setMagnitudeType(
            (NumberType) visitor.getNextAsInhibitor());
        setUnitType((UnitType) visitor.getNextAsInhibitor());
    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if (null == getMagnitudeType()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getMagnitudeType())
                .visitInhibitor(visitor);
        }
        if (null == getUnitType()) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl) getUnitType())
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

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
