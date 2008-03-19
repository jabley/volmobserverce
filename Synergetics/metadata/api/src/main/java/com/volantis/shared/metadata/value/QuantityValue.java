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

package com.volantis.shared.metadata.value;

import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;

/**
 * A quantity value.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface QuantityValue
        extends MetaDataValue {

    /**
     * Get the magnitude value.
     *
     * @return The magnitude value.
     */
    public ImmutableNumberValue getMagnitudeValue();

    /**
     * Get the unit value.
     *
     * @return The unit value.
     */
    public ImmutableUnitValue getUnitValue();

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
    public Number getMagnitudeAsNumber(UnitValue unit);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
