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
import com.volantis.shared.metadata.value.StringValue;

/**
 * Implementation of {@link StringValue}.
 */
abstract class StringValueImpl
        extends SimpleValueImpl
        implements StringValue {

    /**
     * The String value;
     */
    private String value;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StringValueImpl(StringValue value) {
        this.value = value.getValueAsString();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StringValueImpl() {
    }


    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableStringValueImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableStringValueImpl(this);
    }

    // Javadoc inherited.
    public String getAsString() {
        return value;
    }

    // Javadoc inherited
    public void setFromString(String value) {
        this.value = value;
    }

    // Javadoc inherited.
    public String getValueAsString() {
        return value;
    }

    /**
     * Set the underlying String value.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param string The String value.
     */
    public void setValue(String string) {
        value = string;
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(value);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof StringValue)
                ? equalsStringValue((StringValue) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>StringValue</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>StringValue</code> are equal to this one.
     */
    protected boolean equalsStringValue(StringValue other) {
        return MetaDataHelper.equals(value,
                other.getValueAsString());
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
