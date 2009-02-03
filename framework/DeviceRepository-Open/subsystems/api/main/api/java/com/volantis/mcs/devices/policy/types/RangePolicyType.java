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
package com.volantis.mcs.devices.policy.types;

import com.volantis.shared.metadata.type.immutable.ImmutableNumberType;

/**
 * This interface represents a Range Policy Type.
 * 
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated Ranges are defined as constraints on the minimum and maximum
 * values of a {@link ImmutableNumberType}.
 *
 * e.g. For a range with min of 10 and max of 20 the minimum constraint must be
 * inclusive and have a value of 10 and the maximum constraint must be
 * inclusive and have a value of 20.
 */
public interface RangePolicyType extends SimplePolicyType {

    /**
     * Gets the minimum inclusive value for the range.
     * 
     * @return An int representing the minimum inclusive value of the range.
     */
    public int getMinInclusive();

    /**
     * Gets the maximum inclusive value for the range.
     * 
     * @return An int representing the maximum inclusive value of the range.
     */
    public int getMaxInclusive();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Jan-05	6712/1	pduffin	VBM:2005011713 Fix minor problem with Im/MutableMetaDataObject

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
