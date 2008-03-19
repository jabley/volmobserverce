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
import com.volantis.shared.metadata.value.BooleanValue;

/**
 * Implementation of {@link BooleanValue}.
 *
 * <p><strong>Note</strong>: This is package private so that it cannot be
 * accessed from outside this package. This is primarily to protect the
 * mutator method(s) from being called on an immutable instance of this
 * class. All access to these mutators must be done through the relevant
 * mutable interface and not directly via this class.</p>
 */
abstract class BooleanValueImpl
        extends SimpleValueImpl
        implements BooleanValue {

    /**
     * The value of the object.
     */
    private boolean value;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected BooleanValueImpl(BooleanValue value) {
        this.value = value.getValueAsBoolean().booleanValue();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected BooleanValueImpl() {
    }

    // Javadoc inherited.
    public Boolean getValueAsBoolean() {
        return value ? Boolean.TRUE : Boolean.FALSE;
    }

    // Javadoc inherited.
    public String getAsString() {
        return value ? "true" : "false";
    }

    // Javadoc inherited
    public void setFromString(String value) {
        this.value = Boolean.valueOf(value).booleanValue();
    }

    /**
     * Override to create appropriate immutable object.
     */
    public ImmutableInhibitor createImmutable() {
        return new ImmutableBooleanValueImpl(this);
    }

    /**
     * Override to create appropriate mutable object.
     */
    public MutableInhibitor createMutable() {
        return new MutableBooleanValueImpl(this);
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
    public void setValue(Boolean b) {
        this.value = b.booleanValue();
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanValue)) {
            return false;
        }

        BooleanValue other = (BooleanValue) obj;

        return value == other.getValueAsBoolean().booleanValue();
    }

    // Javadoc inherited.
    public int hashCode() {
        return value ? 1 : 0;
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
