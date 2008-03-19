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

import com.volantis.shared.metadata.value.QuantityValue;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.metadata.impl.persistence.MetadataClassMapper;

/**
 * Implementation of {@link MutableQuantityValue}.
 */
final class MutableQuantityValueImpl
        extends QuantityValueImpl
        implements MutableQuantityValue {

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    public MutableQuantityValueImpl(QuantityValue value) {
        super(value);
    }

    /**
     * Public constructor for use by factory.
     */
    public MutableQuantityValueImpl() {
    }

    // Javadoc inherited
    public MetadataClassMapper getClassMapper() {
        return MetadataClassMapper.MUTABLE_QUANTITY_VALUE;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/3	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/1	tom	VBM:2004122401 Completed Metadata API Implementation

 ===========================================================================
*/
