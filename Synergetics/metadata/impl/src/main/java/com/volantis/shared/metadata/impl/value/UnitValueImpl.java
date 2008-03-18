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
import com.volantis.shared.metadata.value.UnitValue;

/**
 * Implementation of {@link UnitValue}.
 */
abstract class UnitValueImpl
        extends SimpleValueImpl
        implements UnitValue {


    /**
     * The name of this unit.
     */
    private String name;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public UnitValueImpl(UnitValue value) {
        this.name = value.getAsString();
    }

    /**
     * Constructor which takes the name of this unit.
     * @param name The name of this unit.
     */
    public UnitValueImpl(String name) {
        this.name = name;
    }

    /**
     * Protected method for future use by JDO.
     */
    protected UnitValueImpl() {
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableUnitValueImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public String getAsString() {
        return name;
    }

    // Javadoc inherited
    public void setFromString(String value) {
        this.name = value;
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof UnitValue)
                ? equalsUnitValue((UnitValue) other)
                : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>UnitValue</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>UnitValue</code> are equal to this one.
     */
    public boolean equalsUnitValue(UnitValue other) {
        return MetaDataHelper.equals(name, other.getAsString());
    }

    // Javadoc inherited.
    public int hashCode() {
        return MetaDataHelper.hashCode(name);
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
