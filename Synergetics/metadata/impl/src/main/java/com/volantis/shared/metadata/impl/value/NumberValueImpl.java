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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.value.jibx.MutableNumber;
import com.volantis.shared.metadata.impl.value.jibx.MutableNumberFactory;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.impl.persistence.MetadataDAOVisitor;
import com.volantis.shared.metadata.impl.persistence.NumberHelper;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Implementation of {@link NumberValue}.
 *
 * <p><strong>Note</strong>: This is package private so that it cannot be
 * accessed from outside this package. This is primarily to protect the
 * mutator method(s) from being called on an immutable instance of this
 * class. All access to these mutators must be done through the relevant
 * mutable interface and not directly via this class.</p>
 */
abstract class NumberValueImpl
        extends SimpleValueImpl
        implements NumberValue {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(NumberValueImpl.class);

    /**
     * The value of the object.
     */
    private transient Number value;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected NumberValueImpl(NumberValue value) {
        this.value = value.getValueAsNumber();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected NumberValueImpl() {
    }

    // Javadoc inherited.
    public Number getValueAsNumber() {
        return value;
    }



    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableNumberValueImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableNumberValueImpl(this);
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
    public void setValue(Number number) {
        this.value = number;
    }

    /**
     * Sets the value using a MutableNumber.
     *
     * @note Added for the JiBX marshalling/unmarshalling only.
     *
     * @param number the new value
     */
    public void setMutableNumber(final MutableNumber number) {
        this.value = number.getNumber();
    }

    /**
     * Returns the value as a MutableNumber.
     *
     * @note Added for the JiBX marshalling/unmarshalling only.
     *
     * @return the value wrapped in a MutableNumber.
     */
    public MutableNumber getMutableNumber() {
        return value == null? null: MutableNumberFactory.createMutableNumber(value);
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof NumberValue)) {
            return false;
        }

        final NumberValue other = (NumberValue) obj;
        final Number otherValue = other.getValueAsNumber();
        return (value == null && otherValue == null) ||
            (value != null && value.equals(otherValue));
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(value);
    }

    // Javadoc inherited
    public void setFromString(String value) {
        this.value = NumberHelper.setFromString(value);
    }

    // Javadoc inherited
    public String getAsString() {
        return NumberHelper.getAsString(value);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
