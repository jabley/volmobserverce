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
package com.volantis.devrep.repository.impl.devices.policy.types;

import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;
import com.volantis.shared.metadata.type.immutable.ImmutableNumberType;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;

/**
 * Default implementation of {@link IntPolicyType}.
 */ 
public class DefaultIntPolicyType
        extends DefaultPolicyType
        implements IntPolicyType {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Default meta data type used to represent this object.
     */
    private static final ImmutableNumberType DEFAULT_NUMBER_TYPE;

    // Initialise the default meta data type.
    static {
        MutableNumberType type = TYPE_FACTORY.createNumberType();
        DEFAULT_NUMBER_TYPE = (ImmutableNumberType) type.createImmutable();
    }

    // Javadoc inherited.
    public ImmutableMetaDataType createMetaDataType() {
        return DEFAULT_NUMBER_TYPE;
    }

    
    // Javadoc inherited.
    public String toString() {
        return "[DefaultIntPolicyType]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4956/1	geoff	VBM:2004072305 Public API for Device Repository: metadata support for import tool (finalise)

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 ===========================================================================
*/
