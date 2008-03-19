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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;
import com.volantis.shared.metadata.impl.persistence.EntryDAO;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.value.QuantityUnits;
import com.volantis.shared.metadata.value.QuantityValue;
import com.volantis.shared.metadata.value.UnitValue;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;

/**
 * Implementation of {@link QuantityValue}.
 */
abstract class QuantityValueImpl
        extends MetaDataValueImpl
        implements QuantityValue {

    /**
     * The magnitude value.
     */
    private ImmutableNumberValue magnitudeValue;

    /**
     * The unit value.
     */
    private ImmutableUnitValue unitValue;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public QuantityValueImpl(QuantityValue value) {
        this.magnitudeValue = value.getMagnitudeValue();
        this.unitValue = value.getUnitValue();
    }

    /**
     * Protected method for future use by JDO.
     */
    protected QuantityValueImpl() {
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableQuantityValueImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableQuantityValueImpl(this);
    }

    // Javadoc inherited.
    public String getAsString() {
        StringBuffer s = new StringBuffer();
        s.append(magnitudeValue);
        s.append("");
        s.append(unitValue);
        return s.toString();
    }

    /**
     * Get the magnitude value.
     *
     * @return The magnitude value.
     */
    public ImmutableNumberValue getMagnitudeValue() {
        return magnitudeValue;
    }

    /**
     * Get the unit value.
     *
     * @return The unit value.
     */
    public ImmutableUnitValue getUnitValue() {
        return unitValue;
    }

    /**
     * Get the magnitude of the quantity in the specified units.
     *
     * <p>The behaviour is dependent on the relationship between the unit
     * stored internally (as returned by {@link #getUnitValue()}) and the
     * unit specified as a parameter to this method.</p>
     *
     * <ol>
     * <li>If they are the same then the magnitude of this quantity is
     * simply returned.</li>
     * <li>If they are equivalent (i.e. can be converted without loss of
     * information) then the quantity is converted to the specified unit and
     * the resulting magnitude returned. This does not change the externally
     * visible state of this object.</li>
     * <li>Otherwise, null is returned.</li>
     * </ol>
     *
     * @param unit The unit in which the quantity is requested, may not be null.
     *
     * @return An unmodifiable Number value, may be null.
     */
    public Number getMagnitudeAsNumber(UnitValue unit) {
        Number result = null;
        if (unit != null && unitValue != null && magnitudeValue != null) {
            if (unitValue.equals(unit)) {
                result = magnitudeValue.getValueAsNumber();
            } else {

                if (unit.equals(QuantityUnits.PIXELS)) {
                    // we can't convert to pixels without more information
                    return null;
                }

                Number valueInCM = getValueInCentimeters(unitValue,
                        magnitudeValue.getValueAsNumber());
                result = convertValueFromCM(unit, valueInCM);
            }
        }
        return result;
    }

    /**
     * Gets a value in Centimeters
     * @param originalUnit The original unit of the value
     * @param value The value
     * @return the value in Centimeters
     */
    private Double getValueInCentimeters(UnitValue originalUnit, Number value) {
        double result;
        double valueDouble = value.doubleValue();
        if (originalUnit.equals(QuantityUnits.MILLIMETERS)) {
            result = valueDouble / 10;
        } else if (originalUnit.equals(QuantityUnits.INCHES)) {
            result = valueDouble * 2.54;
        } else if (originalUnit.equals(QuantityUnits.CENTIMETERS)) {
            result = valueDouble;
        } else {
            throw new IllegalArgumentException("Unknown UnitValue: " + originalUnit);
        }
        return new Double(result);
    }

    /**
     * Gets the provided value in CM and converts it to the specified unit
     * @param newUnit The desired unit of the value
     * @param valueInCM The value
     * @return the value in Centimeters
     */
    private Double convertValueFromCM(UnitValue newUnit, Number valueInCM) {
        double result;
        double valueDouble = valueInCM.doubleValue();
        if (newUnit.equals(QuantityUnits.MILLIMETERS)) {
            result = valueDouble * 10;
        } else if (newUnit.equals(QuantityUnits.INCHES)) {
            result = valueDouble / 2.54;
        } else if (newUnit.equals(QuantityUnits.CENTIMETERS)) {
            result = valueDouble;
        } else {
            throw new IllegalArgumentException("Unknown UnitValue: " + newUnit);
        }
        return new Double(result);
    }

    /**
     * Set the magnitude value.
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
     * @param magnitudeValue The magnitude value.
     */
    public void setMagnitudeValue(NumberValue magnitudeValue) {
        this.magnitudeValue = (ImmutableNumberValue)
                MetaDataHelper.getImmutableOrNull(magnitudeValue);
    }

    /**
     * Set the unit value.
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
     * @param unitValue The unit value.
     */
    public void setUnitValue(UnitValue unitValue) {
        this.unitValue = (ImmutableUnitValue) MetaDataHelper.getImmutableOrNull(unitValue);
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(magnitudeValue)
                + MetaDataHelper.hashCode(unitValue);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof QuantityValue)
                ? equalsQuantityValue((QuantityValue) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>QuantityValue</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>QuantityValue</code> are equal to this one.
     */
    protected boolean equalsQuantityValue(QuantityValue other) {
        return MetaDataHelper.equals(magnitudeValue,
                other.getMagnitudeValue())
                && MetaDataHelper.equals(unitValue,
                        other.getUnitValue());
    }

    // Javadoc inherited
    public void initializeInhibitor(MetadataDAOVisitor visitor) {
        // get myself
        EntryDAO entry = visitor.getNextEntry();
        magnitudeValue = (ImmutableNumberValue) visitor.getNextAsInhibitor();
        unitValue = (ImmutableUnitValue) visitor.getNextAsInhibitor();

    }

    // Javadoc inherited
    public void visitInhibitor(MetadataDAOVisitor visitor) {
        visitor.push(getClassMapper().toString(), getClassMapper());
        if(null == magnitudeValue) {
            visitor.add(null, MetadataClassMapper.NULL, true);
        } else {
            ((InhibitorImpl)magnitudeValue).visitInhibitor(visitor);
        }

        if (null == unitValue) {
            visitor.add(null, MetadataClassMapper.NULL, true
            );
        } else {
            ((InhibitorImpl) unitValue).visitInhibitor(visitor);
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

 14-Jan-05	6560/1	tom	VBM:2004122401 Completed Metadata API Implementation

 ===========================================================================
*/
