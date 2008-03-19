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
import com.volantis.shared.metadata.type.SetType;
import com.volantis.shared.metadata.value.SetValue;

/**
 * Implementation of {@link SetType}.
 */
abstract class SetTypeImpl extends CollectionTypeImpl implements SetType {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected SetTypeImpl(SetType value) {
        super(value);
    }

    /**
     * Protected method for future use by JDO.
     */
    protected SetTypeImpl() {
    }


    // Javadoc inherited.
    public ImmutableInhibitor createImmutable() {
        return new ImmutableSetTypeImpl(this);
    }

    // Javadoc inherited.
    public MutableInhibitor createMutable() {
        return new MutableSetTypeImpl(this);
    }

    // javadoc inherited
    protected Class getExpectedValueType() {
        return SetValue.class;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
