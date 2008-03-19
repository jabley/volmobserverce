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

package com.volantis.shared.metadata.type;

import com.volantis.shared.metadata.MetaDataObject;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;

/**
 * The base interface for all meta data types.
 *
 * <p>{@link MetaDataType}s can have a number of different constraints
 * associated with it. If a specific constraint has not been associated (i.e.
 * it is null) then it does not constrain the set of allowable values that the
 * type can have.</p>
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
public interface MetaDataType
        extends MetaDataObject {

    /**
     * Verifies if the specified meta data value has the expected type and meets
     * all of the constraints of this type.
     *
     * <p>The returned collection contains all of the verification errors that
     * were found during the check. Members of the collection are
     * {@link VerificationError}s.</p>
     *
     * <p>Empty collection is returned if no errors were found.</p>
     *
     * @param value the value to check
     * @return collection of VerificationErrors
     */
    Collection verify(MetaDataValue value);
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
