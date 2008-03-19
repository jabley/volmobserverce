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

import com.volantis.shared.metadata.MetaDataObject;

/**
 * This interface represents a value of a MetaDataType.
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
public interface MetaDataValue
        extends MetaDataObject {

    /**
     * Get a String representation of this value.
     *
     * <p>
     * For simple types the value is returned as is e.g Boolean values are
     * represented by the strings "true" and "false"
     * </p>
     * <p>
     * For composite types an attempt is made to represent the value in as
     * sensible manner as is possible, no guarantee is made that it will be
     * possible to reconstruct the original value from the string
     * representation.
     * </p>
     * <p>
     * List types are returned space separated e.g "item1 item2 item3".
     * </p>
     * <p>
     * Structure types are returned as "[fieldname1=value1] [fieldname2=value2]"
     * where the values follow the rules above.
     * </p>
     *
     * @return an unmodifiable String value
     */
    public String getAsString();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jan-05	6670/4	adrianj	VBM:2005010506 Implementation of resource asset continued

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
