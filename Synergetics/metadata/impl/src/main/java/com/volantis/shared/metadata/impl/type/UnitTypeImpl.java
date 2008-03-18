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
import com.volantis.shared.metadata.type.UnitType;
import com.volantis.shared.metadata.value.UnitValue;

/**
 * Implementation of {@link UnitType}.
 */
abstract class UnitTypeImpl
        extends SimpleTypeImpl
        implements UnitType {

    /**
     * Protected constructor for sub classes.
     */
    protected UnitTypeImpl() {
    }

    /**
     * Copy constructor.
     *
     * @param type The object to copy.
     */
    protected UnitTypeImpl(UnitType type) {
        super(type);
    }

    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableUnitTypeImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableUnitTypeImpl(this);
    }

    // Javadoc inherited.
    public boolean equals(Object other) {
        return (other instanceof UnitType) &&
            equalsSimpleType((UnitType) other);
    }

    // Javadoc inherited.
    protected Class getExpectedValueType() {
        return UnitValue.class;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 14-Jan-05	6560/3	tom	VBM:2004122401 Completed Metadata API Implementation

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
